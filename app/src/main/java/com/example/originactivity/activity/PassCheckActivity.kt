package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.originactivity.R
import com.example.originactivity.model.api.GetTaskAPI
import com.example.originactivity.model.api.SetTaskAPI
import com.example.originactivity.model.entity.Task
import kotlinx.android.synthetic.main.activity_pass_check.*

class PassCheckActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var task: Task

    companion object {
        private const val KEY_TASK = "KEY_TASK"

        fun createIntent(context: Context, task: Task): Intent {
            return Intent(context, PassCheckActivity::class.java).also {
                it.putExtra(KEY_TASK, task)
            }
        }
    }

    private val settaskAPI = SetTaskAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_check)

        task = intent.getSerializableExtra(TaskDetailActivity.KEY_TASK) as Task

        Decide_button.setOnClickListener(this)
        Buck_button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            Buck_button.id -> {
                finish()
            }
            else -> {
                registrationPass(v)
            }
        }
    }

    private fun registrationPass(v: View) {
        if (pass_pass.text.toString() != task.pass) {
            // タイトルが入力されていない時はエラーを表示するだけ
            Snackbar.make(v, "パスワードが間違っています", Snackbar.LENGTH_LONG).show()
            return
        }

        taskAuthentication{
            startActivity(TaskDetailActivity.createIntent(this, task))
        }
    }

    //タスクにユーザーを登録、タスクをお気に入りにする
    private fun taskAuthentication(complete: ()->Unit) {
        settaskAPI.userSave(task.taskId) { isUserSaveResult ->
            if (!isUserSaveResult) {
                return@userSave
            }
            settaskAPI.favoriteSave(task.title, task.taskId) { isFavoriteSaveResult ->
                if (!isFavoriteSaveResult) {
                    return@favoriteSave
                }
                complete()
            }

        }
    }
}
