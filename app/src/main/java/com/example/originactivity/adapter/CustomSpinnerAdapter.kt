package com.example.originactivity.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckedTextView

class CustumAdapter: BaseAdapter(){
    var userList: List<String> = emptyList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: View.inflate(parent.context, android.R.layout.simple_spinner_dropdown_item, null)

        (view as CheckedTextView).text = userList[position]

        return view
    }

    override fun getItem(position: Int): Any {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getCount(): Int {
        return userList.size
    }
}