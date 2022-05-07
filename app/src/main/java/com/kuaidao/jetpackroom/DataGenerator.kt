package com.kuaidao.jetpackroom

import com.kuaidao.jetpackroom.db.entity.User

object DataGenerator {

    @JvmStatic
    fun generateProducts(): List<User> {
        val list = mutableListOf<User>()
        for (i in 0  until 100) {
            val temp=User(uid = i, firstName = "杨$i", lastName = "卫超$i")
            list.add(temp)
        }
        return list
    }


}