package org.wit.hillfort.views.hillfortlist

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.activities.LoginActivity
import org.wit.hillfort.activities.SettingsActivity
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.hillfort.HillfortView

class HillfortListPresenter(val view: HillfortListView) {

  var app: MainApp

  init {
    app = view.application as MainApp
  }

  fun getHillforts(user: UserModel) = app.hillforts.findAllByUser(user)
//  fun getHillforts() = app.hillforts.findAll()

  fun doAddHillfort(loggedInUser: UserModel) {
    view.startActivityForResult(view.intentFor<HillfortView>().putExtra("loggedInUser", loggedInUser), 0)
  }

  fun doEditHillfort(hillfort: HillfortModel, loggedInUser: UserModel) {
    var intent = view.intentFor<HillfortView>().putExtra("loggedInUser", loggedInUser)
    intent.putExtra("hillfort_edit", hillfort)
    view.startActivityForResult(intent, 0)
  }

  fun doShowHillfortsMap() {
    view.startActivity<HillfortMapView>()
  }

  fun doShowSettings(loggedInUser: UserModel) {
    view.startActivityForResult(view.intentFor<SettingsActivity>().putExtra("loggedInUser", loggedInUser), 0)
  }

  fun doLogout() {
    view.startActivity(view.intentFor<LoginActivity>())
  }
}
