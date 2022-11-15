package com.example.debtabase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class CustomerModel (
    var cusId: String? = null,
    var cusFN: String? = null,
    var cusLN: String? = null,
    var custPN: String? = null
)