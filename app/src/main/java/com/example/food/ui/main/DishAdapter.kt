package com.example.food.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.food.R
import com.example.food.data.Dish

/**
 * Adapter pour afficher une liste de [Dish] dans un RecyclerView.
 */
class DishAdapter(
    private val dishes: List<Dish>,
    private val onItemClick: ((Dish) -> Unit)? = null
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    /** ViewHolder qui lie un [item_dish.xml] */
    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDishImage: ImageView = itemView.findViewById(R.id.ivDishImage)
        private val tvDishName: TextView = itemView.findViewById(R.id.tvDishName)
        private val tvDishDescription: TextView = itemView.findViewById(R.id.tvDishDescription)
        private val tvDishPrice: TextView = itemView.findViewById(R.id.tvDishPrice)

        fun bind(dish: Dish) {
            ivDishImage.setImageResource(dish.imageRes)
            tvDishName.text = dish.name
            tvDishDescription.text = dish.description
            // Formatage du prix en 2 décimales + symbole “$”
            tvDishPrice.text = String.format("%.2f $", dish.price)

            // Si on veut gérer un clic sur l’item
            itemView.setOnClickListener {
                onItemClick?.invoke(dish)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        holder.bind(dishes[position])
    }

    override fun getItemCount(): Int = dishes.size
}
