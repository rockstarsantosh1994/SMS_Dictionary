package com.rockstar.mvvmtutorial.data_model

import android.os.Parcel
import android.os.Parcelable

data class SmsDataClass(val _id:String,
                        val _address:String,
                        val _msg:String,
                        val _readState:String,
                        val _time:String,
                        val _folderName:String,
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun toString(): String {
        return "SmsDataClass(_id='$_id', _address='$_address', _msg='$_msg', _readState='$_readState', _time='$_time', _folderName='$_folderName')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(_address)
        parcel.writeString(_msg)
        parcel.writeString(_readState)
        parcel.writeString(_time)
        parcel.writeString(_folderName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SmsDataClass> {
        override fun createFromParcel(parcel: Parcel): SmsDataClass {
            return SmsDataClass(parcel)
        }

        override fun newArray(size: Int): Array<SmsDataClass?> {
            return arrayOfNulls(size)
        }
    }
}
