package com.example.TaskManagement.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.TaskManagement.R
import com.example.TaskManagement.model.api.SetTaskAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_task_create.*
import java.util.*


class TaskCreateActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, TaskCreateActivity::class.java)
        }
    }

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var date: String = ""

    private val taskAPI = SetTaskAPI()

    // ログイン済みのユーザーを取得する
    var user = FirebaseAuth.getInstance().currentUser

    //日付の処理
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
        setContentView(R.layout.activity_task_create)
        setTitle("タスク作成")

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        val calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)

        Decide_button.setOnClickListener(this)
        date_button.setOnClickListener(mOnDateClickListener)
    }

    override fun onClick(v: View) {
        // キーボードが出てたら閉じる
        val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        val tdata = HashMap<String, Any>()

        val title = title_Edit.text.toString()
        val pass = pass_Edit.text.toString()

        if (title.isEmpty() == true) {
            // タイトルが入力されていない時はエラーを表示するだけ
            Snackbar.make(v, "タイトルを入力して下さい", Snackbar.LENGTH_LONG).show()
            return
        }

        if (pass.isEmpty() == true) {
            // 質問が入力されていない時はエラーを表示するだけ
            Snackbar.make(v, "パスワードを入力して下さい", Snackbar.LENGTH_LONG).show()
            return
        }

        if (date.isEmpty() == true) {
            // 質問が入力されていない時はエラーを表示するだけ
            Snackbar.make(v, "日付を入力して下さい", Snackbar.LENGTH_LONG).show()
            return
        }

        val calendar = GregorianCalendar(mYear, mMonth, mDay)
        val date = calendar.time

        tdata["title"] = title
        tdata["pass"] = pass
        tdata["date"] = date.time

        taskAPI.setTask(tdata) { isResult ->
            if (isResult) {
                finish()
            }
        }
    }

    // アクションバーの戻る処理
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
