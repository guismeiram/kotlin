package br.com.guismeiram.animaldonationapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.guismeiram.animaldonationapp.adapter.AnimalAdapter
import br.com.guismeiram.animaldonationapp.model.Animal
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.initialize
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var animalAdapter: AnimalAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var addAnimalButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var allAnimals: MutableList<Animal> = mutableListOf() // Lista completa do Firebase
    private var filteredAnimals: MutableList<Animal> = mutableListOf() // Lista filtrada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializeFirebase()
        setupViews()
        setupRecyclerView()
        setupListeners()

        fetchAnimalsFromFirebase()
    }

    //private fun initializeFirebase() {
        //Firebase.initialize(this)
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)
       // firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        //database = FirebaseDatabase.getInstance().getReference("animals")
        //logAppOpenEvent()
   // }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerViewAnimals)
        searchView = findViewById(R.id.searchView)
        addAnimalButton = findViewById(R.id.addAnimalButton)
    }

    private fun setupRecyclerView() {
        animalAdapter = AnimalAdapter(filteredAnimals)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = animalAdapter
    }

    private fun setupListeners() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterAnimals(newText)
                return true
            }
        })

        addAnimalButton.setOnClickListener {
            // Implement your add animal activity launch here
            // startActivity(Intent(this, AddAnimalActivity::class.java))
        }
    }

    private fun fetchAnimalsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                allAnimals.clear()
                for (animalSnapshot in dataSnapshot.children) {
                    val animal = animalSnapshot.getValue(Animal::class.java)
                    animal?.let { allAnimals.add(it) }
                }
                filteredAnimals.clear()
                filteredAnimals.addAll(allAnimals)
                animalAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error loading data: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterAnimals(query: String?) {
        filteredAnimals.clear()
        filteredAnimals.addAll(
            if (query.isNullOrBlank()) {
                allAnimals
            } else {
                allAnimals.filter {
                    it.nome.contains(query, ignoreCase = true) ||
                            it.raca.contains(query, ignoreCase = true)
                }
            }
        )
        animalAdapter.notifyDataSetChanged()
    }

    fun applyFilters(ageRange: IntRange?, breed: String?) {
        filteredAnimals.clear()
        filteredAnimals.addAll(
            allAnimals.filter { animal ->
                val ageMatch = ageRange?.contains(animal.idade) ?: true
                val breedMatch = breed?.let { animal.raca.contains(it, ignoreCase = true) } ?: true
                ageMatch && breedMatch
            }
        )
        animalAdapter.notifyDataSetChanged()
    }

    private fun logAppOpenEvent() {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, "app_open")
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
    }
}