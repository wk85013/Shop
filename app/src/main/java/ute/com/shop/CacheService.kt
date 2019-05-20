package ute.com.shop

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.bumptech.glide.Glide
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class CacheService : IntentService("CacheService"), AnkoLogger {
    //IntentService可自動onDestroy,可多重使用，排隊執行
    companion object {
        val ACTION_CACHE_DONE = "action_cache_done"

    }

    override fun onHandleIntent(intent: Intent?) {
        info { "onHandleIntent" }
        val title = intent?.getStringExtra("TITLE")
        val url = intent?.getStringExtra("URL")
        info { "Downloading...$title  $url" }
        Glide.with(this).download(url)
        sendBroadcast(Intent(ACTION_CACHE_DONE))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

/*    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        info { "onStartCommand" }
        return START_STICKY

    }*/

    override fun onCreate() {
        super.onCreate()
        info { "onCreate" }
    }

    override fun onDestroy() {
        super.onDestroy()
        info { "onDestroy" }
    }
}