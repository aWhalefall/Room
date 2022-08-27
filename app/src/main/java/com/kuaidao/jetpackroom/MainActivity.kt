package com.kuaidao.jetpackroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuaidao.jetpackroom.databinding.ActivityMainBinding
import com.kuaidao.jetpackroom.db.AppDatabase
import com.kuaidao.jetpackroom.db.entity.ClassRoomWithUser
import com.kuaidao.jetpackroom.db.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    var dbName: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //userLiST()

        // classRoom()

        mBinding.btnCreatdb.setOnClickListener {
            dbName++
            classRoomByName("${dbName} _kuaidao")
        }
        mBinding.btnInset.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    db?.userDao()?.insertAll(
                        User(
                            firstName = "yang",
                            lastName = "weichao",
                            uid = System.currentTimeMillis().toInt(),
                            userRoomId = "1"
                        )
                    )
                }
            }
        }
    }


    private fun classRoom() {
        lifecycleScope.launch {
            val build = AppDatabase.getInstance(this@MainActivity, AppExecutors())
            val classRoom = build?.classRoomDao()
            val list: List<ClassRoomWithUser> = withContext(Dispatchers.IO) {
                classRoom!!.getClassRoom()
            }
            mBinding.version.text = list.toString()
        }
    }

    var db: AppDatabase? = null
    private fun classRoomByName(dbName: String) {
        lifecycleScope.launch {
            db = AppDatabase.getInstance(this@MainActivity, AppExecutors(), dbName)
            val classRoom = db?.classRoomDao()
            val list: List<ClassRoomWithUser> = withContext(Dispatchers.IO) {
                classRoom!!.getClassRoom()
            }
            mBinding.version.text = list.toString()
        }
    }

    private fun MainActivity.userLiST() {
        lifecycleScope.launch {
            val build = AppDatabase.getInstance(this@MainActivity, AppExecutors())
            val userDao = build!!.userDao()
            val users: List<User> = withContext(Dispatchers.IO) {
                userDao.getAll()
            }
            mBinding.txt.text = users.toString()
        }
    }


}

