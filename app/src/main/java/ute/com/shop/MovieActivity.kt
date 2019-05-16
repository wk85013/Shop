package ute.com.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.content_main.recycler
import kotlinx.android.synthetic.main.raw_movie.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.URL

class MovieActivity : AppCompatActivity(), AnkoLogger {

    var movies: List<Movie>? = null
    val retrofit = Retrofit.Builder().baseUrl("http://api.myjson.com/bins/")
        .addConverterFactory(GsonConverterFactory.create())//使用GsonConverterFactory把資料轉成GSON
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        doAsync {
            //            val url = URL("https://api.myjson.com/bins/ccope")
//            val json = url.readText()
//            bus = Gson().fromJson<List<Movie>>(
//                json,
//                object : TypeToken<List<Movie>>() {}.type
//            )
            val movieService = retrofit.create(MovieService::class.java)
            movies = movieService.listMovies()
                .execute()
                .body()
            movies?.forEach {
                info("${it.Title}  ${it.imdbRating}")
            }

            uiThread {
                recycler.layoutManager = LinearLayoutManager(this@MovieActivity)
                recycler.setHasFixedSize(true)
                recycler.adapter = MovieAdapter()

            }

        }
    }

    inner class MovieAdapter() : RecyclerView.Adapter<MovieHolder>() {
        //inner Class 可使用外部CLASS參數
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_movie, parent, false)
            return MovieHolder(view)
        }

        override fun getItemCount(): Int {
            return movies?.size ?: 0
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            holder.bindMovie(movies?.get(position)!!)

            holder.itemView.setOnClickListener { view ->


            }
        }


    }

    inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tx_movieTitle: TextView = view.tx_movieTitle
        var tx_imdb: TextView = view.tx_imdb
        var tx_moviedirector: TextView = view.tx_moviedirector
        var image_movieposter: ImageView = view.image_movieposter
        fun bindMovie(movie: Movie) {
            tx_movieTitle.text = movie.Title
            tx_imdb.text = movie.imdbRating
            tx_moviedirector.text = movie.Director
            Glide.with(this@MovieActivity)//使用Glide套件取得照片
                .load(movie.Poster)
                .override(300)
                .into(image_movieposter)
        }

    }
}

data class Movie(
    val Actors: String,
    val Awards: String,
    val ComingSoon: Boolean,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String,
    val totalSeasons: String
)

interface MovieService {
    @GET ("ccope")
    fun listMovies(): Call<List<Movie>>
}