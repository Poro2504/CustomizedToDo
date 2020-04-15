package com.example.navigationbar

import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoModel (
    var title:String,
    var discription:String,
    var category:String,
    var date:Long,
    var time:Long,
    var isFinished: Int =0,
    @PrimaryKey(autoGenerate = true)
    var id:Long=0
)