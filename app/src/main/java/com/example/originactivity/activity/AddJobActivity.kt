package com.example.originactivity.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import com.example.originactivity.Const
import com.example.originactivity.R
import com.example.originactivity.adapter.TaskDetailAdapter
import com.example.originactivity.model.entity.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_job.*
import kotlinx.android.synthetic.main.activity_task_create.Buck_button
import kotlinx.android.synthetic.main.activity_task_create.Decide_button
import kotlinx.android.synthetic.main.activity_task_create.date_button
import java.util.*

class AddJobActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val KEY_TASK = "KEY_TASK"
        fun createIntent(context: Context, task: Task): Intent {
            return Intent(context, AddJobActivity::class.java).also {
                it.putExtra(KEY_TASK, task)
            }

        }
    }

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mListView: ListView
    private lateinit var mAdapter: TaskDetailAdapter

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var date: String = ""
    private lateinit var task: Task

    //private val taskAPI = TaskAPI()

    // ログイン済みのユーザーを取得する
    var user = FirebaseAuth.getInstance().currentUser

    private val mOnDateClickListener = View.OnClickListener {
        val datePickerDialog = DatePickerDialog(
            this,
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
        setContentView(R.layout.activity_add_job)

        task = intent.getSerializableExtra(KEY_TASK) as Task

        //カレンダーの初期設定を現在の日付に
        val calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)

        date_button.setOnClickListener(mOnDateClickListener)
        Decide_button.setOnClickListener(this)
        Buck_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //戻るボタンの場合
        if (v == Buck_button) {
            finish()
        } else {
            val title = job_Edit.text.toString()

            // キーボードが出てたら閉じる
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(v!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val dataBaseReference = FirebaseDatabase.getInstance().reference
            val JobRef = dataBaseReference.child(Const.ContentsPATH).child(task.TaskUid)

            if (title.isEmpty() == true) {
                // 仕事内容が入力されていない時はエラーを表示するだけ
                Snackbar.make(v!!, "仕事内容を入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            if (date.isEmpty() == true) {
                // 日付が入力されていない時はエラーを表示するだけ
                Snackbar.make(v!!, "パスワードを入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            val calendar = GregorianCalendar(mYear, mMonth, mDay)
            val date = calendar.time

            val JobData = HashMap<String, Any>()

            JobData["title"] = title
            JobData["date"] = date.time

            JobRef.child(Const.JobPATH).push().setValue(JobData)

            finish()
        }
    }

}
