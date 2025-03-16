package br.com.guismeiram.animaldonationapp.infra.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.guismeiram.animaldonationapp.infra.dao.AnimalDao
import br.com.guismeiram.animaldonationapp.infra.entity.AnimalEntity


@Database(entities = [AnimalEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animalDao(): AnimalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "animal_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}