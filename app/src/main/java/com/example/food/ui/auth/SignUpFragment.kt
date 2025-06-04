package com.example.food.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.food.R
import com.example.food.data.UserPreferences
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var userPrefs: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefs = UserPreferences(requireContext())

        // 1. Récupération des TextInputLayouts et EditTexts
        val tilName     = view.findViewById<TextInputLayout>(R.id.tilSignUpName)
        val tilEmail    = view.findViewById<TextInputLayout>(R.id.tilSignUpEmail)
        val tilPassword = view.findViewById<TextInputLayout>(R.id.tilSignUpPassword)
        val tilPhone    = view.findViewById<TextInputLayout>(R.id.tilPhone)
        val tilAddress  = view.findViewById<TextInputLayout>(R.id.tilAddress)

        val etName     = view.findViewById<TextInputEditText>(R.id.etSignUpName)
        val etEmail    = view.findViewById<TextInputEditText>(R.id.etSignUpEmail)
        val etPassword = view.findViewById<TextInputEditText>(R.id.etSignUpPassword)
        val etPhone    = view.findViewById<TextInputEditText>(R.id.etPhone)
        val etAddress  = view.findViewById<TextInputEditText>(R.id.etAddress)

        // 2. Spinner pour le code pays
        val spinnerCountryCode = view.findViewById<Spinner>(R.id.spinnerCountryCode)
        // On crée un ArrayAdapter si besoin (mais souvent, android:entries="@array/country_codes" suffit)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.country_codes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCountryCode.adapter = adapter
        }

        // 3. ChipGroup pour préférences alimentaires
        val chipGroupPreferences = view.findViewById<ChipGroup>(R.id.chipGroupPreferences)

        // 4. Bouton S’inscrire
        val btnSignUp = view.findViewById<MaterialButton>(R.id.btnSignUp)

        // 5. Réinitialiser les erreurs dès que l’utilisateur modifie un champ
        etName.doAfterTextChanged    { tilName.error = null }
        etEmail.doAfterTextChanged   { tilEmail.error = null }
        etPassword.doAfterTextChanged{ tilPassword.error = null }
        etPhone.doAfterTextChanged   { tilPhone.error = null }
        etAddress.doAfterTextChanged { tilAddress.error = null }

        // 6. Au clic sur “S’inscrire”
        btnSignUp.setOnClickListener {
            // 6.1 Lecture et validation du “Nom complet”
            val nameText = etName.text.toString().trim()
            if (nameText.isEmpty()) {
                tilName.error = getString(R.string.error_name_required)
                return@setOnClickListener
            }

            // 6.2 Lecture et validation de l’“Email”
            val emailText = etEmail.text.toString().trim()
            if (emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                tilEmail.error = getString(R.string.error_email_required)
                return@setOnClickListener
            }

            // 6.3 Lecture et validation du “Mot de passe”
            val passText = etPassword.text.toString()
            if (passText.isEmpty()) {
                tilPassword.error = getString(R.string.error_password_required)
                return@setOnClickListener
            }

            // 6.4 Lecture et validation du “Téléphone”
            //    1) Récupérer la chaîne du Spinner (ex. "+33 – France")
            val selectedCountry = spinnerCountryCode.selectedItem.toString()
            //    2) On extrait le code, avant le " –"
            val countryCode = selectedCountry.substringBefore(" –").trim()  // ex. "+33"
            val phoneNumber = etPhone.text.toString().trim()
            if (phoneNumber.isEmpty()) {
                tilPhone.error = getString(R.string.error_phone_required)
                return@setOnClickListener
            }
            //    3) On peut construire la chaîne complète du téléphone
            val fullPhone = countryCode + phoneNumber

            // 6.5 Lecture et validation de l’“Adresse”
            val addressText = etAddress.text.toString().trim()
            if (addressText.isEmpty()) {
                tilAddress.error = getString(R.string.error_address_required)
                return@setOnClickListener
            }

            // 6.6 Lecture des “Préférences alimentaires” (Chips cochées)
            val checkedChipIds: List<Int> = chipGroupPreferences.checkedChipIds
            val preferencesList = mutableListOf<String>()
            for (chipId in checkedChipIds) {
                val chip = chipGroupPreferences.findViewById<Chip>(chipId)
                preferencesList.add(chip.text.toString())
            }
            // Ex. preferencesList = ["Végétarien", "Sans gluten"]

            // 7. Vérification si l’email est déjà utilisé
            val existingUser = userPrefs.getUsers().firstOrNull { it.email == emailText }
            if (existingUser != null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_email_already_registered),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            userPrefs.saveUser(
                nameText,
                emailText,
                passText,
                fullPhone,
                addressText,
                preferencesList
            )

            Toast.makeText(
                requireContext(),
                getString(R.string.msg_registered),
                Toast.LENGTH_SHORT
            ).show()

            // 9. Navigation vers l’écran “Home” (on peut faire popUpTo si besoin)
            findNavController().navigate(R.id.action_signUp_to_home)
        }
    }
}
