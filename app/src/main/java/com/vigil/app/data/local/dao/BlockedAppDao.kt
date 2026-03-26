package com.vigil.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vigil.app.data.local.entity.BlockedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedAppDao {
    @Query("SELECT * FROM blocked_apps WHERE profile_id = :profileId ORDER BY package_name ASC")
    fun observeBlockedAppsForProfile(profileId: String): Flow<List<BlockedAppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blockedApp: BlockedAppEntity)

    @Query("DELETE FROM blocked_apps WHERE profile_id = :profileId AND package_name = :packageName")
    suspend fun deleteByProfileAndPackageName(profileId: String, packageName: String)

    @Query("DELETE FROM blocked_apps WHERE profile_id = :profileId")
    suspend fun deleteAllForProfile(profileId: String)
}
