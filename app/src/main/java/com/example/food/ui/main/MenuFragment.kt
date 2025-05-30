package com.example.food.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food.R
import com.example.food.data.Dish

class MenuFragment : Fragment(R.layout.fragment_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Référence au RecyclerView dans le layout
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvDishes)

        // 2) Création d'une liste “dummy” de plats
        val sampleDishes = listOf(
            Dish(
                id = 1,
                name = "Pizza Margherita",
                description = "Pâte croustillante, coulis de tomate, mozzarella, basilic frais.",
                price = 9.99,
                imageRes = R.drawable.ic_launcher_foreground // remplacez par votre drawable
            ),
            Dish(
                id = 2,
                name = "Couscous Royal",
                description = "Semoule moelleuse, légumes variés, viande d'agneau et merguez.",
                price = 14.50,
                imageRes = R.drawable.ic_launcher_foreground
            ),
            Dish(
                id = 3,
                name = "Sushi Assortiment",
                description = "Assortiment de nigiri, maki et sashimi frais.",
                price = 12.75,
                imageRes = R.drawable.ic_launcher_foreground
            ),
            Dish(
                id = 4,
                name = "Burger Maison",
                description = "Steak haché, cheddar, bacon, sauce barbecue, frites maison.",
                price = 11.20,
                imageRes = R.drawable.ic_launcher_foreground
            ),
            Dish(
                id = 5,
                name = "Salade César",
                description = "Laitue romaine, poulet grillé, croûtons, sauce César crémeuse.",
                price = 8.30,
                imageRes = R.drawable.ic_launcher_foreground
            )
            // Ajoutez d’autres plats si besoin…
        )

        // 3) Configurer le RecyclerView : orientation verticale
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        // 4) Créer et assigner l’adapter
        val adapter = DishAdapter(sampleDishes) { dish ->
            // Callback au clic sur un plat ; par exemple, afficher un toast
            // Toast.makeText(requireContext(), "Clic sur ${dish.name}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }
}
