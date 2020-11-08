package org.wit.hillfort.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.toolbarLogin
import kotlinx.android.synthetic.main.activity_login.userEmail
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class SignupActivity : AppCompatActivity(), AnkoLogger {

  var user = UserModel()
  lateinit var app : MainApp
  lateinit var loggedInUser : UserModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_signup)
    toolbarLogin.title = title
    setSupportActionBar(toolbarLogin)
    info("Login to Hillfort app..")

    app = application as MainApp

    btnRegister.setOnClickListener() {
      user.userName = userName.text.toString()
      user.email = userEmail.text.toString()
      user.password = password.text.toString()
      if (user.userName.isEmpty() || user.email.isEmpty() || user.password.isEmpty()) {
        toast(R.string.enter_user_email_password)
      } else {
        if (app.users.create(user.copy())!!) {
          loggedInUser = app.users.findOneByEmail(user)!!
        } else {
          toast(R.string.user_exists)
        }
      }
      if (loggedInUser != null) {
        setResult(AppCompatActivity.RESULT_OK)
        startActivity(intentFor<HillfortListActivity>().putExtra("loggedInUser", loggedInUser))
        finish()
      } else {
        toast(R.string.enter_user_email_password)
      }
      startActivity(intentFor<SignupActivity>())
    }

    loginLink.setOnClickListener() {
      startActivity(intentFor<LoginActivity>())
    }
  }
}