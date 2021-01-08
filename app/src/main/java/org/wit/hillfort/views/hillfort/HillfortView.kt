package org.wit.hillfort.views.hillfort

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RatingBar
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.views.BaseView
import java.text.SimpleDateFormat
import java.util.*

class HillfortView : BaseView(), AnkoLogger {

  lateinit var presenter: HillfortPresenter
  var hillfort = HillfortModel()
  lateinit var map: GoogleMap
  var currentDate: String = ""
  var tempTitle: String = ""
  var tempDescription: String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)

    super.init(toolbarAdd, true)

    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync {
      map = it
      presenter.doConfigureMap(map)
      it.setOnMapClickListener {
        setTempText()
        presenter.doSetLocation()
      }
    }

    presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

    val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
    currentDate = simpleDateFormat.format(Date())
    ratingBar.rating = presenter.hillfort.rating.toFloat()

//    if (intent.hasExtra("hillfort_edit")) {
//      val hFort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
//      presenter.app.hillforts.update(presenter.hillfort)
//    }

    ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { p0: RatingBar?, p1: Float, p2: Boolean ->
      presenter.hillfort.rating = p1.toInt()
    }

    chooseImage.setOnClickListener {
      if (presenter.hillfort.images.size == 4 ) {
        toast(R.string.change_hillfort_4_images)
      } else {
        setTempText()
        presenter.doSelectMultiImage()
      }
    }

    hillfortImage.setOnClickListener {
      setTempText()
      presenter.doSelectImageOne()
    }

    hillfortImage2.setOnClickListener {
      setTempText()
      presenter.doSelectImageTwo()
    }

    hillfortImage3.setOnClickListener {
      setTempText()
      presenter.doSelectImageThree()
    }

    hillfortImage4.setOnClickListener {
      setTempText()
      presenter.doSelectImageFour()
    }
  }

  fun setTempText() {
    tempTitle = hillfortTitle.text.toString()
    tempDescription = description.text.toString()
  }

  fun setImageVisability(hillfort: HillfortModel) {
    val imageVars = arrayOf(hillfortImage, hillfortImage2, hillfortImage3, hillfortImage4)
    imageVars.forEachIndexed {index, it ->
      if (index >= hillfort.images.size) {
        it.visibility = View.GONE
      } else {
        it.visibility = View.VISIBLE
      }
    }
  }

  override fun onResume() {
    if (!intent.hasExtra("hillfort_edit")) {
      hillfortTitle.setText(tempTitle)
      description.setText(tempDescription)
    }
    super.onResume()
    toolbarAdd.title = "${title}: ${presenter.app.currentUser.email}"
    mapView.onResume()
    presenter.doResartLocationUpdates()
  }

  override fun showHillfort(hillfort: HillfortModel) {
    if (hillfortTitle.text.isEmpty()) hillfortTitle.setText(hillfort.title)
    if (description.text.isEmpty()) description.setText(hillfort.description)
    val imageVars = arrayOf(hillfortImage, hillfortImage2, hillfortImage3, hillfortImage4)
    var i = 0
    while (i < hillfort.images.size) {
//      imageVars[i].setImageBitmap((readImageFromPath(this, hillfort.images[i])))
      Glide.with(this).load(hillfort.images[i]).into(imageVars[i])
      i++
    }
    this.showLocation(hillfort.location)
    if (hillfort.images.size == 4) {
      chooseImage.setText(R.string.change_hillfort_4_images)
    } else if (hillfort.images.isNotEmpty()) {
      chooseImage.setText(R.string.change_hillfort_image)
    }
    setImageVisability(hillfort)
  }

  override fun showLocation(loc: Location) {
    lat.text = "Lat: " + "%.6f".format(loc.lat)
    lng.text = "Lng: " + "%.6f".format(loc.lng)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort, menu)

    // Set checkmark status
    val menuVisCheck: MenuItem = menu?.findItem(R.id.item_mark_visited)
    menuVisCheck.isCheckable = true
    if (presenter.hillfort.isVisited) menuVisCheck.isChecked = true

    val menuFavCheck: MenuItem = menu?.findItem(R.id.item_mark_favorite)
    menuFavCheck.isCheckable = true
    if (presenter.hillfort.favorite) menuFavCheck.isChecked = true

    // Hide delete option on first creation of hillfort
    val item: MenuItem = menu.findItem(R.id.item_delete)
    if (!presenter.edit) {
      item.isVisible = false
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_save -> {
        if (hillfortTitle.text.toString().isEmpty()) {
          toast(R.string.enter_hillfort_title)
        } else {
          info(hillfortTitle.text.toString())
          info(description.text.toString())
          presenter.doAddOrSave(
            hillfortTitle.text.toString(),
            description.text.toString(),
            FirebaseAuth.getInstance().currentUser!!.uid,
            presenter.hillfort.isVisited,
            presenter.hillfort.dateVisited,
            presenter.hillfort.rating,
            presenter.hillfort.favorite)
        }
//        if (hillfort.title.isNotEmpty()) {
//          finish()
//        }
      }
      R.id.item_cancel -> {
        presenter.doCancel()
      }
      R.id.item_delete -> {
        presenter.doDelete()
      }
      R.id.item_logout -> {
        presenter.doLogout()
      }
      R.id.item_mark_visited -> {
        if (presenter.hillfort.isVisited) {
          item.isChecked = false
          presenter.hillfort.isVisited = false
          presenter.hillfort.dateVisited = ""
        } else {
          item.isChecked = true
          presenter.hillfort.isVisited = true
          presenter.hillfort.dateVisited = currentDate
        }
      }
      R.id.item_mark_favorite -> {
        if (presenter.hillfort.favorite) {
          item.isChecked = false
          presenter.hillfort.favorite = false
        } else {
          item.isChecked = true
          presenter.hillfort.favorite = true
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onBackPressed() {
    presenter.doCancel()
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

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}