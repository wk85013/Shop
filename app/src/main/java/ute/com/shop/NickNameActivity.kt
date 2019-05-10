package ute.com.shop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaCodec.MetricsConstants.MODE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_nick_name.*

class NickNameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)
        btn_done.setOnClickListener {
            getSharedPreferences("shop", Context.MODE_PRIVATE)
                .edit()
                .putString("NICKNAME", ed_nickname.text.toString())
                .apply()
            setResult(Activity.RESULT_OK)
            finish()

        }
    }
}
