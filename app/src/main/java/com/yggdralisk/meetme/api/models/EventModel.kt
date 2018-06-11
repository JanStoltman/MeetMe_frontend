package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 4/8/18.
 */
data class EventModel(
        @SerializedName("Id") var id: Int?,
        @SerializedName("TimeCreated") var timeCreated: Long?,
        @SerializedName("StartTime") var startTime: Long?,
        @SerializedName("EndTime") var endTime: Long?,
        @SerializedName("QrCode") var qrCodeLink: QrCode?,
        @SerializedName("EventName") var name: String?,
        @SerializedName("GuestsIds") var guests: List<Int>?,
        @SerializedName("CreatorId") var creator: Int?,
        @SerializedName("GuestLimit") var guestLimit: Int?,
        @SerializedName("AgeRestriction") var ageRestriction: AgeRestriction?,
        @SerializedName("EventType") var eventType: EventType?,
        @SerializedName("Latitude") var latitude: Double?,
        @SerializedName("Longitude") var longitude: Double?,
        @SerializedName("LocationName") var locationName: String?,
        @SerializedName("Description") var description: String?,
        @SerializedName("GoogleMapsURL") var googleMapsURL: String?,
        @SerializedName("Address") var address: String?,
        @SerializedName("Rating") var rating: Double?,
        @SerializedName("PhotosIds") var photosUrls: List<String>? = listOf()
)
