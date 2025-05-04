package com.example.randomtraveller.core.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [SavedSearch::class],
    version = 2,
    exportSchema = false,
)
abstract class RandomTravellerDatabase : RoomDatabase() {
    abstract fun savedSearchDao(): SavedSearchDao
}

@Entity(tableName = "saved_searches")
data class SavedSearch(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityId: String,
    val airportName: String,
    val iata: String,
    val maxPrice: Int,
    val outboundStartDate: String,
    val outboundEndDate: String,
    val inboundStartDate: String,
    val inboundEndDate: String
)

@Dao
interface SavedSearchDao {

    /**
     * Inserts a search. If a search with the same ID already exists, it replaces it.
     * Consider if you need a different conflict strategy (e.g., IGNORE or ABORT).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(savedSearch: SavedSearch)

    /**
     * Retrieves all saved searches, ordered by ID descending (newest first).
     * Returns a Flow for reactive updates.
     */
    @Query("SELECT * FROM saved_searches ORDER BY id DESC")
    fun getAll(): Flow<List<SavedSearch>>

    /**
     * Deletes a specific saved search.
     */
    @Delete
    suspend fun delete(savedSearch: SavedSearch)

    /**
     * Optional: Deletes a saved search by its ID.
     * Returns the number of rows deleted (should be 0 or 1).
     */
    @Query("DELETE FROM saved_searches WHERE id = :searchId")
    suspend fun deleteById(searchId: Int): Int

    /**
     * Optional: Get a single search by its ID.
     * Returns a Flow for reactive updates.
     */
    @Query("SELECT * FROM saved_searches WHERE id = :searchId")
    fun getById(searchId: Int): Flow<SavedSearch?>

}