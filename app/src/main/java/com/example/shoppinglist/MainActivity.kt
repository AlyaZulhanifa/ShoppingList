package com.example.shoppinglist

import ItemInput
import SearchInput
import com.example.shoppinglist.components.AnimatedShoppingListItem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingListApp()
                }
            }
        }
    }
}

@Composable
fun ShoppingListApp() {
    var newItemText by rememberSaveable { mutableStateOf("") }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val shoppingItems = remember { mutableStateListOf<String>() }

    val filteredItems by remember(searchQuery, shoppingItems) {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                shoppingItems
            } else {
                shoppingItems.filter { it.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(horizontal = 16.dp)
    ) {
        Title()

        ItemInput(
            text = newItemText,
            onTextChange = { newItemText = it },
            onAddItem = {
                if (newItemText.isNotBlank()) {
                    shoppingItems.add(newItemText)
                    newItemText = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchInput(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ShoppingList(items = filteredItems)
    }
}

@Composable
fun Title() {
    Text(
        text = "Shopping List",
        style = MaterialTheme.typography.headlineMedium, // lebih besar dari titleLarge
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}


@Composable
fun ItemInput(text: String, onTextChange: (String)->Unit, onAddItem: ()->Unit) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text("Add new item") },
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onAddItem) {
            Text("+ Add")
        }
    }
}

@Composable
fun SearchInput(query: String, onQueryChange: (String)->Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search items") },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ShoppingList(items: List<String>) {
    LazyColumn(
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it }) { item ->
            AnimatedShoppingListItem(item = item)
        }
    }
}
