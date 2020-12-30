package org.wit.hillfort.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class LoginActivity : AppCompatActivity(), AnkoLogger {

  var user = UserModel()
  lateinit var app : MainApp
  var loggedInUser : UserModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    toolbarLogin.title = title
    setSupportActionBar(toolbarLogin)
    info("Login to Hillfort app..")

    app = application as MainApp

    btnLogin.setOnClickListener() {
      user.email = userEmail.text.toString()
      user.password = password.text.toString()
      if (user.email.isEmpty() || user.password.isEmpty()) {
        toast(R.string.enter_email_password)
      } else {
        loggedInUser = app.users.validate(user.copy())!!
      }
      if (loggedInUser != null) {
        setResult(AppCompatActivity.RESULT_OK)
        startActivity(intentFor<HillfortListView>().putExtra("loggedInUser", loggedInUser))
        finish()
      } else {
        toast(R.string.enter_email_password)
      }
    }

    createAccountLink.setOnClickListener() {
      startActivity(intentFor<SignupActivity>())
    }
  }
}