package com.rockstar.mvvmtutorial

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rockstar.mvvmtutorial.dao.UserDao
import com.rockstar.mvvmtutorial.entity.User

@Database(entities = [User::class], version = 1)
abstract class UserDataBase:RoomDatabase() {

    abstract fun userDao() :UserDao

    companion object{
        @Volatile
        private var INSTANCE:UserDataBase?=null

        fun getDatabase(context: Context):UserDataBase{
            synchronized(this){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context,UserDataBase::class.java,"userDB").build()
                }
            }
            return INSTANCE!!
        }
    }
}