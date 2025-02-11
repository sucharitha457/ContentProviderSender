package com.example.contentprovidersender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NameAdapter(private val userList: List<String>) :
    RecyclerView.Adapter<NameAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewUser: TextView = view.findViewById(R.id.textViewUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.name_text, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.textViewUser.text = "${position + 1} - ${userList[position]}"
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
