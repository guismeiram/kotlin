package br.com.guismeiram.animaldonationapp.componente.addAnimal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.guismeiram.animaldonationapp.R
import br.com.guismeiram.animaldonationapp.infra.database.AppDatabase
import br.com.guismeiram.animaldonationapp.infra.entity.AnimalEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAnimalActivity : AppCompatActivity() {

    private lateinit var editTextNome: EditText
    private lateinit var editTextIdade: EditText
    private lateinit var editTextRaca: EditText
    private lateinit var editTextDescricao: EditText
    private lateinit var editTextFotoUrl: EditText
    private lateinit var buttonSalvar: Button
    private lateinit var database: AppDatabase
    private lateinit var imageViewAnimal: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonAddAnimal: Button
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_animal)

        imageViewAnimal = findViewById(R.id.imageViewAnimal)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonAddAnimal = findViewById(R.id.buttonAddAnimal)

        buttonSelectImage.setOnClickListener {
            openGallery()
        }

        // Inicializar o banco de dados
        database = AppDatabase.getDatabase(this)

        // Inicializar os campos de texto
        editTextNome = findViewById(R.id.editTextNome)
        editTextIdade = findViewById(R.id.editTextIdade)
        editTextRaca = findViewById(R.id.editTextRaca)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        editTextFotoUrl = findViewById(R.id.editTextFotoUrl)

        // Inicializar o botão de salvar
        buttonSalvar = findViewById(R.id.buttonAddAnimal)

        // Configurar o evento de clique do botão "Salvar"
        buttonSalvar.setOnClickListener {
            salvarAnimal()
        }
    }

    private fun salvarAnimal() {
        // Coletar os dados inseridos
        val nome = editTextNome.text.toString()
        val idade = editTextIdade.text.toString().toIntOrNull()
        val raca = editTextRaca.text.toString()
        val descricao = editTextDescricao.text.toString()
        val fotoUrl = editTextFotoUrl.text.toString()

        // Validar os dados
        if (nome.isEmpty() || idade == null || raca.isEmpty() || descricao.isEmpty() || fotoUrl.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Criar uma nova instância de AnimalEntity
        val novoAnimal = AnimalEntity(
            nome = nome,
            idade = idade,
            raca = raca,
            descricao = descricao,
            fotoUrl = fotoUrl
        )

        // Inserir o animal no banco de dados usando coroutines
        CoroutineScope(Dispatchers.IO).launch {
            database.animalDao().insert(novoAnimal)
            runOnUiThread {
                Toast.makeText(this@AddAnimalActivity, "Animal adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                // Voltar à tela principal após salvar
                finish()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageViewAnimal.setImageURI(imageUri)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
}
