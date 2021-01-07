package org.wit.hillfort.views.signup

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.toolbarLogin
import kotlinx.android.synthetic.main.activity_login.userEmail
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class SignupView : BaseView() {

  lateinit var presenter: SignupPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_signup)
    init(toolbarLogin, false)
    progressBar.visibility = View.GONE

    presenter = initPresenter(SignupPresenter(this)) as SignupPresenter

    btnRegister.setOnClickListener {
      val email = userEmail.text.toString()
      val password = password.text.toString()
      if (email == "" || password == "") {
        toast("Please provide email + password")
      }
      else {
        presenter.doSignUp(email,password)
      }
    }

    loginLink.setOnClickListener() {
      navigateTo(VIEW.LOGIN)
    }
  }

  override fun showProgress() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    progressBar.visibility = View.GONE
  }
}