package com.yggdralisk.meetme.utility

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream


/**
 * Created by Jan Stoltman on 6/10/18.
 */
class PhotoEncoder {
    companion object {
        fun encodePhoto(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            val byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    }
}