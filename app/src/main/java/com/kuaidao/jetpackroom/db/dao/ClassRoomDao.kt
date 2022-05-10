package com.kuaidao.jetpackroom.db.dao

import androidx.room.*
import com.kuaidao.jetpackroom.db.entity.ClassRoom
import com.kuaidao.jetpackroom.db.entity.ClassRoomWithUser

@Dao
interface ClassRoomDao {
    @Query("SELECT * FROM classroom")
    suspend fun getAll(): List<ClassRoom>

    @Query("SELECT * FROM classroom WHERE roomId IN (:roomIds)")
    fun loadAllByIds(roomIds: IntArray): List<ClassRoom>


    @Insert
    fun insertAll(vararg classrooms: ClassRoom)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ClassRoom>)

    @Delete
    fun delete(classroom: ClassRoom)

    @Transaction
    @Query("SELECT * FROM classroom")
    suspend fun getClassRoom(): List<ClassRoomWithUser>
}