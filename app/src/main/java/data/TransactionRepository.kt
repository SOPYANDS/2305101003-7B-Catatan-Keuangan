
package com.example.catatankeuanganharianberbasisandroid.data

import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao
) {

    // Menambahkan transaksi
    suspend fun insert(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    // Mengambil semua transaksi
    fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    // Mengambil transaksi berdasarkan ID
    suspend fun getById(id: Int): TransactionEntity? {
        return transactionDao.getTransactionById(id)
    }

    // Mengubah transaksi
    suspend fun update(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    // Menghapus transaksi
    suspend fun delete(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    // Total pemasukan
    fun getTotalIncome(): Flow<Long> {
        return transactionDao.getTotalIncome()
    }

    // Total pengeluaran
    fun getTotalExpense(): Flow<Long> {
        return transactionDao.getTotalExpense()
    }
    suspend fun deleteTukuPurel() {
        transactionDao.deleteTukuPurel()
    }
}
