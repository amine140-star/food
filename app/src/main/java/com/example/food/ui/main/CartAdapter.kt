package com.example.food.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.food.MainActivity
import com.example.food.R
import com.example.food.data.CartItem
import com.example.food.data.CartManager

/**
 * Adapter pour afficher la liste de CartItem dans le Panier.
 */
class CartAdapter(
    private val items: List<CartItem>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvCartDishName)
        private val tvSupplements: TextView = itemView.findViewById(R.id.tvCartSupplements)
        private val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemoveItem)
        private val btnDec: ImageButton = itemView.findViewById(R.id.btnDecreaseQty)
        private val tvQty: TextView = itemView.findViewById(R.id.tvCartQuantity)
        private val btnInc: ImageButton = itemView.findViewById(R.id.btnIncreaseQty)
        private val tvSubtotal: TextView = itemView.findViewById(R.id.tvCartItemSubtotal)

        fun bind(cartItem: CartItem) {
            // 1. Nom du plat
            tvName.text = cartItem.dish.name

            // 2. Liste des suppléments (concaténation)
            if (cartItem.supplements.isNotEmpty()) {
                val suppText = cartItem.supplements.joinToString(", ") { "+ ${it.name}" }
                tvSupplements.visibility = View.VISIBLE
                tvSupplements.text = suppText
            } else {
                tvSupplements.visibility = View.GONE
            }

            // 3. Quantité
            tvQty.text = cartItem.quantity.toString()

            // 4. Sous‐total pour cet item
            tvSubtotal.text = String.format("%.2f $", cartItem.subTotal())

            // 5. Clic sur “–”
            btnDec.setOnClickListener {
                CartManager.updateQuantity(cartItem, cartItem.quantity - 1)
                // Rafraîchir tout le RecyclerView (simplification)
                notifyDataSetChanged()
                // Mettre à jour badge et total (émettre un callback ?)
                (itemView.context as? MainActivity)?.updateCartBadge(CartManager.getTotalQuantity())
            }

            // 6. Clic sur “+”
            btnInc.setOnClickListener {
                CartManager.updateQuantity(cartItem, cartItem.quantity + 1)
                notifyDataSetChanged()
                (itemView.context as? MainActivity)?.updateCartBadge(CartManager.getTotalQuantity())
            }

            // 7. Clic sur “Supprimer”
            btnRemove.setOnClickListener {
                CartManager.removeItem(cartItem)
                notifyDataSetChanged()
                (itemView.context as? MainActivity)?.updateCartBadge(CartManager.getTotalQuantity())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
