package com.des.daos

interface DatabaseFactory {

    fun init()

    suspend fun <T> dbQuery(block: suspend () -> T): T
}