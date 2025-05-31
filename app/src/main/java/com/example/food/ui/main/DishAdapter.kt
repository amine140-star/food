package com.example.food.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.food.R
import com.example.food.data.Dish

class DishAdapter(
    private val dishes: List<Dish>,
    private val onAddToCart: ((Dish) -> Unit)? = null
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDishImage: ImageView = itemView.findViewById(R.id.ivDishImage)
        private val tvDishName: TextView = itemView.findViewById(R.id.tvDishName)
        private val tvDishDescription: TextView = itemView.findViewById(R.id.tvDishDescription)
        private val tvDishPrice: TextView = itemView.findViewById(R.id.tvDishPrice)
        private val btnAddToCart: ImageButton = itemView.findViewById(R.id.btnAddToCart)

        fun bind(dish: Dish) {
            // 1. Afficher l’image
            ivDishImage.setImageResource(dish.imageRes)

            // 2. Afficher le nom, la description et le prix
            tvDishName.text = dish.name
            tvDishDescription.text = dish.description
            tvDishPrice.text = String.format("%.2f $", dish.price)

            // 3. Gérer le clic sur “Ajouter au panier”
            btnAddToCart.setOnClickListener {
                onAddToCart?.invoke(dish)
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
