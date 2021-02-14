package com.example.login

import android.os.Parcel
import android.os.Parcelable

data class Question(
    val uid: Int = 0,
    val title: String = "",
    var completed: Boolean = false,
    var favorite: Boolean = false,
    val content: String = "",
    val url: String = "",
    val companies: String = "",
    val role: String = "",
    val frequency: Double = 0.0,
    val topics: String = "",
    val college: String = "",
    val trending: Boolean = false,
    val difficulty: String = "",
    val acceptance_rate: Double = 0.0,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(title)
        parcel.writeByte(if (completed) 1 else 0)
        parcel.writeByte(if (favorite) 1 else 0)
        parcel.writeString(content)
        parcel.writeString(url)
        parcel.writeString(companies)
        parcel.writeString(role)
        parcel.writeDouble(frequency)
        parcel.writeString(topics)
        parcel.writeString(college)
        parcel.writeByte(if (trending) 1 else 0)
        parcel.writeString(difficulty)
        parcel.writeDouble(acceptance_rate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {

        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}