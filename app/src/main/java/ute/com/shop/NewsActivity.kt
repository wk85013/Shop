package ute.com.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, NewsFragment.instance)
        fragmentTransaction.commit()


    }
}
