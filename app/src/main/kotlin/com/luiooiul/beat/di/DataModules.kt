package com.luiooiul.beat.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.luiooiul.beat.data.repo.DefaultSettingRepository
import com.luiooiul.beat.data.repo.SettingRepository
import com.luiooiul.beat.data.source.SettingDataSource
import com.luiooiul.beat.data.source.local.SettingLocalDataSource
import com.luiooiul.beat.data.store.SettingPreferences
import com.luiooiul.beat.data.store.SettingPrefsSerializer
import com.luiooiul.beat.util.SETTING_PREFERENCES_FILE
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindSettingRepository(
        settingRepository: DefaultSettingRepository
    ): SettingRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    fun bindSettingDataSource(
        settingDataSource: SettingLocalDataSource
    ): SettingDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideSettingPrefsDataStore(
        @ApplicationContext context: Context,
        settingPrefsSerializer: SettingPrefsSerializer
    ): DataStore<SettingPreferences> = DataStoreFactory.create(settingPrefsSerializer) {
        context.dataStoreFile(SETTING_PREFERENCES_FILE)
    }
}