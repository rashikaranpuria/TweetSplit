package com.rashikaranpuria.tweetsplit.data.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Tweet (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int,

    @ColumnInfo
    val text: String?

)