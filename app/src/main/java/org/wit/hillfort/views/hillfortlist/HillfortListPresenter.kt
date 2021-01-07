package org.wit.hillfort.views.hillfortlist

import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import org.wit.hillfort.activities.SettingsActivity
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class HillfortListPresenter(view: BaseView) : BasePresenter(view) {

  fun getHillforts() {
    doAsync {
      val hillforts = app.hillforts.findAll()
      uiThread {
        view?.showHillforts(hillforts)
      }
    }
  }

  fun doAddHillfort() {
    view?.navigateTo(VIEW.HILLFORT)
  }

  fun doEditHillfort(hillfort: HillfortModel) {
    val keyArray: Array<String> = arrayOf("hillfort_edit")
    val valueArray: Array<Parcelable?> = arrayOf(hillfort)
    view?.navigateTo(VIEW.HILLFORT, 0, keyArray, valueArray)
  }

  fun doShowHillfortsMap() {
    view?.navigateTo(VIEW.MAPS)
  }

  fun doShowFavorites() {
    view?.navigateTo(VIEW.FAV)
  }

  fun doUpdateFavorite(hillfort: HillfortModel) {
    doAsync {
      app.hillforts.update(hillfort) // Update before display to account for clicked favoriteStar on card view
    }
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
