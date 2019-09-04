package com.example.foodrecipes

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AppExecutors {

    private val mDiskIO: ExecutorService = Executors.newSingleThreadExecutor()
    val diskIO: ExecutorService
        get() = mDiskIO

    private val mMainThreadExecutor: Executor = MainThreadExecutor()
    val mainThread: Executor
        get() = mMainThreadExecutor

    companion object {
        @Volatile
        private var instance: AppExecutors? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: AppExecutors().also { instance = it }
            }
    }

    inner class MainThreadExecutor : Executor {

        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
