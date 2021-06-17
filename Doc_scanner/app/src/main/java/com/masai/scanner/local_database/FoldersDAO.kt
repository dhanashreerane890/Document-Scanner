package com.masai.scanner.local_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FoldersDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFolder(foldersEntity: FoldersEntity)

    @Query("select*from folders")
    fun getFolders(): LiveData<List<FoldersEntity>>

    @Update
    fun updateFolderName(foldersEntity: FoldersEntity)

    @Delete
    fun deleteFolder(foldersEntity: FoldersEntity)
}