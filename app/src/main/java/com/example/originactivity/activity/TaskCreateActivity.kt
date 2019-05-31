package com.example.originactivity.activity

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.AsyncTaskLoader
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.originactivity.Const
import com.example.originactivity.R
import com.example.originactivity.adapter.TasklistAdapter
import com.example.originactivity.entity.FavoriteTask
import com.example.originactivity.entity.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_task_create.*
import java.text.SimpleDateFormat
import java.util.*


class TaskCreateActivity : AppCompatActivity()  , View.OnClickListener {

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private lateinit var mTask: Task
    private var date: String = ""
    private lateinit var mAdapter: TasklistAdapter

    // ログイン済みのユーザーを取得する
    var user = FirebaseAuth.getInstance().currentUser

    private val mOnDateClickListener = View.OnClickListener {
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDay = dayOfMonth
                val dateString =
                    mYear.toString() + "/" + String.format("%02d", mMonth + 1) + "/" + String.format("%02d", mDay)
                date_button.text = dateString
                date = date_button.text.toString()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_create)

        val calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)

        Buck_button.setOnClickListener(this)
        Decide_button.setOnClickListener(this)
        date_button.setOnClickListener(mOnDateClickListener)
    }

    override fun onClick(v: View?) {
        // キーボードが出てたら閉じる
        val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(v!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        val dataBaseReference = FirebaseDatabase.getInstance().reference
        val TaskRef = dataBaseReference.child(Const.ContentsPATH)

        val Tdata = HashMap<String, Any>()
        val Ddata = HashMap<String, Date>()

        if (v == Buck_button) {
            //タスク管理画面に遷移
            finish()
        }else if(v == Decide_button){
            val title = title_Edit.text.toString()
            val goal = goal_Edit.text.toString()
            val pass = pass_Edit.text.toString()

            if (title.isEmpty() == true) {
                // タイトルが入力されていない時はエラーを表示するだけ
                Snackbar.make(v!!, "タイトルを入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            if (goal.isEmpty() == true) {
                // 質問が入力されていない時はエラーを表示するだけ
                Snackbar.make(v!!, "目標を入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            if (pass.isEmpty() == true) {
                // 質問が入力されていない時はエラーを表示するだけ
                Snackbar.make(v!!, "パスワードを入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            if (date.isEmpty() == true) {
                // 質問が入力されていない時はエラーを表示するだけ
                Snackbar.make(v!!, "日付を入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            val calendar = GregorianCalendar(mYear, mMonth, mDay)
            val date = calendar.time

            val sdf = SimpleDateFormat("yyyy年 M月 d日")

            Tdata["title"] = title
            Tdata["goal"] = goal
            Tdata["pass"] = pass
            Tdata["date"] = date.time

            val taskp = TaskRef.push()
            val taskIdkey = taskp.getKey()

            taskp.setValue(Tdata)

            //関与しているタスクidをFavoriteに登録
            reloadId(taskIdkey!!, title, date.time)

            finish()
        }
    }

    private fun reloadId(taskIdkey: String, title: String, date: Any){

        val Fdata = HashMap<String, Any>()
        val dataBaseReference = FirebaseDatabase.getInstance().reference
        //val TRef = dataBaseReference.child(Const.ContentsPATH).child(mTask.TaskUid)
        val FavoriteRef = dataBaseReference.child(Const.Favorite).child(user!!.uid)

        FavoriteRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val Ref = FavoriteRef.child(taskIdkey)
                Fdata["title"] = title
                //Fdata["date"] = date
                Ref.setValue(Fdata)
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
}
