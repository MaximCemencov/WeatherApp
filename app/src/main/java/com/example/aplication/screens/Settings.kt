package com.example.aplication.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(context: Context, onClick: () -> Unit) {
    var switch1State by rememberSaveable { mutableStateOf(loadSwitchState(context, "Change_decimal")) }
    var switch2State by rememberSaveable { mutableStateOf(loadSwitchState(context, "Change_fahrenheit")) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.settings), color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorResource(id = R.color.clear_Sky_Background)
                ),
                navigationIcon = {
                    IconButton(onClick = onClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                SwitchWithLabel(stringResource(R.string.change_fahrenheit), checked = switch2State) { isChecked ->
                    // Обновляем состояние и сохраняем его в SharedPreferences
                    switch2State = isChecked
                    saveSwitchState(context, "Change_fahrenheit", isChecked)
                }
                SwitchWithLabel(stringResource(R.string.change_decimal), checked = switch1State) { isChecked ->
                    // Обновляем состояние и сохраняем его в SharedPreferences
                    switch1State = isChecked
                    saveSwitchState(context, "Change_decimal", isChecked)
                }
            }
        }
    )
}



@Composable
fun SwitchWithLabel(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val colors = SwitchDefaults.colors(
        checkedThumbColor = Color(0xFF3FB5FF),
        checkedTrackColor = Color(0xFF3FB5FF),
        uncheckedThumbColor = Color.LightGray,
        uncheckedTrackColor = Color.LightGray
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier
                .weight(1f)
                .padding(start = 18.dp),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                fontFamily = mainFont,
            ),
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = colors)
    }
}


private fun saveSwitchState(context: Context, key: String, isChecked: Boolean) {
    val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    with(sharedPrefs.edit()) {
        putBoolean(key, isChecked)
        apply()
    }
}

fun loadSwitchState(context: Context, key: String): Boolean {
    val sharedPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return sharedPrefs.getBoolean(key, false)
}
