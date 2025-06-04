package com.example.food.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.food.R

class ContactFragment : Fragment(R.layout.fragment_contact) {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSubject: EditText
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupération des vues
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etSubject = view.findViewById(R.id.etSubject)
        etMessage = view.findViewById(R.id.etMessage)
        btnSend = view.findViewById(R.id.btnSend)

        btnSend.setOnClickListener {
            envoyerEmail()
        }
    }

    private fun envoyerEmail() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val subject = etSubject.text.toString().trim()
        val message = etMessage.text.toString().trim()

        // Validation de base
        if (name.isEmpty()) {
            etName.error = "Veuillez entrer votre nom"
            etName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            etEmail.error = "Veuillez entrer votre e-mail"
            etEmail.requestFocus()
            return
        }

        if (!isValidEmail(email)) {
            etEmail.error = "Format d’e-mail invalide"
            etEmail.requestFocus()
            return
        }

        if (subject.isEmpty()) {
            etSubject.error = "Veuillez entrer un sujet"
            etSubject.requestFocus()
            return
        }

        if (message.isEmpty()) {
            etMessage.error = "Veuillez écrire un message"
            etMessage.requestFocus()
            return
        }

        // Construire le corps du message
        val fullMessage = """
            Nom : $name
            E-mail : $email

            Message :
            $message
        """.trimIndent()

        // Créer l’intent d’envoi d’e-mail
        val mailTo = "mailto:contact@votreapp.com" // Remplacez par l’adresse réelle
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(mailTo)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, fullMessage)
        }

        // Vérifier qu’il existe une application d’e-mail pour traiter l’intent
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(
                requireContext(),
                "Aucune application e-mail disponible sur cet appareil",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Fonction utilitaire pour vérifier la validité d’un e-mail
    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}
