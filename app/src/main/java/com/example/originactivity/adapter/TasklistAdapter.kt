package com.example.originactivity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.originactivity.model.entity.Task
import java.text.SimpleDateFormat
import java.util.*


class TasklistAdapter(context: Context) : BaseAdapter() {
    private var mLayoutInflater: LayoutInflater
    private var mTaskArrayList: List<Task> = emptyList()

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

    @SuppressLint("SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View = convertView ?: mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null)

        //val titleText = convertView!!.findViewById<View>(R.id.titleTextView) as TextView
        //titleText.text = mTaskArrayList[position].title

        val textView1 = view.findViewById<TextView>(android.R.id.text1)
        val textView2 = view.findViewById<TextView>(android.R.id.text2)

        textView1.text = "タイトル： " + mTaskArrayList[position].title

        val sdf = SimpleDateFormat("yyyy年 M月 d日")

        val date = mTaskArrayList[position].date

        textView2.text = "完了予定日： " + sdf.format(date)

        return view
    }

    fun setTaskList(taskArrayList: List<Task>) {
        mTaskArrayList = taskArrayList.sortedWith(Comparator{a, b ->
            if (a.date > b.date) 1 else -1 //正数：昇順　負数：降順
        })
        this.notifyDataSetChanged()
    }

    fun getTask(position: Int):Task{
        return mTaskArrayList[position]
    }

}

