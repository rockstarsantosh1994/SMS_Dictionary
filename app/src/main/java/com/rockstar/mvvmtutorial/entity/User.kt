package com.rockstar.mvvmtutorial.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val firstName:String,
    val lastName:String,
    val mobileNumber:String,
    val password:String

){
    override fun toString(): String {
        return "User(id=$id, firstName='$firstName', lastName='$lastName', mobileNumber='$mobileNumber', password='$password')"
    }
}
