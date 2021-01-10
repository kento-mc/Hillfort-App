package org.wit.hillfort.views.favoritelist

import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class FavoriteListPresenter(view: BaseView) : BasePresenter(view) {

  fun getHillforts() {
    doAsync {
      val hillforts = app.hillforts.findAll().filter { it.favorite }
      uiThread {
        view?.showHillforts(hillforts as ArrayList)
      }
    }
  }

  fun doCheckForUpdates() {
    val fireStore = app.hillforts as HillfortFireStore
    fireStore!!.fetchHillforts {
      getHillforts()
    }
  }

  fun doEditHillfort(hillfort: HillfortModel) {
    val keyArray: Array<String> = arrayOf("hillfort_edit")
    val valueArray: Array<Parcelable?> = arrayOf(hillfort)
    view?.navigateTo(VIEW.HILLFORT, 0, keyArray, valueArray)
  }

  fun doShowHillfortsMap() {
    view?.navigateTo(VIEW.FAVMAP)
  }

  fun doShowSettings() {
    view?.navigateTo(VIEW.SETTINGS)
  }

  fun doDeleteFromList(fbId: String) {
    val hillfortToDelete = app.hillforts.findByFbId(fbId)
    app.hillforts.deleteFromList(hillfortToDelete!!)
//    doCheckForUpdates()
  }

  fun doLogout() {
    FirebaseAuth.getInstance().signOut()
    app.hillforts.clear()
    view?.navigateTo(VIEW.LOGIN)
  }
}
