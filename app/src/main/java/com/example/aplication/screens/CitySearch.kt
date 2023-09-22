package com.example.aplication.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplication.R
import com.example.aplication.ViewModels.CitiesModes
import com.example.aplication.ViewModels.SearchCity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearch(
    viewModelView: CitiesModes,
    drawerState: DrawerState,
    scope : CoroutineScope,
    onClick: () -> Unit
) {
    val viewModel = viewModel<SearchCity>()
    val searchText by viewModel.searchText.collectAsState()
    val cities by viewModel.cities.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.enter_city_here)) },
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (isSearching) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(vertical = 20.dp)
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(cities) { city ->
                    Button(
                        onClick = {
                            // здесь добавляем город в память
                            viewModelView.addCity(city)
                            scope.launch { drawerState.open() }

                            onClick()
                        },
                        modifier = Modifier
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(size = 15.dp),
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    )
                    {
                        Text(
                            text = "${city.cityCurrentLanguageName} ${city.cityCountryName}",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterVertically)

                        )
                    }
                }
            }
        }
    }
}


