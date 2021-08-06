package com.marvel.developer.main.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.main.R
import com.marvel.developer.main.home.paging.CharacterComparator
import com.squareup.picasso.Picasso

class CharacterAdapter(
    private val clickAction: (Character) -> Unit,
    private val favoriteClickAction: (position: Int, character: Character) -> Unit
) : PagingDataAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val view = inflate.inflate(R.layout.item_character, parent, false)

        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character: Character = getItem(position)!!
        holder.bind(character, clickAction, favoriteClickAction)
    }

    fun updateFavorite(character: Character) {
        snapshot().indexOf(character).also { position ->
            if (position >= 0) {
                snapshot()[position]?.isFavorite = false
                notifyItemChanged(position)
            }
        }
    }

    inner class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageCharacter: AppCompatImageView = itemView.findViewById(R.id.image_character)
        private val textName: AppCompatTextView = itemView.findViewById(R.id.text_name)
        private val textDescription: AppCompatTextView = itemView.findViewById(
            R.id.text_description
        )
        private val imageFavorite: AppCompatImageView = itemView.findViewById(R.id.image_favorite)

        fun bind(
            row: Character,
            clickAction: (Character) -> Unit,
            favoriteClickAction: (Int, Character) -> Unit
        ) {
            row.run {
                Picasso.get().load(imageUrl).into(imageCharacter)
                textName.text = name
                textDescription.text = description
                imageFavorite.setImageResource(
                    if (isFavorite) {
                        R.drawable.ic_selected_favorite_red
                    } else {
                        R.drawable.ic_select_favorite_white
                    }
                )

                imageFavorite.setOnClickListener {
                    favoriteClickAction(layoutPosition, row)
                }

                itemView.setOnClickListener {
                    clickAction(row)
                }
            }
        }
    }
}