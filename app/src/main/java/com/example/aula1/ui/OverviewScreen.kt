package com.example.aula1.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula1.model.Transaction
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(viewModel: OverviewViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp),
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        text = "Welcome back, \nRafael Kenedy",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.ClearAll,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.addTransaction(
                    Transaction(
                        category = "Entertainment",
                        value = BigDecimal.valueOf(3)
                    )
                )
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }
    ) {


        Column(modifier = Modifier.padding(it)) {
            AdviceToogle(advice = uiState.advice)
            Text(
                text = "Transactions",
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp
                ),
                style = MaterialTheme.typography.headlineSmall
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp
                ),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 16.dp
                ),
                state = listState
            ) {
                items(uiState.transactions) { transaction ->
                    TransactionCard(
                        uuid = transaction.uuid,
                        value = transaction.value,
                        category = transaction.category,
                        date = transaction.date.formatDate()
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showButton,
            //enter = expandHorizontally(),
            enter = fadeIn(),
            //exit = shrinkHorizontally(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.extraLarge
                        ),
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowCircleUp,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

        }
    }
}

@Composable
fun AdviceToogle(advice: String) {
    var isVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isVisible = !isVisible}
                .padding(16.dp)
        ) {
            Text(
                text = "Advice",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineSmall
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
        }
        AnimatedVisibility(visible = isVisible) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                text = advice
            )
        }
    }

}