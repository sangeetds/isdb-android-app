package com.example.login

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_list")
data class Question (

    @PrimaryKey (autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "completed") var completed: Boolean = false,
    @ColumnInfo(name = "favorite") var favorite: Boolean = false,
    @ColumnInfo(name = "content") val content: String = "",
    @ColumnInfo(name = "url") val url: String = "",
    @ColumnInfo(name = "companies") val companies: String,
    @ColumnInfo(name = "role") val role: String,
    @ColumnInfo(name = "frequency") val frequency: Double,
    @ColumnInfo(name = "topics") val topics: String,
    @ColumnInfo(name = "college") val college: String,
    @ColumnInfo(name = "trending") val trending: Boolean,
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "acceptance_rate") val acceptance_rate: Double,
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
    ) {
    }

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

        val filters = listOf("Companies", "Role", "Frequency", "Topics", "College")
        val sort = listOf("Latest", "Oldest", "Most Asked", "Least Asked")

        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}