package com.marvel.developer.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marvel.developer.persistence.entities.FavoriteCharacterEntity
import com.marvel.developer.persistence.room.dao.FavoriteCharacterDao

@Database(
    entities = [FavoriteCharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MarvelDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "marvel.db"
    }

    abstract fun favoriteCharacterDao(): FavoriteCharacterDao
}