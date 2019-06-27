package com.example.TaskManagement.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.TaskManagement.R
import com.example.TaskManagement.model.api.SetTaskAPI
import com.example.TaskManagement.model.entity.Task
import kotlinx.android.synthetic.main.activity_pass_check.*

class PassCheckActivity : AppCompatActivity() {
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
        setTitle("パスワードチェック")

        task = intent.getSerializableExtra(TaskDetailActivity.KEY_TASK) as Task

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        Decide_button.setOnClickListener {
            registrationPass(it)
        }

    }

    private fun registrationPass(v: View) {
        var i: Int = 0

        if (pass_pass.text.toString() != task.pass) {
            // タイトルが入力されていない時はエラーを表示するだけ
            Snackbar.make(v, "パスワードが間違っています", Snackbar.LENGTH_LONG).show()
            return
        }

        taskAuthentication {
            if (i == 0) {
                startActivity(TaskDetailActivity.createIntent(this, task))
                i++
            }
        }
    }

    //タスクにユーザーを登録、タスクをお気に入りにする
    private fun taskAuthentication(complete: () -> Unit) {
        settaskAPI.favoriteSave(task.title, task.taskId) { isFavoriteSaveResult ->
            if (!isFavoriteSaveResult) {
                return@favoriteSave
            }

            settaskAPI.userSave(task.taskId) { isUserSaveResult ->
                if (!isUserSaveResult) {
                    return@userSave
                }
                complete()

            }
        }
    }

    // アクションバーの戻る処理
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
