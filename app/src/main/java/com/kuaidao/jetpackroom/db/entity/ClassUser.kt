package com.kuaidao.jetpackroom.db.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*


/**
 * Author: yangweichao
 * Date:   2022/5/9 3:52 下午
 * Description: 班级表
 */


@Entity
data class ClassRoom(
    @NonNull
    @PrimaryKey val roomId: String,
    @NonNull
    val masterTeacher: String ="",
    @Nullable
    val roomGrade: Int?= 1
)



/**
 * Author: yangweichao
 * Date:   2022/5/9 3:55 下午
 * Description:班级与用户一对多
 */
//@Entity(tableName = "User")
@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,

    @Nullable
    var userRoomId: String?,
    @ColumnInfo(defaultValue = "test")
    val autoMigrationTest: String="",
    @ColumnInfo(defaultValue = "ext01")
    val ext: String="",
    @Embedded val address: Address?= null
)

data class Address(
    val street: String?,
    val state: String?,
    val city: String?,
    @ColumnInfo(name = "post_code") val postCode: Int
)


/**
 * Author: yangweichao
 * Date:   2022/5/9 4:25 下午
 * Description: 班级用户关系 一对多
 */


data class ClassRoomWithUser(
    @Embedded val classRoom: ClassRoom,
    @Relation(
        parentColumn = "roomId",
        entityColumn = "userRoomId"
    )
    val roomlists: List<User> = emptyList()
)