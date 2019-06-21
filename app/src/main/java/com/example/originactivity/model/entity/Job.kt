package com.example.originactivity.model.entity

import java.io.Serializable

class Job (
    val title: String,
    val date: Long,
    val jobId: String,
    val userName: String
) : Serializable