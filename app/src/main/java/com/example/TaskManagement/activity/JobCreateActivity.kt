package com.example.TaskManagement.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Spinner
import com.example.TaskManagement.R
import com.example.TaskManagement.adapter.CustumSpinnerAdapter
import com.example.TaskManagement.model.api.SetJobAPI
import com.example.TaskManagement.model.api.SpinnerAPI
import com.example.TaskManagement.model.entity.Job
import com.example.TaskManagement.model.entity.User
import com.example.taskapp.UserNameAdapter
import kotlinx.android.synthetic.main.activity_add_job.*
import kotlinx.android.synthetic.main.activity_task_create.Decide_button
import kotlinx.android.synthetic.main.activity_task_create.date_button
import java.text.SimpleDateFormat
import java.util.*

class JobCreateActivity : AppCompatActivity() {

    companion object {
        private const val KEY_TASK_ID = "KEY_TASK_ID"
        private const val KEY_JOB = "KEY_JOB"
        fun createIntent(context: Context, taskId: String, job: Job?): Intent {
            return Intent(context, JobCreateActivity::class.java).also {
                it.putExtra(KEY_TASK_ID, taskId)
                it.putExtra(KEY_JOB, job)
            }
        }
    }

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var stringDate: String = ""
    private var longDate = 0L
    private var taskId: String = ""
    private var job: Job? = null
    private var title: String = ""
    private var updateflag: Boolean = false

    private val jobAPI = SetJobAPI()
    private val userAPI = SpinnerAPI()

    private val mUserAdapter by lazy { UserNameAdapter(this) }
    private var spinnerAdapter = CustumSpinnerAdapter()
    private var spinnerItems: MutableList<String> = mutableListOf()
    private var selectUserName: String = ""

    //時間設定
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
                stringDate = date_button.text.toString()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_job)
        setTitle("ジョブ作成")

        taskId = intent.getStringExtra(KEY_TASK_ID)
        job = intent.getSerializableExtra(KEY_JOB) as? Job

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        job?.also { job ->
            updateflag = true
            job_Edit.setText(job.title)
            title = job.title

            val sdf = SimpleDateFormat("yyyy年 M月 d日")
            longDate = job.date
            date_button.text = sdf.format(longDate)
            stringDate = sdf.format(longDate)

            selectUserName = job.userName
        }

        setSpinner()

        touchSpinnerButton()

        //カレンダーの初期設定を現在の日付に
        val calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)

        date_button.setOnClickListener(mOnDateClickListener)
        Decide_button.setOnClickListener {
            registrationJob(it)
        }
    }

    private fun registrationJob(v: View) {
        title = job_Edit.text.toString()

        // キーボードが出てたら閉じる
        val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        if (title.isEmpty()) {
            // 仕事内容が入力されていない時はエラーを表示するだけ
            Snackbar.make(v, "仕事内容を入力して下さい", Snackbar.LENGTH_LONG).show()
            return
        }

        if (stringDate.isEmpty()) {
            // パスワードが入力されていない時はエラーを表示するだけ
            Snackbar.make(v, "日付を入力して下さい", Snackbar.LENGTH_LONG).show()
            return
        }

        if (selectUserName.isEmpty()) {
            // パスワードが入力されていない時はエラーを表示するだけ
            Snackbar.make(v, "担当者を入力して下さい", Snackbar.LENGTH_LONG).show()
            return
        }

        val calendar = GregorianCalendar(mYear, mMonth, mDay)
        var date = calendar.time

        if (updateflag == true) {
            jobAPI.updateJob(taskId, title, date.time, job!!.jobId, selectUserName) { isupdateResult ->
                if (isupdateResult) {
                    finish()
                }
            }
        } else {
            jobAPI.setJob(taskId, title, date.time, selectUserName) { issetValueResult ->
                if (issetValueResult) {
                    finish()
                }
            }
        }
    }

    private fun setSpinner() {
        // spinner に adapter をセット
        spinner_userid.adapter = spinnerAdapter
        spinnerAdapter.userList = spinnerItems

        userAPI.setSpinner(taskId) { userList ->
            mUserAdapter.spinnerlist = userList

            spinner_userid.adapter = mUserAdapter
            mUserAdapter.notifyDataSetChanged()
        }
    }

    private fun touchSpinnerButton() {

        //spinnerを操作した時
        spinner_userid.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //　アイテムが選択された時
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                var spinnerParent = parent as Spinner
                val user = spinnerParent.selectedItem as User
                selectUserName = user.userName
            }

            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    // アクションバーの戻る処理
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
