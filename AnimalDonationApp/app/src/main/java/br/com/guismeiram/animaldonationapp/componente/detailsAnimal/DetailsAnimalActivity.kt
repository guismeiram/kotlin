package br.com.guismeiram.animaldonationapp.componente.detailsAnimal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.guismeiram.animaldonationapp.R
import br.com.guismeiram.animaldonationapp.infra.database.AppDatabase
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailAnimalActivity : AppCompatActivity() {

    private lateinit var imageViewAnimalDetail: ImageView
    private lateinit var textViewNomeDetail: TextView
    private lateinit var textViewIdadeDetail: TextView
    private lateinit var textViewRacaDetail: TextView
    private lateinit var textViewDescricaoDetail: TextView
    private lateinit var buttonContact: Button
    private lateinit var imageViewFavorite: ImageView
    private var isFavorite: Boolean = false






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_animal)

        // Inicializar Firebase Analytics
        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Registrar um evento de visualização de tela no Firebase Analytics
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "DetailAnimalActivity")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)



        // Inicializar os componentes da interface
        imageViewAnimalDetail = findViewById(R.id.imageViewAnimalDetail)
        textViewNomeDetail = findViewById(R.id.textViewNomeDetail)
        textViewIdadeDetail = findViewById(R.id.textViewIdadeDetail)
        textViewRacaDetail = findViewById(R.id.textViewRacaDetail)
        textViewDescricaoDetail = findViewById(R.id.textViewDescricaoDetail)
        buttonContact = findViewById(R.id.buttonContact)
        imageViewFavorite = findViewById(R.id.imageViewFavorite)

        // Verifique se o animal já está marcado como favorito
        isFavorite = intent.getBooleanExtra("isFavorite", false)
        updateFavoriteIcon()

        // Gerencie o clique no ícone de favorito
        imageViewFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon()

            // Atualizar o status de favorito no banco de dados
            CoroutineScope(Dispatchers.IO).launch {
                val animalDao = AppDatabase.getDatabase(this@DetailAnimalActivity).animalDao()
                animalDao.updateFavoriteStatus(intent.getIntExtra("animalId", 0), isFavorite)
            }
        }

        // Obter os dados passados pela intent
        val nome = intent.getStringExtra("nome")
        val idade = intent.getIntExtra("idade", 0)
        val raca = intent.getStringExtra("raca")
        val descricao = intent.getStringExtra("descricao")
        val fotoUrl = intent.getStringExtra("fotoUrl")

        // Exibir os dados na interface
        textViewNomeDetail.text = nome
        textViewIdadeDetail.text = "Idade: $idade anos"
        textViewRacaDetail.text = "Raça: $raca"
        textViewDescricaoDetail.text = descricao

        // Usar Glide para carregar a imagem do animal
        Glide.with(this).load(fotoUrl).into(imageViewAnimalDetail)

        // Configurar o botão de contato para abrir o e-mail do doador (apenas como exemplo)
        buttonContact.setOnClickListener {
            // Suponha que temos um e-mail do doador fixo para demonstrar
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // Somente e-mails
                putExtra(Intent.EXTRA_EMAIL, arrayOf("doador@exemplo.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Interesse em adotar o $nome")
                putExtra(Intent.EXTRA_TEXT, "Olá, estou interessado em adotar o $nome.")
            }
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            }
        }


    }

    private fun updateFavoriteIcon() {
        if (isFavorite) {
            imageViewFavorite.setImageResource(R.drawable.ic_favorite_filled) // Ícone de favorito preenchido
        } else {
            imageViewFavorite.setImageResource(R.drawable.ic_favorite_border) // Ícone de favorito vazio
        }
    }


}