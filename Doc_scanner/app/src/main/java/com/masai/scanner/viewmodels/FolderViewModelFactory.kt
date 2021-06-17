package com.masai.scanner.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.masai.scanner.repository.FolderRepository

class FolderViewModelFactory(private val repository: FolderRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return FolderViewModel(repository) as T
    }

}