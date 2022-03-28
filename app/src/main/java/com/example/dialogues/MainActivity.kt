package com.example.dialogues

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.dialogues.databinding.ActivityMainBinding
import com.example.dialogues.models.AvailableVolumeValues
import kotlin.properties.Delegates
import kotlin.properties.Delegates.notNull

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var volume by notNull<Int>()
    private var color by notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.defaultDialogue.setOnClickListener { showAlertAlertDialog() }
        binding.singleChoiceDialogue.setOnClickListener { showSingleChoiceAlertDialogue() }
        binding.singleChoiceDialogueWithConfirmation.setOnClickListener { showSingleChoiceWithConfirmationAlertDialogue() }
        binding.multipleChoiceDialogue.setOnClickListener { showMultipleChoiceAlertDialogue() }
        binding.multipleChoiceDialogueWithConfirmation.setOnClickListener { showMultipleChoiceWithConfirmationDialogue() }

        volume = 50
        color = Color.GREEN

        updateUI()
    }

    private fun showAlertAlertDialog() {

        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> showToast("Hell Yeah!")
                DialogInterface.BUTTON_NEGATIVE -> showToast("NO!!!")
                DialogInterface.BUTTON_NEUTRAL -> showToast("Bless me...")
            }
        }

        val dialog = AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_launcher_background)
            .setTitle("Default Alert Dialogue")
            .setMessage("Are you sure?")
            .setCancelable(false)
            .setPositiveButton("Yes", listener)
            .setNegativeButton("No", listener)
            .setNeutralButton("IDK", listener)
            .create()

        dialog.show()
    }

    private fun showSingleChoiceAlertDialogue() {
        val volumeItems = AvailableVolumeValues.createVolumeValues(volume)
        val volumeTextItems = volumeItems.values
            .map { getString(R.string.volume_description, it) }
            .toTypedArray()

        val dialogue = AlertDialog.Builder(this)
            .setTitle("Volume Setup")
            .setSingleChoiceItems(volumeTextItems, volumeItems.currentIndex) { dialog, which ->
                volume = volumeItems.values[which]
                updateUI()
                dialog.dismiss()
            }
            .create()

        dialogue.show()
    }

    private fun showSingleChoiceWithConfirmationAlertDialogue() {
        val volumeItems = AvailableVolumeValues.createVolumeValues(volume)
        val volumeTextItems = volumeItems.values
            .map { getString(R.string.volume_description, it) }
            .toTypedArray()

        val dialog = AlertDialog.Builder(this)
            .setTitle("Volume Setup With Confirmation")
            .setCancelable(false)
            .setSingleChoiceItems(volumeTextItems, volumeItems.currentIndex, null)
            .setPositiveButton("Confirm") { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                volume = volumeItems.values[index]
                updateUI()
            }
            .create()
        dialog.show()
    }

    private fun showMultipleChoiceAlertDialogue() {
        val colorItems = resources.getStringArray(R.array.colors)
        val colorComponents = mutableListOf(
            Color.red(this.color),
            Color.green(this.color),
            Color.blue(this.color)
        )
        val checkBoxes = colorComponents
            .map { it > 0 }
            .toBooleanArray()

        val dialog = AlertDialog.Builder(this)
            .setTitle("Choose a color")
            .setMultiChoiceItems(colorItems, checkBoxes) { _, which, isChecked ->
                colorComponents[which] = if (isChecked) 255 else 0
                this.color = Color.rgb(
                    colorComponents[0],
                    colorComponents[1],
                    colorComponents[2],
                )
                updateUI()
            }
            .create()
        dialog.show()
    }

    private fun showMultipleChoiceWithConfirmationDialogue() {
        val colorNames = resources.getStringArray(R.array.colors)
        val colorComponents = mutableListOf(
            Color.red(this.color),
            Color.green(this.color),
            Color.blue(this.color)
        )
        val checkboxes = colorComponents
            .map { it > 0 }
            .toBooleanArray()

        var color : Int = this.color

        val dialog = AlertDialog.Builder(this)
            .setTitle("Choose a Color With Confirmation")
            .setMultiChoiceItems(colorNames, checkboxes) { _, which, isChecked ->
                colorComponents[which] = if (isChecked) 255 else 0
                color = Color.rgb(
                    colorComponents[0],
                    colorComponents[1],
                    colorComponents[2]
                )
            }
            .setPositiveButton("Confirm") { _, _ ->
                this.color = color
                updateUI()
            }
            .create()
        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUI() {
        binding.currentVolumeTextView.text = getString(R.string.current_volume, volume)
        binding.colorView.setBackgroundColor(color)
    }
}