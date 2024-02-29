package com.andmar.search.di


import androidx.paging.PagingConfig
import com.andmar.data.images.MAX_PAGE_SIZE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PagingConfigProvider {

    @Provides
    @Singleton
    fun providePagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = MAX_PAGE_SIZE,
            enablePlaceholders = true,
        )
    }
}
