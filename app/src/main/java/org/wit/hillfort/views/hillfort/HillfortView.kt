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
import org.wit.hillfort.views.BaseView
import java.text.SimpleDateFormat
import java.util.*

class HillfortView : BaseView(), AnkoLogger {

  lateinit var presenter: HillfortPresenter
  var hillfort = HillfortModel()
  var loggedInUser : UserModel? = null
  var currentDate: String = ""
  var tempTitle: String = ""
  var tempDescription: String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
    currentDate = simpleDateFormat.format(Date())

    init(toolbarAdd)

    presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter

    if (intent.hasExtra("loggedInUser")) {
      loggedInUser = intent.extras?.getParcelable<UserModel>("loggedInUser")!!
      info("User:")
      info(loggedInUser)
    }

    chooseImage.setOnClickListener {
      tempTitle = hillfortTitle.text.toString()
      tempDescription = description.text.toString()
      presenter.doSelectMultiImage()
    }

    hillfortImage.setOnClickListener { presenter.doSelectImageOne() }

    hillfortImage2.setOnClickListener { presenter.doSelectImageTwo() }

    hillfortImage3.setOnClickListener { presenter.doSelectImageThree() }

    hillfortImage4.setOnClickListener { presenter.doSelectImageFour() }

    hillfortLocation.setOnClickListener { presenter.doSetLocation() }
  }

  override fun onResume() {
    if (!intent.hasExtra("hillfort_edit")) {
      hillfortTitle.setText(tempTitle)
      description.setText(tempDescription)
    }
    return super.onResume()
  }

  override fun showHillfort(hillfort: HillfortModel) {
    hillfortTitle.setText(hillfort.title)
    description.setText(hillfort.description)
    hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
//    if (hillfort.image != null) {
//      chooseImage.setText(R.string.change_hillfort_image)
//    }
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
          info(loggedInUser!!.id)
          presenter.doAddOrSave(
            hillfortTitle.text.toString(),
            description.text.toString(),
            loggedInUser!!.id,
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
}