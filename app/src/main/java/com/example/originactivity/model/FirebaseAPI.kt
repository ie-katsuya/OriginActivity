package com.example.originactivity.model


import com.example.originactivity.Const
import com.example.originactivity.model.entity.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

abstract class FirebaseAPI{

    protected val firebaseReference = FirebaseDatabase.getInstance().reference

    // ログイン済みのユーザーを取得する
    protected val user = FirebaseAuth.getInstance().currentUser

}