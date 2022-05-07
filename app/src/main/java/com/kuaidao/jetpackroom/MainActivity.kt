package com.kuaidao.jetpackroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuaidao.jetpackroom.databinding.ActivityMainBinding
import com.kuaidao.jetpackroom.db.AppDatabase
import com.kuaidao.jetpackroom.db.entity.User

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val build = AppDatabase.getInstance(this, AppExecutors())
        val userDao = build.userDao()
        val users: List<User> = userDao.getAll()
         mBinding.txt.text= users.toString()

    }
}
