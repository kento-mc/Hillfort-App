package org.wit.hillfort.views.settings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

  var user = FirebaseAuth.getInstance().currentUser

  fun doUserUpdate(email: String? = "", password: String? = ""): String {
    view?.showProgress()
    var emailResult = false
    var passwordResult = false
    var returnString = ""
    if (email != "") {
      emailResult = getEmail(email)
    }
    if (password != "") {
      passwordResult = getPassword(password)
    }
    if (emailResult && passwordResult) {
      returnString = "Email and password updated successfully"
    } else if (emailResult) {
      returnString = "Email updated successfully"
    } else {
      returnString = "Password updated successfully"
    }
    view?.hideProgress()
    return returnString
  }

  fun getEmail(email: String?): Boolean {
    var returnVal = false
    if (email != "") {
      user!!.updateEmail(email!!).addOnCompleteListener(view!!) {
        if (it.isSuccessful) {
         returnVal = true
        }
      }
    }
    return returnVal
  }

  fun getPassword(password: String?): Boolean {
    var returnVal = false
    if (password != "") {
      user!!.updatePassword(password!!).addOnCompleteListener(view!!) {
        if (it.isSuccessful) {
          returnVal = true
        }
      }
    }
    return returnVal
  }

  fun doLogout() {
    FirebaseAuth.getInstance().signOut()
    app.hillforts.clear()
    view?.navigateTo(VIEW.LOGIN)
  }
}