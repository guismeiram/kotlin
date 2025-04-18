package br.com.guismeiram.animaldonationapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.guismeiram.animaldonationapp.R
import br.com.guismeiram.animaldonationapp.model.Animal
import com.bumptech.glide.Glide
import android.widget.Filter
import br.com.guismeiram.animaldonationapp.componente.detailsAnimal.DetailAnimalActivity

class AnimalAdapter(private var animalList: MutableList<Animal>) :
    RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>(), android.widget.Filterable {

    private var animalListFull: MutableList<Animal> = ArrayList(animalList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.animal_item, parent, false)
        return AnimalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val currentItem = animalList[position]

        // Configurar os dados do animal nos elementos visuais
        holder.animalName.text = currentItem.nome
        holder.animalBreed.text = currentItem.raca

        // Usando Glide para carregar a imagem
        Glide.with(holder.itemView.context)
            .load(currentItem.fotoUrl)
            .into(holder.animalImage)

        // Configurar clique no item para abrir os detalhes do animal
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailAnimalActivity::class.java)
            intent.putExtra("nome", currentItem.nome)
            intent.putExtra("idade", currentItem.idade)
            intent.putExtra("raca", currentItem.raca)
            intent.putExtra("descricao", currentItem.descricao)
            intent.putExtra("fotoUrl", currentItem.fotoUrl)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = animalList.size

    // ViewHolder que contém os elementos do layout
    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animalImage: ImageView = itemView.findViewById(R.id.animalImageView)
        val animalName: TextView = itemView.findViewById(R.id.animalNameTextView)
        val animalBreed: TextView = itemView.findViewById(R.id.animalBreedTextView)
    }

    override fun getFilter(): Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: MutableList<Animal> = ArrayList()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(animalListFull)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (animal in animalListFull) {
                        if (animal.nome.lowercase().contains(filterPattern)) {
                            filteredList.add(animal)
                        }
                    }
                }
                val results = FilterResults();
                results.values = filteredList
                return results
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                animalList.clear()
                if (results?.values is List<*>) {
                    animalList.addAll((results.values as List<*>).filterIsInstance<Animal>())
                }
                notifyDataSetChanged()
            }
        }
    }




}