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

class HillfortListActivity : AppCompatActivity(), HillfortListener, AnkoLogger {

  lateinit var app: MainApp
  var loggedInUser: UserModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    app = application as MainApp

    if (intent.hasExtra("loggedInUser")) {
      loggedInUser = intent.extras?.getParcelable<UserModel>("loggedInUser")!!
      info("User:")
      info(loggedInUser)
    }

      toolbar.title = title
    setSupportActionBar(toolbar)

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    loadHillforts(loggedInUser!!)
  }

  private fun loadHillforts(user: UserModel) {
    showHillforts(app.hillforts.findAllByUser(user))
  }

  fun showHillforts (hillforts: List<HillfortModel>) {
    recyclerView.adapter = HillfortAdapter(hillforts, this)
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
      R.id.item_add -> startActivityForResult(intentFor<HillfortView>().putExtra("loggedInUser", loggedInUser), 0)
      R.id.item_map -> startActivity<HillfortMapsActivity>()
      R.id.item_settings -> startActivityForResult(intentFor<SettingsActivity>().putExtra("loggedInUser", loggedInUser), 0)
      R.id.item_logout -> {
        loggedInUser = null
        startActivity(intentFor<LoginActivity>())
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    var intent = intentFor<HillfortView>().putExtra("loggedInUser", loggedInUser)
    intent.putExtra("hillfort_edit", hillfort)
    startActivityForResult(intent, 0)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    loadHillforts(loggedInUser!!)
    super.onActivityResult(requestCode, resultCode, data)
  }
}
