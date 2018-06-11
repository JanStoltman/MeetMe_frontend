package com.yggdralisk.meetme.ui.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import com.nostra13.universalimageloader.core.ImageLoader
import com.yggdralisk.meetme.R
import kotlinx.android.synthetic.main.activity_events.*
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_image)

        val url = intent.getStringExtra(EventGalleryActivity.PHOTO_URL)

        ImageLoader.getInstance().displayImage(url, photoImageView)
    }


//    class MyPagerAdapter(val urlList: ArrayList<String>, context: Context) : PagerAdapter(){
//        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
//        override fun isViewFromObject(view: View, any: Any): Boolean {
//            return view == any
//        }
//
//        override fun getCount(): Int {
//            return urlList.size
//        }
//
//        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            return super.instantiateItem(container, position)
//        }
//    }
}
