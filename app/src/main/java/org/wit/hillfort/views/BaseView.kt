package org.wit.hillfort.views

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.hillfort.HillfortView
import org.wit.hillfort.views.hillfortlist.HillfortListView

import org.wit.hillfort.views.location.EditLocationView
import org.wit.hillfort.views.map.HillfortMapView

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2
val MULTIPLE_IMAGE_REQUEST = 3
val IMAGE_CHANGE_2 = 4
val IMAGE_CHANGE_3 = 5
val IMAGE_CHANGE_4 = 6

enum class VIEW {
  LOCATION, HILLFORT, MAPS, LIST
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

  var basePresenter: BasePresenter? = null

  fun navigateTo(view: VIEW, code: Int = 0, key: Array<String> = emptyArray(), value: Array<Parcelable?> = emptyArray<Parcelable?>()){
    var intent = Intent(this, HillfortListView::class.java)
    when (view) {
      VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
      VIEW.HILLFORT -> intent = Intent(this, HillfortView::class.java)
      VIEW.MAPS -> intent = Intent(this, HillfortMapView::class.java)
      VIEW.LIST -> intent = Intent(this, HillfortListView::class.java)
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
  open fun showProgress() {}
  open fun hideProgress() {}
}