import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplication.R
import com.example.aplication.ViewModels.GeoData
import com.example.aplication.ViewModels.MainViewModel
import com.example.aplication.screens.MainScreen
import com.example.aplication.screens.mainFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithTopAppBar(
    drawerState: DrawerState,
    scope: CoroutineScope,
    sharedViewModel: GeoData?,
    context: Context,
    onClick: () -> Unit
) {
    val viewModel = viewModel<MainViewModel>()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name), color = Color.White, fontFamily = mainFont)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorResource(
                        viewModel.mainList.value.backgroundColor
                    )
                ),
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "menu",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "menu",
                            tint = Color.White
                        )
                    }
                },
            )
        },

        ) { padding ->
        MainScreen(padding, sharedViewModel, context)
    }
}