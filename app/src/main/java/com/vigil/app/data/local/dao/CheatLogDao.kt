package com.vigil.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vigil.app.data.local.entity.CheatLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CheatLogDao {
    @Query("SELECT * FROM cheat_logs WHERE profile_id = :profileId ORDER BY attempted_at DESC")
    fun observeCheatLogsForProfile(profileId: String): Flow<List<CheatLogEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cheatLog: CheatLogEntity)
}
