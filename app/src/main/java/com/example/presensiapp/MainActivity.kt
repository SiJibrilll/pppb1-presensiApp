package com.example.presensiapp

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var etHour: EditText
    private lateinit var etMinute: EditText
    private lateinit var spinnerLocation: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupSpinners()
        setupListeners()
    }

    private fun initViews() {
        calendarView = findViewById(R.id.calendarView)
        etHour = findViewById(R.id.etHour)
        etMinute = findViewById(R.id.etMinute)
        spinnerLocation = findViewById(R.id.spinnerLocation)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnSubmit = findViewById(R.id.btnSubmit)
    }

    private fun setupSpinners() {
        // Location spinner data
        val locationItems = arrayOf(
            "Pilih Tempat Waktu",
            "Kantor Pusat",
            "Kantor Cabang Jakarta",
            "Kantor Cabang Surabaya",
            "Remote/WFH"
        )

        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, locationItems)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLocation.adapter = locationAdapter

        // Category spinner data
        val categoryItems = arrayOf(
            "Sakit",
            "Hadir Tepat Waktu",
            "Terlambat",
            "Izin",
            "Keterangan"
        )

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryItems)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter
    }

    private fun setupListeners() {
        // Calendar listener
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)

            Toast.makeText(this, "Tanggal dipilih: $formattedDate", Toast.LENGTH_SHORT).show()
        }

        // Submit button listener
        btnSubmit.setOnClickListener {
            handleSubmit()
        }
    }

    private fun handleSubmit() {
        val hour = etHour.text.toString()
        val minute = etMinute.text.toString()
        val location = spinnerLocation.selectedItem.toString()
        val category = spinnerCategory.selectedItem.toString()

        // Get selected date from calendar
        val selectedDate = Date(calendarView.date)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate)

        // Validation
        if (hour.isEmpty() || minute.isEmpty()) {
            Toast.makeText(this, "Mohon isi waktu dengan lengkap", Toast.LENGTH_SHORT).show()
            return
        }

        if (location == "Pilih Tempat Waktu") {
            Toast.makeText(this, "Mohon pilih tempat waktu", Toast.LENGTH_SHORT).show()
            return
        }

        // Show success message
        val message = """
            Presensi berhasil disimpan!
            
            Tanggal: $formattedDate
            Waktu: $hour:$minute AM
            Lokasi: $location
            Kategori: $category
        """.trimIndent()

        Toast.makeText(this, "Presensi berhasil disimpan!", Toast.LENGTH_LONG).show()

        // Reset form
        resetForm()
    }

    private fun resetForm() {
        etHour.setText("")
        etMinute.setText("")
        spinnerLocation.setSelection(0)
        spinnerCategory.setSelection(0)
    }

    override fun onBackPressed() {
        showExitDialog()
    }

    private fun showExitDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_exit_custom)
        dialog.setCancelable(false)

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnExit = dialog.findViewById<Button>(R.id.btnExit)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnExit.setOnClickListener {
            dialog.dismiss()
            finishAffinity() // This will close the app completely
        }

        dialog.show()
    }
}