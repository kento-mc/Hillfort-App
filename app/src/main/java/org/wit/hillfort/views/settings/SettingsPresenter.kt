package org.wit.hillfort.views.settings

import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.Message
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

  var user = FirebaseAuth.getInstance().currentUser

  fun doUserUpdate(email: String? = "", password: String? = "") {
    view?.showProgress()
    if (email != "" && password != "") {
      user!!.updateEmail(email!!).addOnCompleteListener(view!!) {
        if (it.isSuccessful) {
          user!!.updatePassword(password!!).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
              view?.hideProgress()
              val keyArray: Array<String> = arrayOf("updateResult")
              val valueArray: Array<Parcelable?> = arrayOf(Message("Email and password updated successfully"))
              view?.navigateTo(VIEW.LIST, 0, keyArray, valueArray)
            }
          }
        }
      }
    } else if (email != "") {
      user!!.updateEmail(email!!).addOnCompleteListener(view!!) {
        if (it.isSuccessful) {
          view?.hideProgress()
          val keyArray: Array<String> = arrayOf("updateResult")
          val valueArray: Array<Parcelable?> = arrayOf(Message("Email updated successfully"))
          view?.navigateTo(VIEW.LIST, 0, keyArray, valueArray)
        }
      }
    } else if (password != "") {
      user!!.updatePassword(password!!).addOnCompleteListener(view!!) {
        if (it.isSuccessful) {
          view?.hideProgress()
          val keyArray: Array<String> = arrayOf("updateResult")
          val valueArray: Array<Parcelable?> = arrayOf(Message("Password updated successfully"))
          view?.navigateTo(VIEW.LIST, 0, keyArray, valueArray)
        }
      }
    } else {
      val keyArray: Array<String> = arrayOf("updateResult")
      val valueArray: Array<Parcelable?> = arrayOf(Message("Update failed"))
      view?.navigateTo(VIEW.LIST, 0, keyArray, valueArray)
    }
  }

  fun doLogout() {
    FirebaseAuth.getInstance().signOut()
    app.hillforts.clear()
    view?.navigateTo(VIEW.LOGIN)
  }
}