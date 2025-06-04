package com.example.food.ui.main

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.food.R
import com.example.food.databinding.FragmentProfileBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ProfileFragment : Fragment() {

    // ViewBinding pour accéder directement aux vues
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // État du mode édition (false = lecture seule, true = édition)
    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    /** Appelé juste après que la vue soit créée */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Charger et afficher l’avatar + les données utilisateur
        loadUserInfo()

        // 2. Clic sur “Modifier les coordonnées”
        binding.btnEditInfo.setOnClickListener {
            toggleEditMode(true)
        }

        // 3. Clic sur “Enregistrer”
        binding.btnSaveInfo.setOnClickListener {
            saveUserInfo()
            toggleEditMode(false)
        }

        // 4. Gérer les changements de sélection dans les chips “Préférences alimentaires”
        binding.chipGroupDiet.setOnCheckedStateChangeListener { group, checkedIds ->
            // Ici, checkedIds contient la liste des IDs des Chips cochés
            // Ex. pour stocker en prefs :
            // val selectedLabels = checkedIds.mapNotNull { id -> group.findViewById<Chip>(id)?.text?.toString() }
            // saveDietPreferencesToPrefs(selectedLabels)
        }
    }

    /**
     * Charge l’avatar et les autres informations de l’utilisateur (hard-codées ici pour l’exemple).
     * Remplacez ces valeurs “hardcodées” par la lecture depuis vos SharedPreferences ou votre base de données.
     */
    private fun loadUserInfo() {
        // ───────────────── Exemple de données fictives ─────────────────
        val name = "Amine Souid"
        val email = "aminesouid20@gmail.com"
        val phone = "+33 06 09 49 67 27"
        val address = "16 boulevard, Le Mans 72000"
        val categories = listOf(
            "Cuisine Maghrébine",
            "Italienne",
            "Chinoise",
            "Desserts",
            "Végétarienne"
        )
        val dietPreferences = listOf("Végétarien", "Végan", "Sans gluten")
        val isVegetarian = false // Exemple de pré-sélection
        // ───────────────────────────────────────────────────────────────

        // 0. Charger l’avatar de l’utilisateur
        // 0.a) Si vous avez une image locale dans res/drawable (ex: avatar_user.png) :
        //     binding.ivAvatar.setImageResource(R.drawable.avatar_user)
        //
        // 0.b) Si vous voulez charger depuis une URL avec Glide, décommentez la section ci-dessous
        //     et ajoutez la dépendance Glide dans votre build.gradle (voir le commentaire plus bas).
        //
        // val avatarUrl = "https://monserveur.com/photos/monavatar.jpg"
        // Glide.with(this)
        //     .load(avatarUrl)
        //     .circleCrop()
        //     .placeholder(R.drawable.avatar_placeholder) // optionnel, placeholder local
        //     .error(R.drawable.avatar_placeholder)       // optionnel, image erreur
        //     .into(binding.ivAvatar)

        // Pour l’instant, on va juste afficher une ressource locale.
        // Ajoutez dans res/drawable/ un fichier avatar_user.png ou ic_user_avatar.xml.
        binding.ivAvatar.setImageResource(R.drawable.avatar_user)

        // 1. Informations personnelles
        binding.tvUserName.text = name
        binding.tvUserEmail.text = email
        binding.tvUserPhone.text = phone
        binding.tvUserAddress.text = address

        // 2. Catégories : vider puis ajouter dynamiquement des Chips non-cliquables
        binding.chipGroupCategories.removeAllViews()
        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isClickable = false
                isCheckable = false
                setChipBackgroundColorResource(R.color.teal_200)
                setTextColor(resources.getColor(R.color.black, null))
                layoutParams = ChipGroup.LayoutParams(
                    ChipGroup.LayoutParams.WRAP_CONTENT,
                    ChipGroup.LayoutParams.WRAP_CONTENT
                )
            }
            binding.chipGroupCategories.addView(chip)
        }

        // 3. Préférences alimentaires : on coche ou décoche les Chips selon isVegetarian (seul exemple)
        binding.chipVegetarian.isChecked = isVegetarian
        binding.chipVegan.isChecked = false
        binding.chipGlutenFree.isChecked = false
    }

    /**
     * Alterne entre mode édition (Insertion d’EditText) et mode lecture seule (TextView).
     * Si enable = true → on remplace 4 TextView par 4 EditText.
     * Sinon, on lit la valeur du EditText, on met à jour le TextView correspondant et on supprime l’EditText.
     */
    private fun toggleEditMode(enable: Boolean) {
        isEditing = enable

        // Modifier la visibilité des boutons
        binding.btnEditInfo.visibility = if (enable) View.GONE else View.VISIBLE
        binding.btnSaveInfo.visibility = if (enable) View.VISIBLE else View.GONE

        if (enable) {
            // MODE ÉDITION : cacher chaque TextView et insérer un EditText
            makeEditable(
                viewToReplace = binding.tvUserName,
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            )
            makeEditable(
                viewToReplace = binding.tvUserEmail,
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            )
            makeEditable(
                viewToReplace = binding.tvUserPhone,
                inputType = InputType.TYPE_CLASS_PHONE
            )
            makeEditable(
                viewToReplace = binding.tvUserAddress,
                inputType = InputType.TYPE_CLASS_TEXT
            )
        } else {
            // MODE LECTURE : remplacer chaque EditText par son TextView d’origine
            replaceEditTextByTextView(viewToReplace = binding.tvUserName)
            replaceEditTextByTextView(viewToReplace = binding.tvUserEmail)
            replaceEditTextByTextView(viewToReplace = binding.tvUserPhone)
            replaceEditTextByTextView(viewToReplace = binding.tvUserAddress)
        }
    }

    /**
     * Lecture des valeurs tapées dans les EditText, mise à jour des TextView et suppression des EditText.
     * On peut aussi en profiter pour enregistrer ces infos en SharedPreferences ou en base locale.
     */
    private fun saveUserInfo() {
        // Récupération des EditText par leur tag “et_<idName>”
        val etName = binding.root.findViewWithTag<EditText>("et_${binding.tvUserName.idName()}")
        val etEmail = binding.root.findViewWithTag<EditText>("et_${binding.tvUserEmail.idName()}")
        val etPhone = binding.root.findViewWithTag<EditText>("et_${binding.tvUserPhone.idName()}")
        val etAddress = binding.root.findViewWithTag<EditText>("et_${binding.tvUserAddress.idName()}")

        // Mise à jour des TextView avec la nouvelle valeur ou un placeholder si vide
        etName?.let {
            val newName = it.text.toString().trim()
            binding.tvUserName.text = newName.ifBlank { getString(R.string.hint_no_name) }
        }
        etEmail?.let {
            val newEmail = it.text.toString().trim()
            binding.tvUserEmail.text = newEmail.ifBlank { getString(R.string.hint_no_email) }
        }
        etPhone?.let {
            val newPhone = it.text.toString().trim()
            binding.tvUserPhone.text = newPhone.ifBlank { getString(R.string.hint_no_phone) }
        }
        etAddress?.let {
            val newAddress = it.text.toString().trim()
            binding.tvUserAddress.text = newAddress.ifBlank { getString(R.string.hint_no_address) }
        }

        // TODO : Enregistrer les données modifiées en persistance si nécessaire
        // Ex. :
        // val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        // prefs.edit()
        //     .putString("user_name", binding.tvUserName.text.toString())
        //     .putString("user_email", binding.tvUserEmail.text.toString())
        //     .putString("user_phone", binding.tvUserPhone.text.toString())
        //     .putString("user_address", binding.tvUserAddress.text.toString())
        //     .apply()
    }

    /**
     * Remplace un TextView par un EditText au même index dans le parent (ConstraintLayout) :
     * 1. Récupère l’index du TextView dans son parent
     * 2. Crée un EditText avec les mêmes layoutParams et le même texte
     * 3. Attribue un tag “et_<idName>” pour pouvoir le retrouver ensuite
     * 4. Masque le TextView (visibility = GONE)
     * 5. Insère l’EditText au même index
     */
    private fun makeEditable(viewToReplace: View, inputType: Int) {
        val parent = viewToReplace.parent as ViewGroup
        val index = parent.indexOfChild(viewToReplace)

        val editText = EditText(requireContext()).apply {
            layoutParams = viewToReplace.layoutParams
            setText((viewToReplace as? android.widget.TextView)?.text)
            this.inputType = inputType
            tag = "et_${viewToReplace.idName()}"
            textSize = (viewToReplace as? android.widget.TextView)
                ?.textSize
                ?.div(resources.displayMetrics.scaledDensity) ?: 16f
        }

        viewToReplace.visibility = View.GONE
        parent.addView(editText, index)
    }

    /**
     * Replace un EditText (trouvé par son tag) par le TextView d’origine :
     * 1. Trouve l’EditText par son tag “et_<idName>”
     * 2. Lit le texte tapé, supprime l’EditText
     * 3. Réaffiche le TextView d’origine avec la nouvelle valeur
     */
    private fun replaceEditTextByTextView(viewToReplace: View) {
        val tag = "et_${viewToReplace.idName()}"
        val parent = viewToReplace.parent as ViewGroup

        val editText = parent.findViewWithTag<EditText>(tag)
        if (editText != null) {
            val newText = editText.text.toString().trim()
            val index = parent.indexOfChild(editText)
            parent.removeViewAt(index)

            when (viewToReplace.id) {
                R.id.tvUserName -> {
                    binding.tvUserName.text = newText.ifBlank { getString(R.string.hint_no_name) }
                    binding.tvUserName.visibility = View.VISIBLE
                }
                R.id.tvUserEmail -> {
                    binding.tvUserEmail.text = newText.ifBlank { getString(R.string.hint_no_email) }
                    binding.tvUserEmail.visibility = View.VISIBLE
                }
                R.id.tvUserPhone -> {
                    binding.tvUserPhone.text = newText.ifBlank { getString(R.string.hint_no_phone) }
                    binding.tvUserPhone.visibility = View.VISIBLE
                }
                R.id.tvUserAddress -> {
                    binding.tvUserAddress.text = newText.ifBlank { getString(R.string.hint_no_address) }
                    binding.tvUserAddress.visibility = View.VISIBLE
                }
            }
        }
    }

    /** Retourne le nom de la ressource pour la vue (ex. “tvUserEmail”) */
    private fun View.idName(): String {
        return resources.getResourceEntryName(this.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
