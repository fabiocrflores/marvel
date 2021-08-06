package com.marvel.developer.main.home.paging

import androidx.recyclerview.widget.DiffUtil
import com.marvel.developer.domain.models.character.Character

object CharacterComparator : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        // Id is unique.
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}