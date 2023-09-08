package com.des.dao

interface DatabaseFactory {

    fun init()

    suspend fun <T> dbQuery(block: suspend () -> T): T
}