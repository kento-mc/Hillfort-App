package org.wit.hillfort.views.hillfort

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.activities.LoginActivity
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BaseView
import java.text.SimpleDateFormat
import java.util.*

class HillfortView : BaseView(), AnkoLogger {

  lateinit var presenter: HillfortPresenter
  var hillfort = HillfortModel()
  var loggedInUser : UserModel? = null
  lateinit var map: GoogleMap
  var currentDate: String = ""
  var tempTitle: String = ""
  var tempDescription: String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)

//    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
    currentDate = simpleDateFormat.format(Date())

    init(toolbarAdd)

    presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      map = it
      presenter.doConfigureMap(map)
    }

//    if (intent.hasExtra("loggedInUser")) {
//      loggedInUser = intent.extras?.getParcelable<UserModel>("loggedInUser")!!
//      info("User:")
//      info(loggedInUser)
//    }

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

    hillfortLocation.setOnClickListener {
      setTempText()
      presenter.doSetLocation()
    }
  }

  fun setTempText() {
    tempTitle = hillfortTitle.text.toString()
    tempDescription = description.text.toString()
  }

  override fun onResume() {
    if (!intent.hasExtra("hillfort_edit")) {
      hillfortTitle.setText(tempTitle)
      description.setText(tempDescription)
    }
    return super.onResume()
    mapView.onResume()
  }

  override fun showHillfort(hillfort: HillfortModel) {
    hillfortTitle.setText(hillfort.title)
    description.setText(hillfort.description)
//    if (hillfort.images.isNotEmpty()) hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.images[0]))

//    if (hillfort.image != null) {
//      chooseImage.setText(R.string.change_hillfort_image)
//    }
    val imageVars = arrayOf(hillfortImage, hillfortImage2, hillfortImage3, hillfortImage4)
    var i = 0
    while (i < hillfort.images.size) {
      imageVars[i].setImageBitmap((readImageFromPath(this, hillfort.images[i])))
      i++
    }
    if (hillfort.images.size == 4) {
      chooseImage.setText(R.string.change_hillfort_4_images)
    } else if (hillfort.images.isNotEmpty()) {
      chooseImage.setText(R.string.change_hillfort_image)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort, menu)

    // Show user in menu bar
    val menuUser: MenuItem = menu?.findItem(R.id.menu_user)!!
    menuUser.setTitle(presenter.app.loggedInUser?.userName)

    // Set checkmark status
    val menuCheck: MenuItem = menu?.findItem(R.id.item_mark_visited)
    menuCheck.isCheckable = true
    if (presenter.hillfort.isVisited) menuCheck.isChecked = true

    // Hide delete option on first creation of hillfort
    val item: MenuItem = menu.findItem(R.id.item_delete)
    if (!presenter.edit) {
      item.setVisible(false)
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_save -> {
        if (hillfortTitle.toString().isEmpty()) {
          toast(R.string.enter_hillfort_title)
        } else {
          info(hillfortTitle.text.toString())
          info(description.text.toString())
          presenter.doAddOrSave(
            hillfortTitle.text.toString(),
            description.text.toString(),
            presenter.app.loggedInUser!!.id,
            hillfort.isVisited,
            hillfort.dateVisited)
        }
      if (hillfort.title.isNotEmpty()) {
        finish()
      }
      }
      R.id.item_cancel -> {
        presenter.doCancel()
      }
      R.id.item_delete -> {
        presenter.doDelete()
      }
      R.id.item_logout -> {
        loggedInUser = null
        startActivity(intentFor<LoginActivity>())
      }
      R.id.item_mark_visited -> {
        if (presenter.hillfort.isVisited) {
          item.isChecked = false
          hillfort.isVisited = false
          hillfort.dateVisited = ""
        } else {
          item.isChecked = true
          hillfort.isVisited = true
          hillfort.dateVisited = currentDate
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