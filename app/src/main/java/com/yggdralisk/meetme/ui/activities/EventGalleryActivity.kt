package com.yggdralisk.meetme.ui.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.nostra13.universalimageloader.core.ImageLoader
import com.yggdralisk.meetme.R

class EventGalleryActivity : AppCompatActivity() {
    companion object {
        val PHOTO_URLS = "PHOTO_URLS"
        val COLUMNS_NUMBER = 2
    }

    var recyclerView : RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var urlList : ArrayList<String>? = null
    var adapter : MyGalleryAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_gallery)
        urlList = intent.getStringArrayListExtra(PHOTO_URLS)

        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        recyclerView = findViewById(R.id.galleryRecyclerView)
        layoutManager = GridLayoutManager(this, COLUMNS_NUMBER)
        recyclerView?.layoutManager = layoutManager
        adapter = MyGalleryAdapter(this, urlList)
        recyclerView?.adapter = adapter
    }


    class MyGalleryAdapter(val context: Context, val urlList: ArrayList<String>?) : RecyclerView.Adapter<MyGalleryAdapter.ViewHolder>() {

        override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context).inflate(
                    R.layout.photo_recycler_item,
                    viewGroup,
                    false
            )
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return urlList?.size ?: 0
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            urlList?.let {
                loadImage(it[position], viewHolder.imageView)
            }
        }


        class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
            val imageView = view.findViewById<ImageView>(R.id.imageView2)
        }


        fun loadImage(url: String, imageView: ImageView){
            ImageLoader.getInstance().displayImage(url, imageView)
        }
    }


}
