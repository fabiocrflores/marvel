package com.marvel.developer.persistence.room.dao

import androidx.room.*
import com.marvel.developer.persistence.entities.FavoriteCharacterEntity
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface FavoriteCharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(favoriteCharacterEntity: FavoriteCharacterEntity)

    @Query("SELECT * FROM favorite_characters")
    fun loadAll(): Maybe<List<FavoriteCharacterEntity>>

    @Query("SELECT * FROM favorite_characters WHERE favorite_character_id = :id")
    fun loadById(id: Int): Maybe<FavoriteCharacterEntity>

    @Delete
    fun delete(favoriteCharacterEntity: FavoriteCharacterEntity)
}