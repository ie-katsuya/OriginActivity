package com.example.originactivity.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.originactivity.Const
import com.example.originactivity.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SettingActivity::class.java)
        }
    }

    private lateinit var mDataBaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setTitle("アカウント設定")

        // Preferenceから表示名を取得してEditTextに反映させる
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val name = sp.getString(Const.NameKEY, "")
        val nameText: EditText = findViewById(R.id.nameText)
        nameText.setText(name)

        mDataBaseReference = FirebaseDatabase.getInstance().reference

        // UIの初期設定
        title = "設定"

        changeButton.setOnClickListener { v ->
            // キーボードが出ていたら閉じる
            val im = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            // ログイン済みのユーザーを取得する
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                // ログインしていない場合は何もしない
                Snackbar.make(v, "ログインしていません", Snackbar.LENGTH_LONG).show()
            } else {

                val name = nameText.text.toString()

                // 変更した表示名をFirebaseに保存する
                saveFirebase(user.uid, name)

                // 変更した表示名をPreferenceに保存する
                saveDisplayName(name)

                Snackbar.make(v, "表示名を変更しました", Snackbar.LENGTH_LONG).show()
            }
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            nameText.setText("")
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        startActivity(LoginActivity.createIntent(this, true))
    }

    private fun saveFirebase(userId: String, name: String) {
        // 変更した表示名をFirebaseに保存する
        val userRef = mDataBaseReference.child(Const.UsersPATH).child(userId)
        val data = HashMap<String, String>()
        data["name"] = name
        userRef.setValue(data)
    }

    private fun saveDisplayName(name: String) {
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = sp.edit()
        editor.putString(Const.NameKEY, name)
        editor.commit()
    }

    override fun onBackPressed() {
        // バックキーを押した際、タスク管理画面に移行
        startActivity(TaskMainActivity.createIntent(this))
    }
}