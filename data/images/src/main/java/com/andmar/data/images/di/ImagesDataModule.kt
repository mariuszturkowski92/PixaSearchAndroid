package com.andmar.data.images.di

import com.andmar.data.images.ImagesLocalDataSource
import com.andmar.data.images.ImagesLocalDataSourceImpl
import com.andmar.data.images.ImagesRepository
import com.andmar.data.images.ImagesRepositoryImpl
import com.andmar.data.images.ImagesRepositoryImpl_Factory
import com.andmar.data.images.network.ImagesRemoteDataSource
import com.andmar.data.images.network.ImagesRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindImagesRepository(repository: ImagesRepositoryImpl): ImagesRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Singleton
    @Binds
    abstract fun bindLocalDataSource(dataSource: ImagesLocalDataSourceImpl): ImagesLocalDataSource

}
//
//    @Singleton
//    @Provides
//    fun provideDataBase(@ApplicationContext context: Context): ToDoDatabase {
//        return Room.databaseBuilder(
//            context.applicationContext,
//            ToDoDatabase::class.java,
//            "Tasks.db"
//        ).build()
//    }
//
//    @Provides
//    fun provideTaskDao(database: ToDoDatabase): TaskDao = database.taskDao()
//}