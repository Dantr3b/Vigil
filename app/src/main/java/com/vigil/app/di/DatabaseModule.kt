package com.vigil.app.di

import android.content.Context
import androidx.room.Room
import com.vigil.app.data.local.VigilDatabase
import com.vigil.app.data.local.dao.BlockedAppDao
import com.vigil.app.data.local.dao.CheatLogDao
import com.vigil.app.data.local.dao.ProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideVigilDatabase(
        @ApplicationContext context: Context,
    ): VigilDatabase = Room.databaseBuilder(
        context,
        VigilDatabase::class.java,
        "vigil.db",
    ).build()

    @Provides
    fun provideProfileDao(database: VigilDatabase): ProfileDao = database.profileDao()

    @Provides
    fun provideBlockedAppDao(database: VigilDatabase): BlockedAppDao = database.blockedAppDao()

    @Provides
    fun provideCheatLogDao(database: VigilDatabase): CheatLogDao = database.cheatLogDao()
}
