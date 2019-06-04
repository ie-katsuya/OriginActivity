package com.example.originactivity.model.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

abstract class FirebaseAPI {

    protected val firebaseReference = FirebaseDatabase.getInstance().reference

    // ログイン済みのユーザーを取得する
    protected val user = FirebaseAuth.getInstance().currentUser

}