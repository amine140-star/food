package com.example.food.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food.MainActivity
import com.example.food.R
import com.example.food.data.CartManager
import com.example.food.data.Dish
import com.example.food.data.Supplement
import com.example.food.ui.supplements.SupplementDialog

class MenuFragment : Fragment(R.layout.fragment_menu) {

    // Liste globable de suppléments (même choix pour chaque plat, ici)
    private val globalSupplements = listOf(
        Supplement(id = 1, name = "Fromage supplémentaire", price = 1.50),
        Supplement(id = 2, name = "Sauce piquante", price = 0.75),
        Supplement(id = 3, name = "Jambon extra", price = 2.00),
        Supplement(id = 4, name = "Oeuf au plat", price = 1.00)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Référence au RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvDishes)

        // 2) Liste de plats factices
        val sampleDishes = listOf(
            Dish(1, "Pizza Margherita",
                "Pâte croustillante, coulis de tomate, mozzarella, basilic frais.",
                9.99, R.drawable.ic_pizza),
            Dish(2, "Couscous Royal",
                "Semoule moelleuse, légumes variés, viande d'agneau et merguez.",
                14.50, R.drawable.ic_couscous),
            Dish(3, "Sushi Assortiment",
                "Assortiment de nigiri, maki et sashimi frais.",
                12.75, R.drawable.ic_sushi),
            Dish(4, "Burger Maison",
                "Steak haché, cheddar, bacon, sauce barbecue, frites maison.",
                11.20, R.drawable.ic_burger),
            Dish(5, "Salade César",
                "Laitue romaine, poulet grillé, croûtons, sauce César crémeuse.",
                8.30, R.drawable.ic_salad)
        )

        // 3) Configurer le RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        // 4) Créer et assigner l’adapter en passant le callback d’ajout
        val adapter = DishAdapter(sampleDishes) { dish ->
            // Ouvrir le dialog de sélection des suppléments
            val dialog = SupplementDialog(
                allSupplements = globalSupplements,
                alreadySelected = emptyList() // ici on part de zéro
            ) { selectedSupplements ->
                // Callback appelé quand l’utilisateur valide la sélection
                CartManager.addItem(dish, selectedSupplements)

                // Mise à jour du badge sur l'icône Panier
                updateCartBadge()

                // Toast pour confirmer l’ajout
                Toast.makeText(
                    requireContext(),
                    "Ajouté : ${dish.name} (+${selectedSupplements.size} supp.)",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.show(parentFragmentManager, "SupplementDialog")
        }
        recyclerView.adapter = adapter

        // 5) Initialiser le badge au démarrage
        updateCartBadge()
    }


    private fun updateCartBadge() {
        (activity as? MainActivity)?.let { mainAct ->
            mainAct.updateCartBadge(CartManager.getTotalQuantity())
        }
    }
}
