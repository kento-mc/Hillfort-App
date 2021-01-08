package org.wit.hillfort.views.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.progressBar
import kotlinx.android.synthetic.main.activity_login.userEmail
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW


class SettingsView : BaseView(), AnkoLogger {

  lateinit var presenter: SettingsPresenter
  var auth = FirebaseAuth.getInstance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    init(toolbar, true)
    progressBar.visibility = View.GONE

    setSupportActionBar(toolbar)

    presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

    userEmail.hint = auth.currentUser?.email

    btnUpdate.setOnClickListener() {
      presenter.doUserUpdate(userEmail.text.toString(), password.text.toString())
    }

    val userNum: Int = presenter.app.hillforts.findAll().size
    statsHillfortsNum.setText("Hillforts: $userNum")
    val visitedNum: Int = presenter.app.hillforts.findAll().filter { it.isVisited }.size
    statsHillfortsVisited.setText("Visited: $visitedNum")
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_cancel -> {
        finish()
      }
      R.id.item_logout -> {
        presenter.doLogout()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun showProgress() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideProgress() {
    progressBar.visibility = View.GONE
  }
}