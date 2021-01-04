package org.wit.hillfort.main

import android.app.Application
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.*
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.models.json.HillfortJSONStore

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore
    lateinit var currentUser: FirebaseUser
    lateinit var users: UserStore
    lateinit var loggedInUser: UserModel

    override fun onCreate() {
        super.onCreate()
//        hillforts = HillfortJSONStore(applicationContext)
        hillforts = HillfortFireStore(applicationContext)
        users = UserJSONStore(applicationContext)
        info("Hillfort started")
    }
}