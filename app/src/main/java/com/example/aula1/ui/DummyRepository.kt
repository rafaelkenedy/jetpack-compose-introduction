package com.example.aula1.ui

import com.example.aula1.model.Transaction
import kotlin.random.Random


object DummyRepository {
    private var _transactions = mutableListOf<Transaction>()

    val transactions get() = _transactions.toList()

    fun add(transaction: Transaction) {
        _transactions.add(transaction)
    }

    fun deleteTransaction(uuid: String) {
        _transactions.removeIf {
            uuid == it.uuid
        }
    }

    fun updateTransaction(transaction: Transaction) {
        deleteTransaction(transaction.uuid)
        _transactions.add(transaction)
    }

    fun clearTransactions() {
        _transactions.clear()
    }

    fun findTransaction(uuid: String): Transaction {
        return _transactions.firstOrNull { it.uuid == uuid } ?: Transaction()
    }

    fun getRandomFinanceTip(): String {
        val financeTips = listOf(
            "Save at least 10% of your income every month.",
            "Avoid impulse purchases. Always think twice before making a big purchase.",
            "Invest in your future. Consider retirement and other long-term expenses.",
            "Maintain an emergency fund. You never know when you'll need it.",
            "Be mindful about your spending. Keep a budget and stick to it.",
            "Pay off your debts as soon as possible. Interest on debt can add up quickly.",
            "Invest in financial education. The more you know, the better you can manage your money.",
            "Consider long-term investments. They usually provide more stable returns.",
            "Avoid using credit cards for purchases you can't afford outright.",
            "Diversify your investments. Don't put all your eggs in one basket."
        )
        return financeTips.random()
    }
}
