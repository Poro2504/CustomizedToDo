package com.example.navigationbar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ToDoDao {

    @Insert
    suspend fun inserttask(toDoModel: ToDoModel): Long

    @Query("select * from ToDoModel where isFinished==0")
    fun getTask(): LiveData<List<ToDoModel>>

    @Query("update ToDoModel set isFinished=1 where id=:uid")
    fun finishtask(uid: Long)

    @Query("delete from ToDoModel where id=:uid")
    fun deletetask(uid: Long)
}