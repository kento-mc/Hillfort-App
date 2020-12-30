package org.wit.hillfort.views.hillfort

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
import org.wit.hillfort.activities.LoginActivity
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import java.text.SimpleDateFormat
import java.util.*

class HillfortView : AppCompatActivity(), AnkoLogger {

  lateinit var presenter: HillfortPresenter
  var hillfort = HillfortModel()
  lateinit var app : MainApp
  var loggedInUser : UserModel? = null
  var currentDate: String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)
    toolbarAdd.title = title
    setSupportActionBar(toolbarAdd)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
    currentDate = simpleDateFormat.format(Date())

    presenter = HillfortPresenter(this)

    app = application as MainApp

    if (intent.hasExtra("loggedInUser")) {
      loggedInUser = intent.extras?.getParcelable<UserModel>("loggedInUser")!!
      info("User:")
      info(loggedInUser)
    }

    var edit = false

//    if (intent.hasExtra("hillfort_edit")) {
//      edit = true
//      hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
//      hillfortTitle.setText(hillfort.title)
//      description.setText(hillfort.description)
//      btnAdd.setText(R.string.save_hillfort)
//      hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
//      val imageVars = arrayOf(hillfortImage, hillfortImage2, hillfortImage3, hillfortImage4)
//      var i = 0
//      while ( i < hillfort.images.size) {
//        imageVars[i+1].setImageBitmap((readImageFromPath(this, hillfort.images[i])))
//        i++
//      }
//      if (hillfort.image != null) {
//        chooseImage.setText(R.string.change_hillfort_image)
//      }
//    }

    btnAdd.setOnClickListener() {
      if (hillfortTitle.toString().isEmpty()) {
        toast(R.string.enter_hillfort_title)
      } else {
        info(hillfortTitle.text.toString())
        info(description.text.toString())
        info(loggedInUser!!.id)
        presenter.doAddOrSave(hillfortTitle.text.toString(), description.text.toString(), loggedInUser!!.id )
      }
//      if (hillfort.title.isNotEmpty()) {
//        finish()
//      }
    }

    chooseImage.setOnClickListener { presenter.doSelectMultiImage() }

    hillfortImage.setOnClickListener { presenter.doSelectImageOne() }

    hillfortImage2.setOnClickListener { presenter.doSelectImageTwo() }

    hillfortImage3.setOnClickListener { presenter.doSelectImageThree() }

    hillfortImage4.setOnClickListener { presenter.doSelectImageFour() }

    hillfortLocation.setOnClickListener { presenter.doSetLocation() }
  }

  fun showHillfort(hillfort: HillfortModel) {
    hillfortTitle.setText(hillfort.title)
    description.setText(hillfort.description)
    btnAdd.setText(R.string.save_hillfort)
    hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
    if (hillfort.image != null) {
      chooseImage.setText(R.string.change_hillfort_image)
    }
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

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort, menu)

    // Show user in menu bar
    val menuUser: MenuItem = menu?.findItem(R.id.menu_user)!!
    menuUser.setTitle(loggedInUser?.userName)

    // Set checkmark status
    val menuCheck: MenuItem = menu?.findItem(R.id.item_mark_visited)
    if (hillfort.isVisited) menuCheck.setChecked(true)

    // Hide delete option on first creation of hillfort
    val item: MenuItem = menu.findItem(R.id.item_delete)
    if (!presenter.edit) {
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
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }
}