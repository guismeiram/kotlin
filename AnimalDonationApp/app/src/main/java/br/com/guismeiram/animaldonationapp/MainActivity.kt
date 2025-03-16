package br.com.guismeiram.animaldonationapp

import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.guismeiram.animaldonationapp.adapter.AnimalAdapter
import br.com.guismeiram.animaldonationapp.model.Animal
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var animalAdapter: AnimalAdapter
    private lateinit var animalList: MutableList<Animal>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var addAnimalButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Habilitar persistência offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Inicializar o Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Exemplo de evento personalizado (será registrado quando o usuário abrir a MainActivity)
        logAppOpenEvent()

        // Inicializar o Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("animals")

        // Buscar dados do Firebase
        fetchAnimalsFromFirebase()



        // Inicializar componentes da interface
        recyclerView = findViewById(R.id.recyclerViewAnimals)
        searchView = findViewById(R.id.searchView)
        addAnimalButton = findViewById(R.id.addAnimalButton)

        // Inicializar a lista de animais (posteriormente será carregada do banco de dados)
        animalList = mutableListOf()

        // Inicializar o adapter e conectá-lo ao RecyclerView
        animalAdapter = AnimalAdapter(animalList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = animalAdapter

        // Configurar o SearchView para filtrar a lista
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                animalAdapter.filter.filter(newText)
                return true
            }
        })

        // Evento de clique para adicionar novos animais
        addAnimalButton.setOnClickListener {
            // Chamar uma nova tela para adicionar animais (essa tela será criada posteriormente)
            // startActivity(Intent(this, AddAnimalActivity::class.java))
        }


    }

    fun fetchAnimalsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                animalList.clear()
                for (animalSnapshot in dataSnapshot.children) {
                    val animal = animalSnapshot.getValue(Animal::class.java)
                    animal?.let { animalList.add(it) }
                }
                animalAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Tratar erros de leitura do Firebase
            }
        })
    }

    // Função para registrar um evento personalizado no Firebase Analytics
    fun logAppOpenEvent() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.METHOD, "app_open")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
    }
}