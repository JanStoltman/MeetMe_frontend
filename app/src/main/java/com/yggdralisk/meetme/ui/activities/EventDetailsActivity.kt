package com.yggdralisk.meetme.ui.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.yggdralisk.meetme.MyApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.MyCallback
import com.yggdralisk.meetme.api.calls.EventCalls
import com.yggdralisk.meetme.api.calls.ImgurCalls
import com.yggdralisk.meetme.api.calls.UsersCalls
import com.yggdralisk.meetme.api.models.*
import com.yggdralisk.meetme.utility.PhotoEncoder
import com.yggdralisk.meetme.utility.TimestampManager
import com.yggdralisk.meetme.utility.notifications.NotificationHelper
import kotlinx.android.synthetic.main.activity_event_details.*
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EventDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val EVENT_ID = "event_id"
        const val PHOTO_REQUEST = 1
        const val FILE_PROVIDER_AUTHORITY_NAME: String = "com.yggdralisk.meetme.fileprovider"

        var mCurrentPhotoPath: String = ""
    }

    var eventToDisplay: EventModel? = null
    var eventGuests: ArrayList<SimpleUserModel> = arrayListOf()
    var canRateEvent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val eventId = intent.getIntExtra(EVENT_ID, 1) //TODO: Change this shit

        progressBar2.visibility = View.VISIBLE

        callEvent(eventId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId ?: 0) {
        R.id.menuCameraButton -> {
            dispatchTakePhotoIntent()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun createImageFile(): File {
        val imageFileName = "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        )

        mCurrentPhotoPath = image.absolutePath;
        return image;
    }

    private fun dispatchTakePhotoIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null;
            try {
                photoFile = createImageFile();
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            photoFile?.let {
                val photoURI: Uri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY_NAME, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PHOTO_REQUEST);
            }
        }

    }

    private fun callEvent(eventId: Int) {
        EventCalls.getEventById(eventId, object : MyCallback<EventModel>(this) {
            override fun onResponse(call: Call<EventModel>?, response: Response<EventModel>?) {
                super.onResponse(call, response)
                eventToDisplay = response?.body()
                eventToDisplay?.let {
                    if (eventEnded(it)) {
                        checkIfNotRatedYet(eventId)
                    } else {
                        setEvent()
                    }
                }

                progressBar2.visibility = View.GONE
            }
        })
    }

    private fun checkIfNotRatedYet(eventId: Int) {
        progressBar2.visibility = View.VISIBLE
        EventCalls.wasRated(eventId, object : MyCallback<Boolean>(this@EventDetailsActivity) {
            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                super.onResponse(call, response)
                progressBar2.visibility = View.GONE
                response?.body()?.let {
                    canRateEvent = !it
                }
                setEvent()
            }
        })
    }

    private fun eventEnded(event: EventModel): Boolean {
        val timestampManager = TimestampManager(this)
        event.endTime?.let {
            return timestampManager.isTimestampInPast(it)
        }
        return false
    }

    private fun setEvent() {
        populateUI()
        (mapView as SupportMapFragment).getMapAsync(this)

        if (eventToDisplay != null && eventEnded(eventToDisplay!!)) {
            joinButton.visibility = View.INVISIBLE
        } else if (MyApplication.userId == eventToDisplay?.creator ?: true) {
            joinButton.text = getString(R.string.cancel_event)
            joinButton.setBackgroundColor(Color.RED)
            joinButton.setOnClickListener({
                val deleteDialogListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            deleteEvent()
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

                AlertDialog.Builder(this).setMessage("Are you sure?").setPositiveButton("Yes", deleteDialogListener)
                        .setNegativeButton("No", deleteDialogListener)
                        .show()
            })
        } else if (MyApplication.userId in eventToDisplay?.guests ?: listOf()) {
            joinButton.text = getString(R.string.leave_event)
            joinButton.setOnClickListener({
                leaveEvent()
            })
        } else if (eventToDisplay?.guestLimit ?: 4 <= eventToDisplay?.guests?.size ?: 1) {
            joinButton.text = this.getText(R.string.event_full)
        } else {
            joinButton.setOnClickListener({
                joinEvent()
            })
        }

        if (canRateEvent) {
            ratingBar.visibility = View.VISIBLE
            rateButton.visibility = View.VISIBLE
            rateButton.setOnClickListener({
                rateEvent()
            })
        }
    }

    private fun joinEvent() {
        eventToDisplay?.let {
            EventCalls.joinEvent(eventToDisplay?.id!!, object : MyCallback<EventModel>(this) {
                override fun onResponse(call: Call<EventModel>?, response: Response<EventModel>?) {
                    super.onResponse(call, response)
                    if (response?.isSuccessful == true) {
                        //notification
                        NotificationHelper.remindUser(this@EventDetailsActivity, eventToDisplay!!)
                        Toast.makeText(baseContext, "Event joined", Toast.LENGTH_LONG).show()
                        this@EventDetailsActivity.finish()
                    }
                }
            })
        }
    }

    private fun rateEvent() {
        eventToDisplay?.let {
            EventCalls.rateEvent(eventToDisplay?.id!!, ratingBar.rating, object : MyCallback<Float>(this) {
                override fun onResponse(call: Call<Float>?, response: Response<Float>?) {
                    super.onResponse(call, response)
                    rateButton.visibility = View.INVISIBLE
                }
            })
        }
    }

    private fun leaveEvent() {
        eventToDisplay?.let {
            EventCalls.leaveEvent(eventToDisplay?.id!!, object : MyCallback<EventModel>(this) {
                override fun onResponse(call: Call<EventModel>?, response: Response<EventModel>?) {
                    super.onResponse(call, response)
                    if (response?.isSuccessful == true) {
                        Toast.makeText(baseContext, "Event left", Toast.LENGTH_LONG).show()
                        this@EventDetailsActivity.finish()
                    }
                }
            })
        }
    }

    private fun deleteEvent() {
        eventToDisplay?.let {
            EventCalls.deleteEvent(eventToDisplay?.id!!, object : MyCallback<EventModel>(this) {
                override fun onResponse(call: Call<EventModel>?, response: Response<EventModel>?) {
                    super.onResponse(call, response)
                    if (response?.isSuccessful == true) {
                        Toast.makeText(baseContext, "Event deleted", Toast.LENGTH_SHORT).show()
                        this@EventDetailsActivity.finish()
                    }
                }
            })
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.setMinZoomPreference(14f)

        val pos = LatLng(eventToDisplay?.latitude ?: 51.108081, eventToDisplay?.longitude
                ?: 17.065134)//TODO: handle lack of permission
        val marker: MarkerOptions = MarkerOptions()
                .position(pos)

        googleMap?.addMarker(marker)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(pos))
    }

    private fun populateUI() {
        eventName.text = eventToDisplay?.name ?: ""
        locationName.text = eventToDisplay?.locationName ?: ""
        eventDescription.text = eventToDisplay?.description ?: ""
        startTime.text = TimestampManager(this).toDateHourString(eventToDisplay?.startTime ?: 0)
        endTime.text = TimestampManager(this).toDateHourString(eventToDisplay?.endTime ?: 0)
        locationAddress.text = eventToDisplay?.address ?: ""

        fetchCreator()

        val ageRestriction = eventToDisplay?.ageRestriction
        ageRestrictions.text = "min.${ageRestriction?.minAge} max.${ageRestriction?.maxAge}"

        guestsList.adapter = MyAdapter(eventGuests, this)
        fetchGuests()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            processTakenPhoto()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processTakenPhoto() {
        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
        val encoded = PhotoEncoder.encodePhoto(bitmap)

        ImgurCalls.postImage(encoded, object : MyCallback<ImgurPhotoModel>(this) {
            override fun onResponse(call: Call<ImgurPhotoModel>?, response: Response<ImgurPhotoModel>?) {
                super.onResponse(call, response)

                eventToDisplay?.let {
                    EventCalls.addPhoto(eventToDisplay!!.id!!,
                            PhotoModel.fromImgurPhoto(response?.body()),
                            object : MyCallback<EventModel>(this@EventDetailsActivity) {})
                }
            }
        })
    }

    private fun fetchGuests() {
        UsersCalls.getNamesForIds(eventToDisplay?.guests
                ?: listOf(), object : MyCallback<List<SimpleUserModel>>(this) {
            override fun onResponse(call: Call<List<SimpleUserModel>>?, response: Response<List<SimpleUserModel>>?) {
                super.onResponse(call, response)
                eventGuests.addAll(response?.body() ?: listOf())
                (guestsList?.adapter as MyAdapter).notifyDataSetChanged()
            }
        })
    }

    private fun fetchCreator() {
        UsersCalls.getUserById(eventToDisplay?.creator ?: 1, object : MyCallback<UserModel>(this) {
            override fun onResponse(call: Call<UserModel>?, response: Response<UserModel>?) {
                super.onResponse(call, response)
                creatorName.text = "${response?.body()?.name ?: "Error"} ${response?.body()?.surname
                        ?: "Error"}"

                creatorName.setOnClickListener({
                    val intent = Intent(this@EventDetailsActivity, UserProfileActivity::class.java)
                    intent.putExtra(UserProfileActivity.USER_ID, eventToDisplay?.creator ?: 1)
                    this@EventDetailsActivity.startActivity(intent)
                })
            }
        })
    }

    class MyAdapter(private val guests: List<SimpleUserModel>, val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view = convertView
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.guest_list_element, parent, false)
            }

            val currentItem = getItem(position) as SimpleUserModel
            view?.findViewById<TextView>(R.id.userName)?.text = "${currentItem.firstName} ${currentItem.lastName}"
            ImageLoader.getInstance().displayImage(currentItem.photoImage, view?.findViewById<ImageView>(R.id.profilePic))

            view?.setOnClickListener {
                val intent = Intent(context, UserProfileActivity::class.java)
                intent.putExtra(UserProfileActivity.USER_ID, currentItem.id)
                context.startActivity(intent)
            }

            return view
        }

        override fun getItem(position: Int): Any? = guests[position]

        override fun getItemId(position: Int): Long = guests[position].id.toLong()

        override fun getCount(): Int = guests.size
    }
}
