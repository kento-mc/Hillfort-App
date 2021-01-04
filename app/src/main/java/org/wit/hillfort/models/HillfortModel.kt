package org.wit.hillfort.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@TypeConverters
data class HillfortModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                         var fbId : String = "",
                         var title: String = "",
                         var description: String = "",
                         var contributor: String = "",
                         var isVisited: Boolean = false,
                         var dateVisited: String = "",
//                         var image: String = "",
                         var images: MutableList<String> = ArrayList(),
                         @Embedded var location : Location = Location()) : Parcelable
@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
