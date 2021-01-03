package org.wit.hillfort.views.map

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BaseView

class HillfortMapView : BaseView(), GoogleMap.OnMarkerClickListener {

  lateinit var presenter: HillfortMapPresenter
  lateinit var map: GoogleMap
  open var loggedInUser: UserModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_maps)
    super.init(toolbar, true)

    if (intent.hasExtra("loggedInUser")) {
      loggedInUser = intent.extras?.getParcelable<UserModel>("loggedInUser")!!
      info("User:")
      info(loggedInUser)
    }

    presenter = initPresenter(HillfortMapPresenter(this)) as HillfortMapPresenter

    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync {
      map = it
      map.setOnMarkerClickListener(this)
      presenter.loadHillforts()
    }
  }

  override fun showHillfort(hillfort: HillfortModel) {
    currentTitle.text = hillfort.title
    currentDescription.text = hillfort.description
    currentImage.setImageBitmap(readImageFromPath(this, hillfort.images[0]))
  }

  override fun showHillforts(hillforts: List<HillfortModel>) {
    presenter.doPopulateMap(map, hillforts)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doMarkerSelected(marker)
    return true
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}