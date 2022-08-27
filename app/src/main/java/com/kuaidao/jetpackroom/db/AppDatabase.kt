/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kuaidao.jetpackroom.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kuaidao.jetpackroom.AppExecutors
import com.kuaidao.jetpackroom.DataGenerator.generateProducts
import com.kuaidao.jetpackroom.DataGenerator.generateRoom
import com.kuaidao.jetpackroom.db.dao.ClassRoomDao
import com.kuaidao.jetpackroom.db.dao.UserDao
import com.kuaidao.jetpackroom.db.entity.ClassRoom
import com.kuaidao.jetpackroom.db.entity.User
import java.util.concurrent.TimeUnit

//@Database(entities = [User::class, ClassRoom::class], version = 2)
@Database(
    entities = [User::class, ClassRoom::class], version = 4, autoMigrations = [
        AutoMigration(from = 3, to = 4)
    ], exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun classRoomDao(): ClassRoomDao
    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

     @RenameTable(fromTableName = "User", toTableName = "appuser")
     class RenameMigration : AutoMigrationSpec


    /**
     * Check whether the database already exists and expose it via [.getDatabaseCreated]
     */
    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }

    val databaseCreated: LiveData<Boolean>
        get() = mIsDatabaseCreated

    companion object {
        private var sInstance: AppDatabase? = null

        @VisibleForTesting
        val DATABASE_NAME = "basic-sample-db"

        @JvmStatic
        fun getInstance(context: Context, executors: AppExecutors): AppDatabase? {
            if (sInstance == null) {
                synchronized(AppDatabase::class.java) {
                    if (sInstance == null) {
                        sInstance = buildDatabase(context.applicationContext, executors)
                        sInstance!!.updateDatabaseCreated(context.applicationContext)
                    }
                }
            }
            return sInstance
        }
         var dbName:String="12"
        @JvmStatic
        fun getInstance(context: Context, executors: AppExecutors,name: String): AppDatabase? {
            if(!dbName.equals(name)){
                sInstance=null
            }
            if (sInstance == null) {
                synchronized(AppDatabase::class.java) {
                    if (sInstance == null) {
                        sInstance = buildDatabase(context.applicationContext, executors,name)
                        sInstance!!.updateDatabaseCreated(context.applicationContext)
                    }
                }
            }
            return sInstance
        }

        /**
         * Build the database. [Builder.build] only sets up the database configuration and
         * creates a new instance of the database.
         * The SQLite database is only created when it's accessed for the first time.
         */
        private fun buildDatabase(
            appContext: Context,
            executors: AppExecutors
        ): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        executors.diskIO().execute {

                            // Generate the data for pre-population
                            val database = getInstance(appContext, executors)
                            val users = generateProducts()
                            val rooms = generateRoom()
                            insertData(database, users, rooms)
                            // notify that the database was created and it's ready to be used
                            database!!.setDatabaseCreated()
                        }
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        @SuppressLint("UnsafeOptInUsageError")
        private fun buildDatabase(
            appContext: Context,
            executors: AppExecutors
            ,name:String
        ): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, "test_$name")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        executors.diskIO().execute {

                            // Generate the data for pre-population
                            val database = getInstance(appContext, executors,name)
                            val users = generateProducts()
                            val rooms = generateRoom()
                            insertData(database, users, rooms)
                            // notify that the database was created and it's ready to be used
                            database!!.setDatabaseCreated()
                        }
                    }
                }).setAutoCloseTimeout(20,TimeUnit.SECONDS).build()
        }

        private fun insertData(
            database: AppDatabase?,
            users: List<User>,
            classRooms: List<ClassRoom>
        ) {
            database!!.runInTransaction {
                database.userDao().insertAll(users)
                database.classRoomDao().insertAll(classRooms)
            }
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user ADD COLUMN userRoomId INTEGER")
                database.execSQL("CREATE TABLE IF NOT EXISTS classroom (`roomId` TEXT NOT NULL, `masterTeacher` TEXT NOT NULL, `roomGrade` INTEGER NOT NULL, PRIMARY KEY(`roomId`))")
            }
        }
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {}
        }
    }

    interface demo{

    }
}