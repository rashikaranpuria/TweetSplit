package com.rashikaranpuria.tweetsplit.di.module

import android.app.Application
import com.rashikaranpuria.tweetsplit.data.DataManager
import com.rashikaranpuria.tweetsplit.data.IDataManager
import com.rashikaranpuria.tweetsplit.data.db.DbManager
import com.rashikaranpuria.tweetsplit.data.db.IDbManager
import com.rashikaranpuria.tweetsplit.data.db.TweetDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

    @Provides
    @Singleton
    fun provideDataManager(mDataManager: DataManager): IDataManager = mDataManager

    @Provides
    @Singleton
    fun provideTweetDao() = TweetDatabase.getInstance(context = app).tweetDao()

    @Provides
    @Singleton
    fun provideDbManager(mDbManager: DbManager) = mDbManager as IDbManager
}