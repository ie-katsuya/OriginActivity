package com.example.originactivity.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.originactivity.R
import com.example.originactivity.entity.Task
import java.text.SimpleDateFormat
import java.util.*

class TasklistAdapter(context: Context) : BaseAdapter() {
    private var mLayoutInflater: LayoutInflater
    private var mTaskArrayList = ArrayList<Task>()

    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getItem(position: Int): Any {
        return mTaskArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mTaskArrayList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View = convertView ?: mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null)

        val titleText = convertView!!.findViewById<View>(R.id.titleTextView) as TextView
        titleText.text = mTaskArrayList[position].title

        val goalText = convertView!!.findViewById<View>(R.id.goal_Edit) as TextView
        goalText.text = mTaskArrayList[position].goal

        val textView1 = view.findViewById<TextView>(android.R.id.text1)
        val textView2 = view.findViewById<TextView>(android.R.id.text2)

        textView1.text = "タイトル： " + mTaskArrayList[position].title
        //textView2.text = "目標： " + mTaskArrayList[position].goal

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE)
        val date = mTaskArrayList[position].date
        textView2.text = simpleDateFormat.format(date)

        return view
    }

}

