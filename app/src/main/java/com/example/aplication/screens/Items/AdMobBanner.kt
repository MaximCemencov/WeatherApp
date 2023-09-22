package com.example.aplication.screens.Items


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


@Composable
fun AdMobBanner() {
        AndroidView(factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.SMART_BANNER)
                adUnitId = "ca-app-pub-3940256099942544/9214589741"
                val adRequest = AdRequest.Builder().build()
                loadAd(adRequest)
            }
        },
            modifier = Modifier.fillMaxWidth()
        )
}
