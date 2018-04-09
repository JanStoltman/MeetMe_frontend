package com.yggdralisk.meetme.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Jan Stoltman on 4/8/18.
 */
data class Event(
        @SerializedName("id") var id: Int?,
        @SerializedName("Name") var name: String?,
        @SerializedName("GuestsIds")var guests: List<Int>?,
        @SerializedName("CreatorId")var creator: Int?,
        @SerializedName("TimeCreated")var timeCreated: Long?,
        @SerializedName("GuestLimit")var guestLimit: Int?,
        @SerializedName("AgeRestriction")var ageRestriction: AgeRestriction?,
        @SerializedName("EventType")var eventType: EventType?,
        @SerializedName("Location") var location: Location?,
        @SerializedName("StartTime")var startTime: Long?,
        @SerializedName("EndTime")var endTime: Long?,
        @SerializedName("QrCode")var qrCodeLink: QrCode?)