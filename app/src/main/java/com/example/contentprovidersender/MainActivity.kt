package com.example.contentprovidersender

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var btnAddUser: Button
    private lateinit var recyclerViewUsers: RecyclerView
    private val userList = mutableListOf<String>()
    private lateinit var adapter: NameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editTextName = findViewById(R.id.editTextName)
        btnAddUser = findViewById(R.id.btnAddUser)
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers)

        adapter = NameAdapter(userList)
        recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        recyclerViewUsers.adapter = adapter

        loadUsers()

        btnAddUser.setOnClickListener {
            val name = editTextName.text.toString().trim()
            if (name.isNotEmpty()) {
                insertUser(name)
                editTextName.text.clear()
            }
        }
    }

    @SuppressLint("Range")
    private fun loadUsers() {
        val cursor = contentResolver.query(NameContentProvider.CONTENT_URI, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val name = it.getString(it.getColumnIndex("name"))
                    userList.add(name)
                } while (it.moveToNext())
            }
            cursor.close()
        }
        adapter.notifyDataSetChanged()
    }

    private fun insertUser(name: String) {
        val values = ContentValues().apply {
            put("name", name)
        }
        contentResolver.insert(NameContentProvider.CONTENT_URI, values)
        userList.add(name)
        adapter.notifyDataSetChanged()
    }
}
