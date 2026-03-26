package com.vigil.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vigil.app.data.local.converter.RoomTypeConverters
import com.vigil.app.data.local.dao.BlockedAppDao
import com.vigil.app.data.local.dao.CheatLogDao
import com.vigil.app.data.local.dao.ProfileDao
import com.vigil.app.data.local.entity.BlockedAppEntity
import com.vigil.app.data.local.entity.CheatLogEntity
import com.vigil.app.data.local.entity.ProfileEntity

@Database(
    entities = [
        ProfileEntity::class,
        BlockedAppEntity::class,
        CheatLogEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(RoomTypeConverters::class)
abstract class VigilDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun blockedAppDao(): BlockedAppDao
    abstract fun cheatLogDao(): CheatLogDao
}
