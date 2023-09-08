package com.des.dao

interface DatabaseFactory {
    suspend fun <T> dbQuery(block: suspend () -> T): T
}