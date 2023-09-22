import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplication.R
import com.example.aplication.screens.Items.Drawer
import com.example.aplication.screens.mainFont
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherFeatureWithLocationPermission(context: Context) {
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    if (locationPermissionState.status.isGranted) {
        Drawer(context)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.clear_Sky_Background)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (locationPermissionState.status.shouldShowRationale) {
                // Если пользователь ранее отклонил разрешение, показываем разъяснение
                stringResource(R.string.location_permission_required_2)
            } else {
                // Если это первый раз, когда пользователь попал на экран с погодой,
                // или он отключил запрос на разрешение, показываем общее разъяснение
                stringResource(R.string.location_permission_access_first)
            }
            Text(
                text = textToShow,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                color = Color(0xFFFFFFFF),
                fontSize = 17.sp,
                lineHeight = 20.sp,
                fontFamily = mainFont,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { locationPermissionState.launchPermissionRequest() },
                modifier = Modifier
                    .wrapContentWidth(),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.clear_Sky_Items))
            )
             {
                Text(
                    stringResource(R.string.request_permission_button),
                    fontSize = 20.sp,
                    fontFamily = mainFont,
                )
            }
        }
    }
}
