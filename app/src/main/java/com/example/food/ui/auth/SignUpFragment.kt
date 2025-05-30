package com.example.food.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.food.R
import com.example.food.data.UserPreferences
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var userPrefs: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefs = UserPreferences(requireContext())

        val tilName        = view.findViewById<TextInputLayout>(R.id.tilSignUpName)
        val tilEmail       = view.findViewById<TextInputLayout>(R.id.tilSignUpEmail)
        val tilPassword    = view.findViewById<TextInputLayout>(R.id.tilSignUpPassword)
        val etName         = view.findViewById<TextInputEditText>(R.id.etSignUpName)
        val etEmail        = view.findViewById<TextInputEditText>(R.id.etSignUpEmail)
        val etPassword     = view.findViewById<TextInputEditText>(R.id.etSignUpPassword)
        val btnSignUp      = view.findViewById<MaterialButton>(R.id.btnSignUp)

        // Nettoie les erreurs au fur et à mesure de la saisie
        etName.doAfterTextChanged    { tilName.error = null }
        etEmail.doAfterTextChanged   { tilEmail.error = null }
        etPassword.doAfterTextChanged{ tilPassword.error = null }

        btnSignUp.setOnClickListener {
            val nameText     = etName.text.toString().trim()
            val emailText    = etEmail.text.toString().trim()
            val passText     = etPassword.text.toString()

            // Validation simple
            var isValid = true
            if (nameText.isEmpty()) {
                tilName.error = getString(R.string.error_name_required)
                isValid = false
            }
            if (emailText.isEmpty()) {
                tilEmail.error = getString(R.string.error_email_required)
                isValid = false
            }
            if (passText.isEmpty()) {
                tilPassword.error = getString(R.string.error_password_required)
                isValid = false
            }
            if (!isValid) return@setOnClickListener

            // Vérifier si l’email existe déjà
            val existingUser = userPrefs.getUsers().firstOrNull { it.email == emailText }
            if (existingUser != null) {
                Toast.makeText(requireContext(), getString(R.string.error_email_already_registered), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Sauvegarde du nouvel utilisateur
            userPrefs.saveUser(nameText, emailText, passText)
            Toast.makeText(requireContext(), getString(R.string.msg_registered), Toast.LENGTH_SHORT).show()

            // Navigue vers l’écran Home (popUpTo élimine SignIn/SignUp de la backstack)
            findNavController().navigate(R.id.action_signUp_to_home)
        }
    }
}
