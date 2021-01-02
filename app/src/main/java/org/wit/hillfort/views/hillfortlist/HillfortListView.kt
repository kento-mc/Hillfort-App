package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.BaseView

class HillfortListView : BaseView(),
  HillfortListener, AnkoLogger {

  lateinit var presenter: HillfortListPresenter
  var loggedInUser: FirebaseUser? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    setSupportActionBar(toolbar)

    presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter
    loggedInUser = presenter.app.auth?.currentUser

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
//    presenter.getHillforts(loggedInUser!!)
    presenter.getHillforts()
//    recyclerView.adapter =
//      HillfortAdapter(
//        presenter.getHillforts(
//          loggedInUser!!
//        ), this
//      )
//    recyclerView.adapter?.notifyDataSetChanged()
  }

//  override fun onResume() {
//    recyclerView.adapter?.notifyDataSetChanged()
//    return super.onResume()
//  }

  override fun showHillforts(hillforts: List<HillfortModel>) {
    recyclerView.adapter = HillfortAdapter(hillforts, this)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    val menuUser: MenuItem = menu?.findItem(R.id.menu_user)!!
    menuUser.setTitle(loggedInUser?.email)

    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_add -> presenter.doAddHillfort(loggedInUser!!)
      R.id.item_map -> presenter.doShowHillfortsMap(loggedInUser!!)
      R.id.item_settings -> presenter.doShowSettings(loggedInUser!!)
      R.id.item_logout -> {
//        loggedInUser = null
        presenter.doLogout()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    presenter.doEditHillfort(hillfort, loggedInUser!!)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    val layoutManager = LinearLayoutManager(this)
//    recyclerView.layoutManager = layoutManager
//    recyclerView.adapter =
//      HillfortAdapter(
//        presenter.getHillforts(
//          loggedInUser!!
//        ), this
//      )
//    recyclerView.adapter?.notifyDataSetChanged()

//    presenter.getHillforts(loggedInUser!!)
    presenter.getHillforts()
    super.onActivityResult(requestCode, resultCode, data)
  }
}
