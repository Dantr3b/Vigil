package com.vigil.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vigil.app.data.local.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY created_at DESC")
    fun observeProfiles(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: UUID): ProfileEntity?

    @Query("SELECT * FROM profiles WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveProfile(): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(profile: ProfileEntity)

    @Update
    suspend fun update(profile: ProfileEntity)

    @Delete
    suspend fun delete(profile: ProfileEntity)
}
