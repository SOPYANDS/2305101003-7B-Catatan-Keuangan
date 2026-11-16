
package com.example.catatankeuanganharianberbasisandroid.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val type: String,

    val amount: Long,

    val category: String,

    val date: String,

    val note: String
)
