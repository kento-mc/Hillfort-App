package org.wit.hillfort.views.hillfort

import android.content.ClipData
import android.content.Intent
import android.os.Parcelable
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.views.location.EditLocationView
import org.wit.hillfort.helpers.readImage
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.helpers.showMultipleImagesPicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.*

class HillfortPresenter(view: BaseView) : BasePresenter(view) {

  var hillfort = HillfortModel()
  var defaultLocation = Location(52.245696, -7.139102, 15f)
  var loggedInUser : UserModel? = null
  var edit = false;

  init {
    if (view.intent.hasExtra("hillfort_edit")) {
      edit = true
      loggedInUser = view.intent.extras?.getParcelable<UserModel>("loggedInUser")!!
      view.info("User:")
      view.info(loggedInUser)
      hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
      view.showHillfort(hillfort)
    }
  }

  fun doAddOrSave(title: String, description: String, id: Long, isVisited: Boolean = false, dateVisited: String = "") {
    hillfort.title = title
    hillfort.description = description
    hillfort.contributor = id
    hillfort.isVisited = isVisited
    hillfort.dateVisited = dateVisited

    if (edit) {
      app.hillforts.update(hillfort)
    } else {
      app.hillforts.create(hillfort)
    }
    view?.finish()
  }

  fun doCancel() {
    view?.finish()
  }

  fun doDelete() {
    app.hillforts.delete(hillfort)
    view?.finish()
  }

  fun doSelectImageOne() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }

  fun doSelectImageTwo() {
    view?.let {
      showImagePicker(view!!, IMAGE_CHANGE_2)
    }
  }

  fun doSelectImageThree() {
    view?.let {
      showImagePicker(view!!, IMAGE_CHANGE_3)
    }
  }

  fun doSelectImageFour() {
    view?.let {
      showImagePicker(view!!, IMAGE_CHANGE_4)
    }
  }

  fun doSelectMultiImage() {
    view?.let {
      showMultipleImagesPicker(view!!, MULTIPLE_IMAGE_REQUEST)
    }
  }

  fun doSetLocation() {
    var keyArray: Array<String> = arrayOf("location")
    var valueArray: Array<Parcelable?> = arrayOf(defaultLocation)
    if (edit == false) {
      view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, keyArray, valueArray)
    } else {
      view?.navigateTo(
        VIEW.LOCATION,
        LOCATION_REQUEST,
        keyArray,
        arrayOf(Location(hillfort.lat, hillfort.lng, hillfort.zoom))
      )
    }
  }

  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      IMAGE_REQUEST -> {
        if (data != null) {
          hillfort.image = data.data.toString()
          view?.showHillfort(hillfort)
        }
      }
      IMAGE_CHANGE_2 -> {
        if (data != null) {
          hillfort.images[0] = data.data.toString()
          view?.showHillfort(hillfort)
        }
      }
      IMAGE_CHANGE_3 -> {
        if (data != null) {
          hillfort.images[1] = data.data.toString()
          view?.showHillfort(hillfort)
        }
      }
      IMAGE_CHANGE_4 -> {
        if (data != null) {
          hillfort.images[2] = data.data.toString()
          view?.showHillfort(hillfort)
        }
      }
      MULTIPLE_IMAGE_REQUEST -> {
        if (data != null) {
          if (data.data != null) {
            hillfort.image = data.data.toString()
            view?.showHillfort(hillfort)
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
            val imageVars = arrayOf(view?.hillfortImage, view?.hillfortImage2, view?.hillfortImage3, view?.hillfortImage4)
            i = 0
            while (i < hillfort.images.size) {
              if (view?.hillfortImage?.drawable == null) { // Check if hillfortImage is already set
                imageVars[i]?.setImageBitmap(readImage(view!!, resultCode, data, i))
              } else {
                imageVars[i+1]?.setImageBitmap(readImage(view!!, resultCode, data, i))
              }
              i++
            }
            view?.info(clipArray)
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