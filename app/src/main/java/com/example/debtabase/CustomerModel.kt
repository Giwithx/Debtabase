package com.example.debtabase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class CustomerModel (
    var cusId: String? = null,
    var cusFN: String? = null,
    var cusN: String? = null,
    var custPN: String? = null
)