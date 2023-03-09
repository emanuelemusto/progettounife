package it.unife.projectscorpio

import android.os.Bundle
import android.util.Log.println
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    private lateinit var tvResponse: TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvResponse = findViewById(R.id.textViewConsole)
        val btnGet = findViewById(R.id.getbtn) as Button
        val btnPost = findViewById(R.id.postbtn) as Button

        doAsync {
            sendGet();
        }

        btnGet.setOnClickListener {view ->
            doAsync {
                sendGet();
            }
        }


        btnPost.setOnClickListener {view ->
            doAsync {
                sendPost();
            }
        }
    }

    private fun sendGet() {
        val client = OkHttpClient()
        val url = URL("http://192.168.1.13:9200/sensore")

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()

        val responseBody = response.body!!.string()

        //Response
        println("Response Body: " + responseBody)

        //we could use jackson if we got a JSON
        val mapperAll = ObjectMapper()
        val objData = mapperAll.readTree(responseBody)

        objData.get("data").forEachIndexed { index, jsonNode ->
            println("$index $jsonNode")
        }
        tvResponse.setText("GET Response : " + response.toString())

    }


    fun sendPost() {
/*
        var urlPost = "http://localhost:9200/sensore";
        var msgPost = "Jorgesys was here! :-) !"

        var reqParam = URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(msgPost, "UTF-8")
        val mURL = URL(urlPost)

        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"

            val wr = OutputStreamWriter(getOutputStream());
            wr.write(reqParam);
            wr.flush();

            println("\nSending 'POST' request to URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println("POST Response : $response")
                tvResponse.setText("POST Response : $response")
            }
        }*/
    }
}