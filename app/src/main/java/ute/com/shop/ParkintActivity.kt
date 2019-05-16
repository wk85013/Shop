package ute.com.shop

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_parkint.*
import org.jetbrains.anko.*
import java.net.URL

class ParkintActivity : AppCompatActivity(), AnkoLogger {
    //使用Anko做LOG
    val TAG = ParkintActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parkint)

        val parkingURL =
            "http://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=f4cc0b12-86ac-40f9-8745-885bddc18f79&rid=0daad6e6-0632-44f5-bd25-5e1de1e9146f"
//        Thread(Runnable {
//
//            val json = url.readText()
//            Log.i(TAG, "json: $json");
//
//        }).start()

//        ParkingTask().execute(parkingURL)//多執行續

        //anko
        doAsync {

            val url = URL(parkingURL)
            val json = url.readText()
            info("json: $json")//Anko Logging

            uiThread {
                //                Toast.makeText(it, "got itttttt", Toast.LENGTH_SHORT).show();
                toast("got itxxxxxxxxxx")
                tx_info.text = json
                alert("got it", "alert") {
                    okButton {
                        parseGson(json)

                    }

                }.show()
            }
        }
    }

    private fun parseGson(json: String) {
        val parking = Gson().fromJson<Parking>(json, Parking::class.java)
        info(parking.parkingLots.size)
        parking.parkingLots.forEach {
            info("${it.areaId} ${it.areaName} ${it.parkName} ${it.totalSpace}")
        }
    }

    inner class ParkingTask : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String {
            val url = URL(params[0])
            val json = url.readText()
            Log.i(TAG, "json: $json");

            return json

        }

        override fun onProgressUpdate(vararg values: String?) {//執行中
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {//執行後
            super.onPostExecute(result)
            Toast.makeText(this@ParkintActivity, "got it", Toast.LENGTH_SHORT).show();
            tx_info.text = result
        }

        override fun onPreExecute() {//執行前
            super.onPreExecute()
        }
    }
}

/*
{
    "parkingLots" :
        [
            {
                "areaId" : "1",
                "areaName" : "桃園區",
                "parkName" : "府前地下停車場",
                "totalSpace" : 344,
                "surplusSpace" : "39",
                "payGuide" : "停車費率:30 元/小時。停車時數未滿一小時者，以一小時計算。逾一小時者，其超過之不滿一小時部分，如不逾三十分鐘者，以半小時計算；如逾三十分鐘者，仍以一小時計算收費。",
                "introduction" : "桃園市政府管轄之停車場",
                "address" : "桃園區縣府路一號",
                "wgsX" : 121.3011,
                "wgsY" : 24.9934,
                "parkId" : "P-TY-001"
            }
      ]
}

* */


data class Parking(
    val parkingLots: List<ParkingLot>
)

data class ParkingLot(
    val address: String,
    val areaId: String,
    val areaName: String,
    val introduction: String,
    val parkId: String,
    val parkName: String,
    val payGuide: String,
    val surplusSpace: String,
    val totalSpace: Int,
    val wgsX: Double,
    val wgsY: Double
)

