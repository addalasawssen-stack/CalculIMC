package com.example.calculimc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Locale
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPoids: EditText
    private lateinit var editTextTaille: EditText
    private lateinit var buttonCalculer: Button
    private lateinit var buttonEffacer: Button
    private lateinit var textViewImc: TextView
    private lateinit var textViewCategorie: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        buttonCalculer = findViewById(R.id.buttonCalculer)
        buttonEffacer = findViewById(R.id.buttonEffacer)
        textViewImc = findViewById(R.id.textViewImc)
        textViewCategorie = findViewById(R.id.textViewCategorie)

        buttonCalculer.setOnClickListener { calculerImc() }
        buttonEffacer.setOnClickListener { effacer() }
    }

    private fun calculerImc() {
        // 1. Récupération des saisies (la virgule est acceptée comme séparateur)
        val poidsTexte = editTextPoids.text.toString().trim().replace(',', '.')
        val tailleTexte = editTextTaille.text.toString().trim().replace(',', '.')

        // 2. Vérification des champs vides
        if (poidsTexte.isEmpty()) {
            editTextPoids.error = getString(R.string.erreur_champ_vide)
            editTextPoids.requestFocus()
            return
        }
        if (tailleTexte.isEmpty()) {
            editTextTaille.error = getString(R.string.erreur_champ_vide)
            editTextTaille.requestFocus()
            return
        }

        // 3. Conversion en nombres décimaux (null si saisie invalide, pas de plantage)
        val poids = poidsTexte.toDoubleOrNull()
        val taille = tailleTexte.toDoubleOrNull()

        if (poids == null || taille == null) {
            Toast.makeText(this, R.string.erreur_valeur_invalide, Toast.LENGTH_SHORT).show()
            return
        }

        // 4. Vérification : valeurs strictement positives
        if (poids <= 0) {
            editTextPoids.error = getString(R.string.erreur_valeur_positive)
            editTextPoids.requestFocus()
            return
        }
        if (taille <= 0) {
            editTextTaille.error = getString(R.string.erreur_valeur_positive)
            editTextTaille.requestFocus()
            return
        }

        // 5 et 6. Calcul et arrondi à deux décimales
        val imc = poids / (taille * taille)
        val imcArrondi = round(imc * 100.0) / 100.0

        // 7. Détermination de la catégorie
        val categorie: Int
        val couleur: Int
        if (imcArrondi < 18.5) {
            categorie = R.string.cat_insuffisance
            couleur = R.color.orange
        } else if (imcArrondi < 25) {
            categorie = R.string.cat_normale
            couleur = R.color.vert
        } else if (imcArrondi < 30) {
            categorie = R.string.cat_surpoids
            couleur = R.color.orange
        } else if (imcArrondi < 35) {
            categorie = R.string.cat_obesite_moderee
            couleur = R.color.rouge
        } else if (imcArrondi < 40) {
            categorie = R.string.cat_obesite_severe
            couleur = R.color.rouge
        } else {
            categorie = R.string.cat_obesite_morbide
            couleur = R.color.rouge_fonce
        }

        // 8. Affichage
        val couleurResolue = ContextCompat.getColor(this, couleur)
        textViewImc.text = getString(
            R.string.resultat_imc,
            String.format(Locale.getDefault(), "%.2f", imcArrondi)
        )
        textViewImc.setTextColor(couleurResolue)
        textViewCategorie.text = getString(categorie)
        textViewCategorie.setTextColor(couleurResolue)
    }

    private fun effacer() {
        editTextPoids.text.clear()
        editTextTaille.text.clear()
        editTextPoids.error = null
        editTextTaille.error = null
        textViewImc.text = ""
        textViewCategorie.text = ""
        editTextPoids.requestFocus()
    }
}