package br.com.guismeiram.animaldonationapp.infra.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import br.com.guismeiram.animaldonationapp.MainActivity
import br.com.guismeiram.animaldonationapp.R

class FilterDialogFragment : DialogFragment() {

    private lateinit var editTextMinAge: EditText
    private lateinit var editTextMaxAge: EditText
    private lateinit var editTextBreed: EditText
    private lateinit var buttonApply: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_filters, container, false)
        editTextMinAge = view.findViewById(R.id.editTextMinAge)
        editTextMaxAge = view.findViewById(R.id.editTextMaxAge)
        editTextBreed = view.findViewById(R.id.editTextBreed)
        buttonApply = view.findViewById(R.id.buttonApply)

        buttonApply.setOnClickListener {
            val minAge = editTextMinAge.text.toString().toIntOrNull()
            val maxAge = editTextMaxAge.text.toString().toIntOrNull()
            val breed = editTextBreed.text.toString()

            val ageRange = if (minAge != null && maxAge != null) IntRange(minAge, maxAge) else null
            (activity as MainActivity).applyFilters(ageRange, if (breed.isNotEmpty()) breed else null)
            dismiss()
        }
        return view
    }
}