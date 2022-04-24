package com.rockstar.mvvmtutorial.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class Keywords(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val keyword:String,
){
    override fun toString(): String {
        return "Keywords(id=$id, keyword='$keyword')"
    }
}
