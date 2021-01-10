package org.wit.hillfort.views.favoritemap

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.activity_hillfort_maps.toolbar
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.map.HillfortMapPresenter

class FavoriteMapView : BaseView(), GoogleMap.OnMarkerClickListener {

  lateinit var presenter: FavoriteMapPresenter
  lateinit var map: GoogleMap

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_maps)
    super.init(toolbar, true)

    presenter = initPresenter(FavoriteMapPresenter(this)) as FavoriteMapPresenter

    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync {
      map = it
      map.setOnMarkerClickListener(this)
      presenter.loadHillforts()
    }

    cardPreClick.visibility = View.VISIBLE
    currentTitle.visibility = View.GONE
    currentDescription.visibility = View.GONE
    currentImage.visibility = View.GONE
    lat.visibility = View.GONE
    lng.visibility = View.GONE
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
      toolbar.title = "Favorites: ${user.email}"
    }

    return super.onCreateOptionsMenu(menu)
  }

  override fun showHillfort(hillfort: HillfortModel) {
    currentTitle.text = hillfort.title
    currentDescription.text = hillfort.description
    lat.text = "Lat: ${"%.6f".format(hillfort.location.lat)}"
    lng.text = "Lng: ${"%.6f".format(hillfort.location.lng)}"
//    currentImage.setImageBitmap(readImageFromPath(this, hillfort.images[0]))
    Glide.with(this).load(hillfort.images[0]).into(currentImage)
  }

  override fun showHillforts(hillforts: ArrayList<HillfortModel>) {
    presenter.doPopulateMap(map, hillforts)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    cardPreClick.visibility = View.GONE
    currentTitle.visibility = View.VISIBLE
    currentDescription.visibility = View.VISIBLE
    currentImage.visibility = View.VISIBLE
    lat.visibility = View.VISIBLE
    lng.visibility = View.VISIBLE
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