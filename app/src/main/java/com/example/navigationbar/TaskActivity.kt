package com.example.navigationbar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TimePicker
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "todo.db"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mycalendar: Calendar
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    var finalDate = 0L
    var finalTime = 0L
    val labels = arrayListOf("Person", "Business", "Insurance", "Shopping", "Banking")
    val db by lazy {
        Appdatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
        saveBtn.setOnClickListener(this)
        setUpSpinner()
    }

    private fun setUpSpinner() {
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, labels)
        labels.sort()
       spinnerCategory.adapter = adapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dateEdt -> {
                setListener()
            }
            R.id.timeEdt -> {
                setTimeListener()
            }
            R.id.saveBtn -> {
                saveToDo()
            }
        }
    }

    private fun saveToDo() {
        val category = spinnerCategory.selectedItem.toString()
        val title = titleInpLay.editText?.text.toString()
        val discription = taskInpLay.editText?.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.tododao().inserttask(
                    ToDoModel(title, discription, category, finalDate, finalTime)

                )
            }
            finish()
        }
    }

    private fun setTimeListener() {
        mycalendar= Calendar.getInstance()
        timeSetListener=
        TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
            mycalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            mycalendar.set(Calendar.MINUTE, minute)
            UpdateTime()
        }
        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener,
            mycalendar.get(Calendar.DAY_OF_MONTH),
            mycalendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    private fun UpdateTime() {
        val myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)
        timeEdt.setText(sdf.format(mycalendar.time))
        finalTime = mycalendar.time.time
    }

    private fun setListener() {
        mycalendar = Calendar.getInstance()

        dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                mycalendar.set(Calendar.YEAR, year)
                mycalendar.set(Calendar.MONTH, month)
                mycalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                UpdateDate()

            }
        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            mycalendar.get(Calendar.YEAR),
            mycalendar.get(Calendar.MONTH),
            mycalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun UpdateDate() {

        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        dateEdt.setText(sdf.format(mycalendar.time))
        finalDate = mycalendar.time.time
        timeInptLay.visibility = View.VISIBLE
    }
}
