package com.goloveschenko.calculator.entity

data class HistoryItem (
    var id: Long?,
    var date: String,
    var expression: String,
    var result: String,
    var comment: String?
)
