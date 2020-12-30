package org.wit.hillfort.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel

class HillfortListView : AppCompatActivity(), HillfortListener, AnkoLogger {

  lateinit var presenter: HillfortListPresenter
  var loggedInUser: UserModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    toolbar.title = title
    setSupportActionBar(toolbar)

    if (intent.hasExtra("loggedInUser")) {
      loggedInUser = intent.extras?.getParcelable<UserModel>("loggedInUser")!!
      info("User:")
      info(loggedInUser)
    }

    presenter = HillfortListPresenter(this)
    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
//    loadHillforts(loggedInUser!!)
    recyclerView.adapter =
      HillfortAdapter(presenter.getHillforts(loggedInUser!!), this)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    val menuUser: MenuItem = menu?.findItem(R.id.menu_user)!!
    menuUser.setTitle(loggedInUser?.userName)

    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_add -> presenter.doAddHillfort(loggedInUser!!)
      R.id.item_map -> presenter.doShowHillfortsMap()
      R.id.item_settings -> presenter.doShowSettings(loggedInUser!!)
      R.id.item_logout -> {
        loggedInUser = null
        presenter.doLogout()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    presenter.doEditHillfort(hillfort, loggedInUser!!)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    recyclerView.adapter?.notifyDataSetChanged()
    super.onActivityResult(requestCode, resultCode, data)
  }
}
