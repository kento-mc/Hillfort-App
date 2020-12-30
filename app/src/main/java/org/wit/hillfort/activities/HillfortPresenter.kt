package org.wit.hillfort.activities

import android.content.ClipData
import android.content.Intent
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImage
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.helpers.showMultipleImagesPicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.UserModel

class HillfortPresenter(val view: HillfortView) {

  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2
  val MULTIPLE_IMAGE_REQUEST = 3
  val IMAGE_CHANGE_2 = 4
  val IMAGE_CHANGE_3 = 5
  val IMAGE_CHANGE_4 = 6

  var hillfort = HillfortModel()
  var location = Location(52.245696, -7.139102, 15f)
  var app: MainApp
  var loggedInUser : UserModel? = null
  var edit = false;

  init {
    app = view.application as MainApp
    if (view.intent.hasExtra("hillfort_edit")) {
      edit = true
      loggedInUser = view.intent.extras?.getParcelable<UserModel>("loggedInUser")!!
      view.info("User:")
      view.info(loggedInUser)
      hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
      view.showHillfort(hillfort)
    }
  }

  fun doAddOrSave(title: String, description: String, id: Long) {
    hillfort.title = title
    hillfort.description = description
    hillfort.contributor = id
    if (edit) {
      app.hillforts.update(hillfort)
    } else {
      app.hillforts.create(hillfort)
    }
    view.finish()
  }

  fun doCancel() {
    view.finish()
  }

  fun doDelete() {
    app.hillforts.delete(hillfort)
    view.finish()
  }

  fun doSelectImageOne() {
    showImagePicker(view, IMAGE_REQUEST)
  }

  fun doSelectImageTwo() {
    showImagePicker(view, IMAGE_REQUEST)
  }

  fun doSelectImageThree() {
    showImagePicker(view, IMAGE_REQUEST)
  }

  fun doSelectImageFour() {
    showImagePicker(view, IMAGE_REQUEST)
  }

  fun doSelectMultiImage() {
    showMultipleImagesPicker(view, MULTIPLE_IMAGE_REQUEST)
  }

  fun doSetLocation() {
    val location = Location(52.245696, -7.139102, 15f)
    if (hillfort.zoom != 0f) {
      location.lat =  hillfort.lat
      location.lng = hillfort.lng
      location.zoom = hillfort.zoom
    }
    view.startActivityForResult(view.intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
  }

  fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      IMAGE_REQUEST -> {
        if (data != null) {
          hillfort.image = data.getData().toString()
          view.hillfortImage.setImageBitmap(readImage(view, resultCode, data, null))
          view.chooseImage.setText(R.string.change_hillfort_image)
        }
      }
      IMAGE_CHANGE_2 -> {
        if (data != null) {
          hillfort.images[0] = data.getData().toString()
          view.hillfortImage2.setImageBitmap(readImage(view, resultCode, data, null))
        }
      }
      IMAGE_CHANGE_3 -> {
        if (data != null) {
          hillfort.images[1] = data.getData().toString()
          view.hillfortImage3.setImageBitmap(readImage(view, resultCode, data, null))
        }
      }
      IMAGE_CHANGE_4 -> {
        if (data != null) {
          hillfort.images[2] = data.getData().toString()
          view.hillfortImage4.setImageBitmap(readImage(view, resultCode, data, null))
        }
      }
      MULTIPLE_IMAGE_REQUEST -> {
        if (data != null) {
          if (data.data != null) {
            hillfort.image = data.getData().toString()
            view.hillfortImage.setImageBitmap(readImage(view, resultCode, data, null))
            view.chooseImage.setText(R.string.change_hillfort_image)
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
            val imageVars = arrayOf(view.hillfortImage, view.hillfortImage2, view.hillfortImage3, view.hillfortImage4)
            i = 0
            while (i < hillfort.images.size) {
              if (view.hillfortImage.drawable == null) { // Check if hillfortImage is already set
                imageVars[i].setImageBitmap(readImage(view, resultCode, data, i))
              } else {
                imageVars[i+1].setImageBitmap(readImage(view, resultCode, data, i))
              }
              i++
            }
            view.info(clipArray)
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