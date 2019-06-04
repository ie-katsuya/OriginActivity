package com.example.originactivity.model.entity

import java.io.Serializable

data class Task(
    val title: String,
    val pass: String,
    val goal: String,
    val date: Long,
    val taskId: String
) : Serializable
