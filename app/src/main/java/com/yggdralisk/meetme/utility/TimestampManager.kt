package com.yggdralisk.meetme.utility

import android.content.Context
import android.os.Build
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Maciek on 2018-04-16.
 */
class TimestampManager(private val context: Context){
    private val currentLocale = getCurrentLocale(context)
    private val formatDate = SimpleDateFormat("dd.MM.yyyy", currentLocale)
    private val formatDateHour = SimpleDateFormat("dd.MM.yyyy hh:mm", currentLocale)
    private val cal = Calendar.getInstance(currentLocale)!!

    fun toDateString(timestamp:Long):String{
        val timestampMilis = timestamp * 1000L
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestampMilis
        return formatDate.format(cal.time)
    }

    fun toDateHourString(timestamp:Long):String = if(timestamp != 0L){
            val timestampMilis = timestamp * 1000L
            cal.timeInMillis = timestampMilis
            formatDateHour.format(cal.time)
        }
        else ""

    fun dateToTimestamp(dateString:String):Long{
        var timestamp = 0L
        try{
            val date = formatDate.parse(dateString)
            cal.time = date
            timestamp = cal.timeInMillis / 1000
        }catch (e:ParseException){
            //TODO
        }
        return timestamp
    }

    fun dateHourToTimestamp(dateString:String):Long{
        var timestamp = 0L
        try{
            val date = formatDateHour.parse(dateString)
            cal.time = date
            timestamp = cal.timeInMillis / 1000
        }catch (e:ParseException){
            //TODO
        }
        return timestamp
    }

    private fun getCurrentLocale(context:Context):Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            context.resources.configuration.locales.get(0)
        } else{
            context.resources.configuration.locale
        }


    fun getTimestampHourBefore(timestamp: Long):Long{
        val timestampMilis = timestamp * 1000L
        cal.timeInMillis = timestampMilis
        cal.add(Calendar.HOUR, -1)
        val return_timestamp = cal.timeInMillis / 1000
        return return_timestamp
    }

    fun isTimestampInPast(timestamp: Long):Boolean = timestamp <= ( System.currentTimeMillis() / 1000)

//    fun isPast(timestamp: Long):Boolean{
//        //return
//    }
}