package com.example.food.ui.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food.MainActivity
import com.example.food.R
import com.example.food.data.CartManager
import com.google.android.material.button.MaterialButton

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var rvCart: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnConfirm: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCart = view.findViewById(R.id.rvCartItems)
        tvTotal = view.findViewById(R.id.tvTotalPrice)
        btnConfirm = view.findViewById(R.id.btnConfirmOrder)

        // Configurer le RecyclerView
        rvCart.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        refreshCartList()

        // Clic sur “Valider la commande”
        btnConfirm.setOnClickListener {
            val total = CartManager.getTotalPrice()
            if (total > 0) {
                // Ici, vous pouvez envoyer la commande à votre back‐end, puis :
                Toast.makeText(requireContext(), "Commande validée (Total: ${"%.2f".format(total)} $)", Toast.LENGTH_LONG).show()
                // Vidage du panier
                CartManager.clearCart()
                // Rafraîchir l’affichage
                refreshCartList()
                // Mettre à jour le badge en vue de HomeFragment (panier=0)
                (activity as? MainActivity)?.updateCartBadge(0)
            } else {
                Toast.makeText(requireContext(), "Le panier est vide", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Recharge la liste et le total à l’écran.
     * Appelé au démarrage et chaque fois qu’on modifie le panier.
     */
    private fun refreshCartList() {
        val items = CartManager.getItems()
        rvCart.adapter = CartAdapter(items)

        val totalPrice = CartManager.getTotalPrice()
        tvTotal.text = getString(R.string.total_format, totalPrice)
    }
}
