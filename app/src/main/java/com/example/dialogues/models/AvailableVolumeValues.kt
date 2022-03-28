package com.example.dialogues.models

data class AvailableVolumeValues(
    val values: List<Int>,
    val currentIndex: Int
) {
    companion object {
        fun createVolumeValues(currentValue: Int) : AvailableVolumeValues {
            val values = (0..100 step 10)
            val currentIndex = values.indexOf(currentValue)
            return AvailableVolumeValues(values.toList(), currentIndex)
        }
    }
}
