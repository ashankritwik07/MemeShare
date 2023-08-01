package com.example.memeshareingapp

import android.app.DownloadManager
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.common.api.Response
import com.google.androidgamesdk.gametextinput.Listener
import org.json.JSONException


class MainActivity : AppCompatActivity() {

    var currentImageurl: String? = null

    private lateinit var memeimageView: ImageView
    private lateinit var progressbar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        memeimageView = findViewById(R.id.memeimageView)
       progressbar = findViewById(R.id.progressbar)

        loadmeme()
    }

    private fun loadmeme() {
        progressbar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Handle the response here
                try {
                    currentImageurl = response.getString("url")
                    // Do something with the "url" value obtained from the response
                    Log.d("Success Request", "URL: $url")
                    Glide.with(this).load(currentImageurl).listener(object:RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressbar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressbar.visibility = View.GONE
                            return false
                        }

                    }).into(memeimageView)
                } catch (e: JSONException) {
                    // Handle JSON parsing exception if needed
                    Log.e("Error", "Error parsing JSON: ${e.localizedMessage}")
                }
            },
            { error ->
                // Handle error here
                Log.e("Error", "Volley error: ${error.localizedMessage}")
            }
        )


// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

    }



    fun nextmeme(view: View) {
        loadmeme()

    }
    fun sharememe(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey Checkout this col meme I got from Reddit $currentImageurl")
        val chooser = Intent.createChooser(intent,"share this using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View){
        loadmeme()
    }

}