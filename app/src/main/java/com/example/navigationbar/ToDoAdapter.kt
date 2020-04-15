package com.example.navigationbar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat
import java.util.*

class ToDoAdapter(val list: List<ToDoModel>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false

            )
        )


    }

    override fun getItemCount()=list.size



    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemId(position:Int): Long {
        return list[position].id
    }
    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(toDoModel: ToDoModel) {
            with(itemView) {
//                val colors = resources.getIntArray(R.array.random_color)
//                val randomColor = colors[Random().nextInt(colors.size)]
//                viewColorTag.setBackgroundColor(randomColor)
                tasktilte.text = toDoModel.title
                subtitle.text = toDoModel.discription
                updateTime(toDoModel.time)
                updateDate(toDoModel.date)
            }
        }

        private fun updateDate(time: Long)  {
            val myformat = "EEE,d MMM yyyy"
            val sdf =SimpleDateFormat(myformat)
            itemView.txtShowDate.text=sdf.format(Date(time))
        }

        private fun updateTime(time: Long) {
            val myformat = "h:mm a"
            val sdf = SimpleDateFormat(myformat)
            itemView.texttime.text = sdf.format(Date(time))
        }


    }


}