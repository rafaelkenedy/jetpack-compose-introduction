package com.example.aula1.ui


import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aula1.model.Transaction
import com.example.aula1.network.AiService
import com.example.aula1.network.OpenIAPrompt
import com.example.aula1.network.RetrofitModule
import com.example.aula1.ui.DummyRepository.getRandomFinanceTip
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class OverviewViewModel(
    private val repository: DummyRepository = DummyRepository,
    private val aiService: AiService = RetrofitModule.provideAiService(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val filter = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        val prompt = "Give me a small personal finance advice"
        viewModelScope.launch(ioDispatcher) {
            val response = aiService.completions(OpenIAPrompt(prompt))
            if (response.isSuccessful) {
                response.body()?.choices?.first()?.text?.let { advice ->
                    _uiState.value = uiState.value.copy(
                        advice = advice.trim()
                    )
                }
            } else {
                _uiState.value = uiState.value.copy(
                    advice = getRandomFinanceTip()
                )
            }
        }
    }

//    init {
//        val name = "Rafael Kenedy"
//        viewModelScope.launch {
//            var letter = 1
//            name.forEach { _ ->
//                delay(1000)
//                _uiState.value = UiState(userName = name.substring(0, letter))
//                letter++
//
//            }
//        }
//    }

    fun addTransaction(transaction: Transaction) {
        repository.add(transaction)
        updateUiState()
    }

    fun clearTransaction() {
        repository.clearTransactions()
        updateUiState()
    }

    fun deleteTransaction(uuid: String) {
        repository.deleteTransaction(uuid)
        updateUiState()
    }

    fun findTransaction(category: String) {
        filter.value = category
        updateUiState()
    }

    fun clearFilter() {
        filter.value = null
        updateUiState()
    }

    private fun updateUiState() {
        val transactionListSaved = repository.transactions
        val transactions = if (filter.value != null) {
            transactionListSaved.filter {
                it.category == filter.value
            }
        } else {
            transactionListSaved
        }

        _uiState.value = uiState.value.copy(
            transactions = transactions,
            total = transactionListSaved.sumOf { it.value }
        )
    }

    data class UiState(
        val advice: String = "",
        val userName: String = "",
        val transactions: List<Transaction> = emptyList(),
        val total: BigDecimal = transactions.sumOf { it.value }
    )


}

