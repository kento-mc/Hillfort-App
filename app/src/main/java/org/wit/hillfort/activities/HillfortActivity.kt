package org.wit.hillfort.activities

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImage
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.helpers.showMultipleImagesPicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.UserModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HillfortActivity : AppCompatActivity(), AnkoLogger {

  var hillfort = HillfortModel()
  lateinit var app : MainApp
  var loggedInUser : UserModel? = null
  var currentDate: String = ""
  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2
  val MULTIPLE_IMAGE_REQUEST = 3
  val IMAGE_CHANGE_2 = 4
  val IMAGE_CHANGE_3 = 5
  val IMAGE_CHANGE_4 = 6

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)
    toolbarAdd.title = title
    setSupportActionBar(toolbarAdd)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
    currentDate = simpleDateFormat.format(Date())

    info("Hillfort Activity started..")

    app = application as MainApp

    if (intent.hasExtra("loggedInUser")) {
      loggedInUser = intent.extras?.getParcelable<UserModel>("loggedInUser")!!
      info("User:")
      info(loggedInUser)
    }

    var edit = false

    if (intent.hasExtra("hillfort_edit")) {
      edit = true
      hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
      hillfortTitle.setText(hillfort.title)
      description.setText(hillfort.description)
      btnAdd.setText(R.string.save_hillfort)
      hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
      val imageVars = arrayOf(hillfortImage, hillfortImage2, hillfortImage3, hillfortImage4)
      var i = 0
      while ( i < hillfort.images.size) {
        imageVars[i+1].setImageBitmap((readImageFromPath(this, hillfort.images[i])))
        i++
      }
      if (hillfort.image != null) {
        chooseImage.setText(R.string.change_hillfort_image)
      }
    }

    btnAdd.setOnClickListener() {
      hillfort.title = hillfortTitle.text.toString()
      hillfort.description = description.text.toString()
      hillfort.contributor = loggedInUser?.id!!
      if (hillfort.title.isEmpty()) {
        toast(R.string.enter_hillfort_title)
      } else {
        if (edit) {
          app.hillforts.update(hillfort.copy())
        } else {
          app.hillforts.create(hillfort.copy())
        }
      }
      info("add Button Pressed: $hillfortTitle")
      setResult(AppCompatActivity.RESULT_OK)
      if (hillfort.title.isNotEmpty()) {
        finish()
      }
    }

    chooseImage.setOnClickListener {
//      showImagePicker(this, IMAGE_REQUEST)
      showMultipleImagesPicker(this, MULTIPLE_IMAGE_REQUEST)
    }

    hillfortImage.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST)
    }

    hillfortImage2.setOnClickListener {
      showImagePicker(this, IMAGE_CHANGE_2)
    }

    hillfortImage3.setOnClickListener {
      showImagePicker(this, IMAGE_CHANGE_3)
    }

    hillfortImage4.setOnClickListener {
      showImagePicker(this, IMAGE_CHANGE_4)
    }

    hillfortLocation.setOnClickListener {
      val location = Location(52.245696, -7.139102, 15f)
      if (hillfort.zoom != 0f) {
        location.lat =  hillfort.lat
        location.lng = hillfort.lng
        location.zoom = hillfort.zoom
      }
      startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort, menu)
    val menuUser: MenuItem = menu?.findItem(R.id.menu_user)!!
    menuUser.setTitle(loggedInUser?.userName)
    val menuCheck: MenuItem = menu?.findItem(R.id.item_mark_visited)
    if (hillfort.isVisited) menuCheck.setChecked(true)
    val item: MenuItem = menu.findItem(R.id.item_delete)
    if (!intent.hasExtra("hillfort_edit")) {
      item.setVisible(false)
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_cancel -> {
        finish()
      }
      R.id.item_delete -> {
        app.hillforts.delete(hillfort)
        finish()
      }
      R.id.item_logout -> {
        loggedInUser = null
        startActivity(intentFor<LoginActivity>())
      }
      R.id.item_mark_visited -> {
        if (hillfort.isVisited) {
          item.setChecked(false)
          hillfort.isVisited = false
        } else {
          item.setChecked(true)
          hillfort.isVisited = true
          hillfort.dateVisited = currentDate
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {
      IMAGE_REQUEST -> {
        if (data != null) {
          hillfort.image = data.getData().toString()
          hillfortImage.setImageBitmap(readImage(this, resultCode, data, null))
          chooseImage.setText(R.string.change_hillfort_image)
        }
      }
      IMAGE_CHANGE_2 -> {
        if (data != null) {
          hillfort.images[0] = data.getData().toString()
          hillfortImage2.setImageBitmap(readImage(this, resultCode, data, null))
        }
      }
      IMAGE_CHANGE_3 -> {
        if (data != null) {
          hillfort.images[1] = data.getData().toString()
          hillfortImage3.setImageBitmap(readImage(this, resultCode, data, null))
        }
      }
      IMAGE_CHANGE_4 -> {
        if (data != null) {
          hillfort.images[2] = data.getData().toString()
          hillfortImage4.setImageBitmap(readImage(this, resultCode, data, null))
        }
      }
      MULTIPLE_IMAGE_REQUEST -> {
        if (data != null) {
          if (data.data != null) {
            hillfort.image = data.getData().toString()
            hillfortImage.setImageBitmap(readImage(this, resultCode, data, null))
            chooseImage.setText(R.string.change_hillfort_image)
          } else {
            var clipData: ClipData = data.clipData!!
            var clipArray: MutableList<String> = ArrayList()
            var i = 0
            while (i < clipData.itemCount) {
              clipArray.add(clipData.getItemAt(i).uri.toString())
              i++
            }
            if (clipArray.size > 0) {
              for (image in clipArray) {
                hillfort.images.add(image)
              }
            }
            val imageVars = arrayOf(hillfortImage, hillfortImage2, hillfortImage3, hillfortImage4)
            i = 0
            while (i < hillfort.images.size) {
              if (hillfortImage.drawable == null) { // Check if hillfortImage is already set
                imageVars[i].setImageBitmap(readImage(this, resultCode, data, i))
              } else {
                imageVars[i+1].setImageBitmap(readImage(this, resultCode, data, i))
              }
              i++
            }
            info(clipArray)
          }
        }
      }
      LOCATION_REQUEST -> {
        if (data != null) {
          val location = data.extras?.getParcelable<Location>("location")!!
          hillfort.lat = location.lat
          hillfort.lng = location.lng
          hillfort.zoom = location.zoom
        }
      }
    }
  }
}