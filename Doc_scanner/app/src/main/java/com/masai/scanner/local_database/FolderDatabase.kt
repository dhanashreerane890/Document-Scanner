package com.masai.scanner.local_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FoldersEntity::class], version = 1)
abstract class FolderDatabase : RoomDatabase() {
    abstract fun getFolderDao(): FoldersDAO

    companion object {
        private var INSTANCE: FolderDatabase? = null

        fun getDatabase(context: Context): FolderDatabase {
            if (INSTANCE == null) {
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    FolderDatabase::class.java,
                    "folder_db"
                )
                builder.fallbackToDestructiveMigration()
                INSTANCE = builder.build()
                return INSTANCE!!
            } else {
                return INSTANCE!!
            }
        }
    }
}