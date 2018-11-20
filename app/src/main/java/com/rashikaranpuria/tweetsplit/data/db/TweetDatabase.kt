package com.rashikaranpuria.tweetsplit.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.rashikaranpuria.tweetsplit.data.db.entity.Tweet

@Database( entities = [Tweet::class], version = 1)
abstract class TweetDatabase: RoomDatabase() {
    abstract fun tweetDao(): TweetDao

    companion object {
        @Volatile private var INSTANCE: TweetDatabase? = null

        fun getInstance(context: Context): TweetDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context,
                    TweetDatabase::class.java, "Tweets.db").build()
    }
}