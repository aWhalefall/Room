package com.kuaidao.jetpackroom

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //userLiST()

        classRoom()

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

