package org.wit.hillfort.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_settings.toolbar
import kotlinx.android.synthetic.main.activity_login.userEmail
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.btnRegister
import kotlinx.android.synthetic.main.activity_signup.userName
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class SettingsActivity : AppCompatActivity(), AnkoLogger {

  var user = UserModel()
  lateinit var app : MainApp
  var loggedInUser : UserModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    toolbar.title = "Hillfort Settings"
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)


    info("Login to Hillfort app..")

    app = application as MainApp

    if (intent.hasExtra("loggedInUser")) {
      loggedInUser = intent.extras?.getParcelable<UserModel>("loggedInUser")!!
      info("User:")
      info(loggedInUser)
    }

    userName.setText(loggedInUser?.userName)
    userEmail.setText(loggedInUser?.email)
    password.setText(loggedInUser?.password)

    btnUpdate.setOnClickListener() {
      loggedInUser?.userName = userName.text.toString()
      loggedInUser?.email = userEmail.text.toString()
      loggedInUser?.password = password.text.toString()

      app.users.update(loggedInUser?.copy()!!)
      finish()
    }

    val userNum: Int = app.hillforts.findAll().filter { it.contributor == app.currentUser.uid }.size
    statsHillfortsNum.setText("Hillforts: $userNum")
    val visitedNum: Int = app.hillforts.findAll().filter { it.isVisited }.size
    statsHillfortsVisited.setText("Visited: $visitedNum")
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_cancel -> {
        finish()
      }
      R.id.item_logout -> {
        loggedInUser = null
        startActivity(intentFor<LoginActivity>())
      }
    }
    return super.onOptionsItemSelected(item)
  }
}