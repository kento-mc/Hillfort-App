package org.wit.hillfort.views

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.favoritelist.FavoriteListView
import org.wit.hillfort.views.favoritemap.FavoriteMapView
import org.wit.hillfort.views.hillfort.HillfortView
import org.wit.hillfort.views.hillfortlist.HillfortListView

import org.wit.hillfort.views.location.EditLocationView
import org.wit.hillfort.views.login.LoginView
import org.wit.hillfort.views.map.HillfortMapView
import org.wit.hillfort.views.settings.SettingsView
import org.wit.hillfort.views.signup.SignupView

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2
val MULTIPLE_IMAGE_REQUEST = 3
val IMAGE_CHANGE_2 = 4
val IMAGE_CHANGE_3 = 5
val IMAGE_CHANGE_4 = 6

enum class VIEW {
  LOCATION, HILLFORT, MAPS, FAVMAP, LIST, FAV, LOGIN, SIGNUP, SETTINGS
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

  var basePresenter: BasePresenter? = null

  fun navigateTo(view: VIEW, code: Int = 0, key: Array<String> = emptyArray(), value: Array<Parcelable?> = emptyArray<Parcelable?>()){
    var intent = Intent(this, HillfortListView::class.java)
    when (view) {
      VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
      VIEW.HILLFORT -> intent = Intent(this, HillfortView::class.java)
      VIEW.MAPS -> intent = Intent(this, HillfortMapView::class.java)
      VIEW.FAVMAP -> intent = Intent(this, FavoriteMapView::class.java)
      VIEW.LIST -> intent = Intent(this, HillfortListView::class.java)
      VIEW.FAV -> intent = Intent(this, FavoriteListView::class.java)
      VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
      VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
      VIEW.SIGNUP -> intent = Intent(this, SignupView::class.java)
    }
    if (key.isNotEmpty()) {
      key.forEachIndexed { index, key ->
        intent.putExtra(key, value[index])
      }
    }
    startActivityForResult(intent, code)
  }

  fun initPresenter(presenter: BasePresenter): BasePresenter {
    basePresenter = presenter
    return presenter
  }

  fun init(toolbar: Toolbar, upEnabled: Boolean) {
    toolbar.title = title
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
      toolbar.title = "${title}: ${user.email}"
    }
  }

  override fun onDestroy() {
    basePresenter?.onDestroy()
    super.onDestroy()
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      basePresenter?.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  open fun showHillfort(hillfort: HillfortModel) {}
  open fun showHillforts(hillforts: List<HillfortModel>) {}
  open fun showLocation(location: Location) {}
  open fun showProgress() {}
  open fun hideProgress() {}
}