package org.wit.hillfort.views.favoritelist

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

class FavoriteListPresenter(view: BaseView) : BasePresenter(view) {

  fun getHillforts() {
    doAsync {
      val hillforts = app.hillforts.findAll().filter { it.favorite }
      uiThread {
        view?.showHillforts(hillforts)
      }
    }
  }

  fun doEditHillfort(hillfort: HillfortModel) {
    var keyArray: Array<String> = arrayOf("hillfort_edit")
    var valueArray: Array<Parcelable?> = arrayOf(hillfort)
    view?.navigateTo(VIEW.HILLFORT, 0, keyArray, valueArray)
  }

  fun doShowHillfortsMap() {
    view?.navigateTo(VIEW.MAPS)
  }

  fun doShowSettings() {
    view?.startActivityForResult(view!!.intentFor<SettingsActivity>(), 0)
  }

  fun doLogout() {
    FirebaseAuth.getInstance().signOut()
    app.hillforts.clear()
    view?.navigateTo(VIEW.LOGIN)
  }
}
