package com.masai.scanner.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.masai.scanner.local_database.FoldersEntity
import com.masai.scanner.repository.FolderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FolderViewModel(private val repository: FolderRepository) : ViewModel() {

    fun addFolder(foldersEntity: FoldersEntity) {
        repository.addFolder(foldersEntity)
    }

    fun getFolder(): LiveData<List<FoldersEntity>> {
        return repository.getFolder()
    }

    fun updateFolderName(foldersEntity: FoldersEntity) {
        repository.updateFolderName(foldersEntity)
    }

    fun deleteFolder(foldersEntity: FoldersEntity) {
        repository.deleteFolder(foldersEntity)
    }
}