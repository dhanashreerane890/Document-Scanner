package com.masai.scanner.local_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
class FoldersEntity(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "count") var count: Int,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}