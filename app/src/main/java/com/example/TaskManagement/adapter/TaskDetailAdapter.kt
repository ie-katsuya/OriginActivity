package com.example.TaskManagement.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.TaskManagement.R
import com.example.TaskManagement.model.entity.Job
import java.text.SimpleDateFormat

class TaskDetailAdapter(context: Context) : BaseAdapter() {
    private var mLayoutInflater: LayoutInflater
    private var mJobArrayList: MutableList<Job> = mutableListOf()

    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getItem(position: Int): Any {
        return mJobArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mJobArrayList.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View = convertView ?: mLayoutInflater.inflate(R.layout.favorite_task, null)

        val textView1 = view.findViewById<TextView>(R.id.titleTextView)
        val textView2 = view.findViewById<TextView>(R.id.dateTextView)
        val textView3 = view.findViewById<TextView>(R.id.userName)

        textView1.text = mJobArrayList[position].title

        val sdf = SimpleDateFormat("yyyy年 M月 d日")

        val date = mJobArrayList[position].date

        textView2.text = sdf.format(date)

        textView3.text = mJobArrayList[position].userName
        return view
    }

    fun insertUpdateJob(job: Job) {
        //追加するジョブが無ければ新しく追加する
        if (mJobArrayList.filter { listItem -> listItem.jobId == job.jobId }.isEmpty()) {
            mJobArrayList.add(job)
            setJobList(mJobArrayList)
            notifyDataSetChanged()
            return
        }

        //ジョブの更新
        mJobArrayList.singleOrNull { targetJob ->
            targetJob.jobId == job.jobId
        }?.also { targetJob ->
            mJobArrayList.remove(targetJob)
            mJobArrayList.add(job)
            setJobList(mJobArrayList)
            notifyDataSetChanged()
            return
        }
    }

    fun setJobList(JobArrayList: List<Job>) {
        mJobArrayList = JobArrayList.sortedWith(Comparator { a, b ->
            if (a.date > b.date) 1 else -1 //正数：昇順　負数：降順
        }).toMutableList()
        this.notifyDataSetChanged()
    }

    fun getJob(position: Int): Job {
        return mJobArrayList[position]
    }

}
