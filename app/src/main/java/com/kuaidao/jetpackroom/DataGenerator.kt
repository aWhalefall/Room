package com.kuaidao.jetpackroom

import com.kuaidao.jetpackroom.db.entity.ClassRoom
import com.kuaidao.jetpackroom.db.entity.User

object DataGenerator {

    @JvmStatic
    fun generateProducts(): List<User> {
        val list = mutableListOf<User>()
        for (i in 0  until 100) {
            val temp=User(uid = i, firstName = "杨$i", lastName = "卫超$i",i.toString())
            list.add(temp)
        }
        return list
    }

    @JvmStatic
    fun generateRoom():List<ClassRoom>{
        val list = mutableListOf<ClassRoom>()
        for (i in 0 .. 5) {
            val temp=ClassRoom(roomId = i.toString(),roomGrade = i,masterTeacher = "teacher_$i")
            list.add(temp)
        }
        return list
    }

}