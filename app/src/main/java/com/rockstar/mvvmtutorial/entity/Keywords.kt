package com.rockstar.mvvmtutorial.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class Keywords(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val keyword:String,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString()
    )

    override fun toString(): String {
        return "Keywords(id=$id, keyword='$keyword')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(keyword)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Keywords> {
        override fun createFromParcel(parcel: Parcel): Keywords {
            return Keywords(parcel)
        }

        override fun newArray(size: Int): Array<Keywords?> {
            return arrayOfNulls(size)
        }
    }
}
