package ute.com.shop

import android.app.Activity
import android.content.Context
import kotlinx.android.synthetic.main.activity_nick_name.*

fun Activity.setNickname(nickname: String) {
    getSharedPreferences("shop", Context.MODE_PRIVATE)
        .edit()
        .putString("NICKNAME", nickname)
        .apply()

}

fun Activity.getNickname(): String {
    return  getSharedPreferences("shop", Context.MODE_PRIVATE)
        .getString("NICKNAME","")

}