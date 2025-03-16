package br.com.guismeiram.animaldonationapp.infra.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_table")
data class AnimalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val idade: Int,
    val raca: String,
    val fotoUrl: String,
    val descricao: String
)