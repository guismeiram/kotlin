package br.com.guismeiram.animaldonationapp.infra.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.guismeiram.animaldonationapp.infra.entity.AnimalEntity

@Dao
interface AnimalDao {
    @Insert
    suspend fun insert(animal: AnimalEntity)

    @Query("SELECT * FROM animal_table")
    suspend fun getAllAnimals(): List<AnimalEntity>

    @Query("DELETE FROM animal_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM animal_table WHERE (:ageRange IS NULL OR idade BETWEEN :minAge AND :maxAge) AND (:breed IS NULL OR raca LIKE :breed)")
    suspend fun getFilteredAnimals(ageRange: IntRange?, breed: String?): List<AnimalEntity>

    @Query("UPDATE animal_table SET isFavorite = :isFavorite WHERE id = :animalId")
    suspend fun updateFavoriteStatus(animalId: Int, isFavorite: Boolean)
}
