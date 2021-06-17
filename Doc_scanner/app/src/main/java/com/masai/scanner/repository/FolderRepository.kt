package com.masai.scanner.repository

import androidx.lifecycle.LiveData
import com.masai.scanner.local_database.FoldersDAO
import com.masai.scanner.local_database.FoldersEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FolderRepository(private val foldersDAO: FoldersDAO) {
    fun addFolder(foldersEntity: FoldersEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            foldersDAO.addFolder(foldersEntity)
        }
    }

    fun getFolder(): LiveData<List<FoldersEntity>> {
        return foldersDAO.getFolders()
    }

    fun updateFolderName(foldersEntity: FoldersEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            foldersDAO.updateFolderName(foldersEntity)
        }
    }

    fun deleteFolder(foldersEntity: FoldersEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            foldersDAO.deleteFolder(foldersEntity)
        }
    }
}