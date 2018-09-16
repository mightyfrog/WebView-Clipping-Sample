package org.mightyfrog.android.webclipping

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            clip()
        }

        with(webView) {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.setGeolocationEnabled(false)
            settings.useWideViewPort = true
            settings.builtInZoomControls = true
            settings.setSupportZoom(true)
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            loadUrl("https://www.android.com/")
        }
    }

    private fun clip() {
        activityCircle.visibility = View.VISIBLE
        launch(UI) {
            withContext(DefaultDispatcher) {
                val file = File(Environment.getExternalStorageDirectory(), "test.png")
//                val webViewHeight = (webView.height * resources.displayMetrics.density) // can't render the offscreen part any more :(
                val bitmap = Bitmap.createBitmap(webView.width, webView.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                webView.draw(canvas)
                FileOutputStream(file).use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, it)
                }
            }
            activityCircle.visibility = View.GONE
        }
    }
}
