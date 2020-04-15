package com.example.navigationbar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ToDoModel::class], version = 1)
abstract class Appdatabase : RoomDatabase() {
    abstract fun tododao(): ToDoDao

    companion object {

        @Volatile
        private var INSTANCE: Appdatabase? = null

        fun getDatabase(context: Context):Appdatabase{
            val tempInstance= INSTANCE
            if(tempInstance!=null) {

                return tempInstance
            }
             synchronized(this){
                 val instance=Room.databaseBuilder(
                     context.applicationContext,
                     Appdatabase::class.java,
                     DB_NAME
                 ).build()
                 INSTANCE=instance
                 return instance
             }
        }
    }
}