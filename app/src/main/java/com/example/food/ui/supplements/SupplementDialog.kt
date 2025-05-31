package com.example.food.ui.supplements

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.food.R
import com.example.food.data.Supplement

/**
 * DialogFragment pour afficher une liste de suppléments à l'utilisateur.
 * @param allSupplements : liste complète des suppléments possibles pour ce plat
 * @param alreadySelected : liste des suppléments déjà sélectionnés (pour édition, si besoin)
 * @param onConfirmed : callback lorsque l'utilisateur clique sur “Ajouter”,
 *                      renvoyant la liste des suppléments cochés.
 */
class SupplementDialog(
    private val allSupplements: List<Supplement>,
    private val alreadySelected: List<Supplement> = emptyList(),
    private val onConfirmed: (selected: List<Supplement>) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Builder pour construire l'AlertDialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.supplements_title)

        // Infalte le layout custom
        val inflater = LayoutInflater.from(requireContext())
        val root = inflater.inflate(R.layout.dialog_supplements, null)

        // Container dans lequel on ajoutera dynamiquement les CheckBox
        val container = root.findViewById<ViewGroup>(R.id.containerSupplements)

        // Crée une map pour accéder aux CheckBox plus tard
        val checkBoxMap = mutableMapOf<CheckBox, Supplement>()

        // Pour chaque supplément disponible, on crée une CheckBox
        for (supp in allSupplements) {
            val cb = CheckBox(requireContext()).apply {
                text = "${supp.name} (+ ${"%.2f".format(supp.price)} $)"
                isChecked = alreadySelected.any { it.id == supp.id }
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            // Ajoute la CheckBox dans le container
            container.addView(cb)
            checkBoxMap[cb] = supp
        }

        // Bouton Valider / Annuler
        val btnConfirm = root.findViewById<com.google.android.material.button.MaterialButton>(
            R.id.btnSupplementsConfirm
        )
        val btnCancel = root.findViewById<com.google.android.material.button.MaterialButton>(
            R.id.btnSupplementsCancel
        )

        // Gérer le clic “Valider” : collecte les suppléments cochés
        btnConfirm.setOnClickListener {
            val selected = checkBoxMap.filter { it.key.isChecked }.values.toList()
            onConfirmed(selected)
            dismiss()
        }

        // Gérer le clic “Annuler”
        btnCancel.setOnClickListener {
            dismiss()
        }

        builder.setView(root)
        return builder.create()
    }
}
