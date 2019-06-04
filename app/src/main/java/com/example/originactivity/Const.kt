package com.example.originactivity

import com.example.originactivity.model.entity.Job

class Const{
    companion object{
        const val UsersPATH = "users"       // Firebaseにユーザの表示名を保存するパス
        const val PathWord = "passes"     // Firebaseにパスワードを保存するバス
        const val JobPATH = "job"     // Firebaseに解答を保存するパス
        const val NameKEY = "name"          // Preferenceに表示名を保存する時のキー
        const val ContentsPATH = "contents" // Firebaseにタスクを保存するバス
        const val Favorite = "favorites" // Firebaseにタスクを保存するバス
    }
}