package ute.com.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.raw_bus.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import java.net.URL

class BusActivity : AppCompatActivity(), AnkoLogger {

    var bus: Bus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)

        val busURL =
            "http://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed"

        doAsync {

            val url = URL(busURL)
            val json = url.readText()
            info("json: $json")//Anko Logging

            bus = Gson().fromJson<Bus>(json, Bus::class.java)

            info(bus!!.datas.size)
            bus!!.datas.forEach {
                info("${it.BusID} ${it.RouteID} ${it.Speed}")
            }

            uiThread {
                recycler.layoutManager = LinearLayoutManager(this@BusActivity)
                recycler.setHasFixedSize(true)
                recycler.adapter = BusAdapter()

            }

        }
    }

    inner class BusAdapter() : RecyclerView.Adapter<BusHolder>() {
        //inner Class 可使用外部CLASS參數
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_bus, parent, false)
            return BusHolder(view)
        }

        override fun getItemCount(): Int {
            return bus?.datas!!.size ?: 0
        }

        override fun onBindViewHolder(holder: BusHolder, position: Int) {
            holder.bindBus(bus?.datas!!.get(position))

        }
    }

    inner class BusHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tx_busID: TextView = view.tx_busID
        var tx_ruteID: TextView = view.tx_ruteID
        var tx_speed: TextView = view.tx_speed
        fun bindBus(data: Data) {
            tx_busID.text = data.BusID
            tx_ruteID.text = data.RouteID
            tx_speed.text = data.Speed
        }
    }
}


data class Data(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)

data class Bus(
    val datas: List<Data>
)
