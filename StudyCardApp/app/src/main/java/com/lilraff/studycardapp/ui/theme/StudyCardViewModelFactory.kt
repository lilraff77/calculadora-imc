package com.lilraff.studycardapp.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lilraff.studycardapp.repository.StudyCardRepository

class StudyCardViewModelFactory(private val repository: StudyCardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudyCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudyCardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}