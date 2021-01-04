package org.wit.hillfort.views.hillfortlist

import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.activities.LoginActivity
import org.wit.hillfort.activities.SettingsActivity
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import org.wit.hillfort.views.hillfort.HillfortView

class HillfortListPresenter(view: BaseView) : BasePresenter(view) {

  fun getHillforts() {
    doAsync {
//    view?.showHillforts(app.hillforts.findAllByUser(user))
      val hillforts = app.hillforts.findAll()
      uiThread {
        view?.showHillforts(hillforts)
      }
    }
  }

  fun doAddHillfort() {
    view?.navigateTo(VIEW.HILLFORT)
  }

  fun doEditHillfort(hillfort: HillfortModel, loggedInUser: UserModel) {
    var keyArray: Array<String> = arrayOf("loggedInUser", "hillfort_edit")
    var valueArray: Array<Parcelable?> = arrayOf(loggedInUser, hillfort)
    view?.navigateTo(VIEW.HILLFORT, 0, keyArray, valueArray)
  }

  fun doShowHillfortsMap(loggedInUser: UserModel) {
    var keyArray: Array<String> = arrayOf("loggedInUser")
    var valueArray: Array<Parcelable?> = arrayOf(loggedInUser)
    view?.navigateTo(VIEW.MAPS)
  }

  fun doShowSettings(loggedInUser: UserModel) {
    view?.startActivityForResult(view!!.intentFor<SettingsActivity>().putExtra("loggedInUser", loggedInUser), 0)
  }

  fun doLogout() {
    FirebaseAuth.getInstance().signOut()
    app.hillforts.clear()
    view?.navigateTo(VIEW.LOGIN)
  }
}
