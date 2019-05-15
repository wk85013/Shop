package ute.com.shop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaCodec.MetricsConstants.MODE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_nick_name.*

class NickNameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)
        btn_done.setOnClickListener {
            setNickname(ed_nickname.text.toString())//使用Extensinos，讓父類別加功能
/*            getSharedPreferences("shop", Context.MODE_PRIVATE)
                .edit()
                .putString("NICKNAME", ed_nickname.text.toString())
                .apply()*/
            //save Firebase Realtime DataBase
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            FirebaseDatabase.getInstance().getReference("users")
                .child(uid)
                .child("nickname")
                .setValue(ed_nickname.text.toString())
            setResult(Activity.RESULT_OK)
            finish()

        }
    }
}
