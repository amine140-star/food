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

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var userPrefs: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefs = UserPreferences(requireContext())

        val tilEmail    = view.findViewById<TextInputLayout>(R.id.tilEmail)
        val tilPassword = view.findViewById<TextInputLayout>(R.id.tilPassword)
        val etEmail     = view.findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword  = view.findViewById<TextInputEditText>(R.id.etPassword)
        val btnSignIn   = view.findViewById<MaterialButton>(R.id.btnSignIn)
        val tvGoSignUp  = view.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.tvGoSignUp)

        // Réinitialise l’erreur quand l’utilisateur modifie les champs
        etEmail.doAfterTextChanged    { tilEmail.error = null }
        etPassword.doAfterTextChanged { tilPassword.error = null }

        btnSignIn.setOnClickListener {
            val emailText = etEmail.text.toString().trim()
            val passText  = etPassword.text.toString()

            var isValid = true
            if (emailText.isEmpty()) {
                tilEmail.error = getString(R.string.error_email_required)
                isValid = false
            }
            if (passText.isEmpty()) {
                tilPassword.error = getString(R.string.error_password_required)
                isValid = false
            }
            if (!isValid) return@setOnClickListener

            // Authentifie l’utilisateur avec email + mot de passe
            val user = userPrefs.authenticate(emailText, passText)
            if (user != null) {
                // OK → naviguer vers HomeFragment
                findNavController().navigate(R.id.action_signIn_to_home)
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_incorrect_credentials), Toast.LENGTH_SHORT).show()
            }
        }

        tvGoSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_signUp)
        }
    }
}
