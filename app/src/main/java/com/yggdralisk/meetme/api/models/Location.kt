package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 4/8/18.
 */
data class Location(
        @SerializedName("id") var id: Int?,
        @SerializedName("Longitude") var lon: Double?,
        @SerializedName("Latitude") var lat: Double?,
        @SerializedName("LocationName") var name: String?,
        @SerializedName("Description") var description: String?,
        @SerializedName("GoogleMapsURL") var googleMapsUrl: String?)