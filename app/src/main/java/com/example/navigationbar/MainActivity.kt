package com.example.navigationbar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.item_todo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    var finalDate = 0L
    var finalTime = 0L
    val list = arrayListOf<ToDoModel>()

    val db by lazy {
        Appdatabase.getDatabase(this)
    }
    var todoadapter = ToDoAdapter(list)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nm=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            nm.createNotificationChannel(NotificationChannel("First","Default",NotificationManager.IMPORTANCE_DEFAULT))

        floating.setOnClickListener {
            onNewTask()
            val simpleNotification=NotificationCompat.Builder(this,"First")
                .setContentTitle("To do")
                .setContentText("Today's Task")
                .setSmallIcon(R.drawable.icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            nm.notify(1,simpleNotification)
        }

        rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            adapter = todoadapter
        }
        menu_bottom.setOnItemSelectedListener { id ->
            when (id) {
                R.id.history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                }
                R.id.floating -> {
                    startActivity(Intent(this, TaskActivity::class.java))
                }
            }

        }
        intDrag()

        db.tododao().getTask().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                list.clear()
                list.addAll(it)
                todoadapter.notifyDataSetChanged()
            } else {
                list.clear()
                todoadapter.notifyDataSetChanged()
            }
        })
    }

    private fun onNewTask() {
        startActivity(Intent(this, TaskActivity::class.java))
    }

    fun intDrag() {
        val simpleItemTouchHelper =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP or ItemTouchHelper.DOWN) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    if (direction == ItemTouchHelper.UP) {
                        GlobalScope.launch(Dispatchers.IO) {
                            db.tododao().deletetask(todoadapter.getItemId(position))
                            list.remove(ToDoModel("title", "discription", "category",finalDate,finalTime))
                        }
                        todoadapter.notifyItemRemoved(position)


                    } else if (direction == ItemTouchHelper.DOWN) {
                        GlobalScope.launch(Dispatchers.IO) {
                            db.tododao().finishtask(todoadapter.getItemId(position))
                        }
                        todoadapter.notifyItemRemoved(position)
                    }
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchHelper)
        itemTouchHelper.attachToRecyclerView(rv)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }


}



