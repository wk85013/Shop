package ute.com.shop

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.raw_function.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.net.URL

class MainActivity : AppCompatActivity() {
    val TAG = "see"
    private val RC_NICKNAME: Int = 300
    private val RC_SIGHUP: Int = 200
    var signUp = false
    val auth = FirebaseAuth.getInstance()
    val functions = listOf<String>(
        "Camera",
        "Invite friend",
        "parking",
        "Movie",
        "Bus",
        "download coupons",
        "news",
        "Maps"
    )

    var cacheService: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

/*        if (!signUp) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGHUP)
        }*/

        auth.addAuthStateListener { auth ->
            authChanged(auth)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //spinner
        val colors = arrayOf("Red", "Green", "Blue")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i(TAG, ": ${colors[position]}")
            }
        }

        //recyclerView

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = FunctionAdapter()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(CacheService.ACTION_CACHE_DONE)
        registerReceiver(broadcastReceiver, filter)
    }

    inner class FunctionAdapter() : RecyclerView.Adapter<FunctionHolder>() {
        //inner Class 可使用外部CLASS參數
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_function, parent, false)
            val holder = FunctionHolder(view)
            return holder
        }

        override fun getItemCount(): Int {
            return functions.size
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.name.text = functions.get(position)
            holder.itemView.setOnClickListener { view ->
                functionClicked(holder, position)

            }
        }


    }

    private fun functionClicked(holder: FunctionHolder, position: Int) {
        Log.i(TAG, "position: ${position}")
        when (position) {
            1 -> startActivity(Intent(this, ContactActivity::class.java))
            2 -> startActivity(Intent(this, ParkintActivity::class.java))
            3 -> startActivity(Intent(this, MovieActivity::class.java))
            4 -> startActivity(Intent(this, BusActivity::class.java))
            6 -> startActivity(Intent(this, NewsActivity::class.java))


        }
    }

    class FunctionHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.tx_name
    }

    private fun authChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGHUP)
        } else {
            Log.d(TAG, "authChanged: ${auth.currentUser?.uid}")

        }
    }

    override fun onResume() {
        super.onResume()
//        tx_nickname.text = getNickname();//使用Extensinos，讓父類別加功能
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(auth.currentUser!!.uid)
            .child("nickname")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                //匿名類別
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapShot: DataSnapshot) {
                    tx_nickname.text = dataSnapShot.value.toString()
                }


            })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGHUP) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, NickNameActivity::class.java)
                startActivityForResult(intent, RC_NICKNAME)
            }
        } else if (requestCode == RC_NICKNAME) {

        }

    }

    override fun onStop() {
        super.onStop()
//        stopService(cacheService)
        unregisterReceiver(broadcastReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.equals(CacheService.ACTION_CACHE_DONE)) {
                toast("MainActivity cache informed")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_cache -> {
                doAsync {
                    //                    cacheService = Intent(this, CacheService::class.java)
/*
                startService(cacheService)
                startService(Intent(this, CacheService::class.java))//多重使用，排隊執行
                startService(Intent(this, CacheService::class.java))//多重使用，排隊執行
*/

                    val url = URL("https://api.myjson.com/bins/ccope")
                    val json = url.readText()
                    val movies = Gson().fromJson<List<Movie>>(
                        json,
                        object : TypeToken<List<Movie>>() {}.type
                    )

                    val movie = movies.get(0)

/*
                val intent = Intent(this, CacheService::class.java)
                intent.putExtra("TITLE", movie.Title)
                intent.putExtra("URL", movie.Poster)
                startService(intent)//多重使用，排隊執行
*/
                    startService(
                        intentFor<CacheService>(//簡化intent putExtra
                            "TITLE" to movie.Title,
                            "URL" to movie.Poster
                        )
                    )//多重使用，排隊執行


                }



                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
