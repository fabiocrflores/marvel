package com.marvel.developer.main.favorites.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marvel.developer.domain.models.character.Character
import com.marvel.developer.main.databinding.ItemCharacterBinding

class FavoriteAdapter(
    private val presentation: MutableList<Character>,
    private val clickAction: (Character) -> Unit,
    private val favoriteClickAction: (position: Int, character: Character) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val itemBinding = ItemCharacterBinding.inflate(inflate, parent, false)

        return FavoriteViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = presentation.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(presentation[position])
    }

    inner class FavoriteViewHolder(
        private val itemBinding: ItemCharacterBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(row: Character) {
            itemBinding.run {
                presentation = row
                executePendingBindings()

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