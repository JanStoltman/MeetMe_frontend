package com.yggdralisk.meetme.ui.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.yggdralisk.meetme.MyApplication
import com.yggdralisk.meetme.R
import com.yggdralisk.meetme.api.models.AgeRestriction
import com.yggdralisk.meetme.api.models.EventModel
import com.yggdralisk.meetme.api.models.EventType
import com.yggdralisk.meetme.utility.TimestampManager
import kotlinx.android.synthetic.main.activity_add_event.*
import java.util.*


class AddEventActivity : AppCompatActivity() {
    companion object {
        val PLACE_PICKER_REQUEST = 1

        private var choosenPlace: Place? = null
        var chosenMinute: String = ""
        var chosenHour: String = ""
        var chosenYear: String = ""
        var chosenMonth: String = ""
        var chosenDay: String = ""

        var chosenMinuteEnd: String = ""
        var chosenHourEnd: String = ""
        var chosenYearEnd: String = ""
        var chosenMonthEnd: String = ""
        var chosenDayEnd: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        googleMapsButton.setOnClickListener({
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        })

        timeButton.setOnClickListener({
            val newFragment = TimePickerFragment()
            newFragment.show(supportFragmentManager, "timePicker")
        })

        calendarButton.setOnClickListener({
            val newFragment = DatePickerFragment()
            newFragment.show(supportFragmentManager, "datePicker")
        })

        timeButtonEnd.setOnClickListener({
            val bundle = Bundle()
            bundle.putBoolean("is_end", true)
            val newFragment = TimePickerFragment()
            newFragment.arguments = bundle
            newFragment.show(supportFragmentManager, "timePicker")
        })

        calendarButtonEnd.setOnClickListener({
            val bundle = Bundle()
            bundle.putBoolean("is_end", true)
            val newFragment = DatePickerFragment()
            newFragment.arguments = bundle
            newFragment.show(supportFragmentManager, "datePicker")
        })

        saveButton.setOnClickListener({
            //TODO: Validate data
            postEvent()
        })
    }

    private fun postEvent() {
        val ageRestriction = AgeRestriction(maxAge = maxAge.text.toString().toInt(), minAge = minAge.text.toString().toInt())

        val event = EventModel(ageRestriction = ageRestriction,
                creator = MyApplication.userId,
                description = descriptionEdit.text.toString(),
                guestLimit = guestLimit.text.toString().toInt(),
                eventType = if (privateButton.isChecked) EventType.PRIVATE else EventType.PUBLIC,
                guests = listOf(),
                googleMapsURL = choosenPlace?.address?.toString() ?: "",
                latitude = choosenPlace?.latLng?.latitude ?: 0.0,
                longitude = choosenPlace?.latLng?.longitude ?: 0.0,
                id = null,
                qrCodeLink = null,
                name = eventNameEdit.text.toString(),
                locationName = choosenPlace?.name?.toString() ?: "",
                timeCreated = System.currentTimeMillis()/1000,
                endTime = TimestampManager(baseContext).dateToTimestamp("$chosenDay.$chosenMonth.$chosenYear $chosenHour:$chosenMinute"),
                startTime = TimestampManager(baseContext).dateToTimestamp("$chosenDayEnd.$chosenMonthEnd.$chosenYearEnd $chosenHourEnd:$chosenMinuteEnd"))

        event.name = eventNameEdit.text.toString()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val place = PlacePicker.getPlace(data, this)
                    val toastMsg = String.format("Place: %s", place.name)
                    Toast.makeText(baseContext, toastMsg, Toast.LENGTH_SHORT).show()
                    choosenPlace = place
                    locationNameEdit.text = place.name
                }
            }
        }
    }

    class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
        var isEnd = false
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            isEnd = savedInstanceState?.getBoolean("is_end") ?: false

            return TimePickerDialog(activity, this, hour, minute,
                    DateFormat.is24HourFormat(activity))
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            Toast.makeText(context, "$hourOfDay : $minute", Toast.LENGTH_SHORT).show()
            if (isEnd) {
                chosenMinuteEnd = if (minute > 9) "$minute" else "0$minute"
                chosenHourEnd = "$hourOfDay"
            } else {
                chosenMinute = if (minute > 9) "$minute" else "0$minute"
                chosenHour = "$hourOfDay"
            }
        }
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
        var isEnd = false
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            isEnd = savedInstanceState?.getBoolean("is_end") ?: false

            return DatePickerDialog(activity!!, this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            Toast.makeText(context, "$year . $month . $day", Toast.LENGTH_SHORT).show()
            if (isEnd) {
                chosenYearEnd = "$year"
                chosenMonthEnd = "${month + 1}"
                chosenDayEnd = "$day"
            } else {
                chosenYear = "$year"
                chosenMonth = "${month + 1}"
                chosenDay = "$day"
            }
        }
    }
}
