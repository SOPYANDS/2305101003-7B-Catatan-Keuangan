
package com.example.catatankeuanganharianberbasisandroid.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(
        transaction: TransactionEntity
    )

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Update
    suspend fun updateTransaction(
        transaction: TransactionEntity
    )

    @Delete
    suspend fun deleteTransaction(
        transaction: TransactionEntity
    )

    // Hapus khusus transaksi tuku purel
    @Query("""
        DELETE FROM transactions
        WHERE type = 'Pengeluaran'
        AND amount = 35000
        AND category = 'Belanja'
        AND date = '11/11/2026'
        AND note = 'tuku purel'
    """)
    suspend fun deleteTukuPurel()

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'Pemasukan'
    """)
    fun getTotalIncome(): Flow<Long>

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'Pengeluaran'
    """)
    fun getTotalExpense(): Flow<Long>
}
