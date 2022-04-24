package com.rockstar.mvvmtutorial.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rockstar.mvvmtutorial.entity.User

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from user")
    fun getContact(): LiveData<List<User>>

    @Query("select * from user where mobileNumber=:mobileNumber ")
    suspend fun getParticularUser(mobileNumber: String) : User

    @Query("select * from user where mobileNumber=:mobileNumber and password=:password")
    suspend fun getUserLogin(mobileNumber: String,password:String) : User
}