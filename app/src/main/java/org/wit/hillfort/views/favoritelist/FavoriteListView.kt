package org.wit.hillfort.views.favoritelist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.card_hillfort.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.utils.SwipeToDeleteCallback
import org.wit.hillfort.utils.SwipeToEditCallback
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.hillfortlist.HillfortAdapter
import org.wit.hillfort.views.hillfortlist.HillfortListPresenter
import org.wit.hillfort.views.hillfortlist.HillfortListener

class FavoriteListView : BaseView(),
  FavoriteListener, AnkoLogger {

  lateinit var presenter: FavoriteListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    setSupportActionBar(toolbar)
    super.init(toolbar, true)

    presenter = initPresenter(FavoriteListPresenter(this)) as FavoriteListPresenter

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager

    presenter.getHillforts()
    setSwipeRefresh()

    val swipeEditHandler = object : SwipeToEditCallback(this) {
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val hillfortSwiped = presenter.app.hillforts.findByFbId(viewHolder.itemView.tag as String)
        onHillfortClick(hillfortSwiped!!)
      }
    }
    val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
    itemTouchEditHelper.attachToRecyclerView(recyclerView)

    val swipeDeleteHandler = object : SwipeToDeleteCallback(this) {
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val adapter = recyclerView.adapter as FavoriteAdapter
        deleteFromList(viewHolder.itemView.tag as String)
        adapter.removeAt(viewHolder.adapterPosition)
      }
    }
    val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
    itemTouchDeleteHelper.attachToRecyclerView(recyclerView)
  }

  override fun showHillforts(hillforts: ArrayList<HillfortModel>) {
    recyclerView.adapter = FavoriteAdapter(hillforts, this)
    recyclerView.adapter?.notifyDataSetChanged()
    checkSwipeRefresh()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)

    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
      toolbar.title = "Favorites: ${user.email}"
    }

    val item: MenuItem = menu!!.findItem(R.id.item_add)
    item.isVisible = false

    val item2: MenuItem = menu!!.findItem(R.id.item_favorites)
    item2.isVisible = false

    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_map -> presenter.doShowHillfortsMap()
      R.id.item_settings -> presenter.doShowSettings()
      R.id.item_logout -> presenter.doLogout()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    presenter.doEditHillfort(hillfort)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.getHillforts()
    super.onActivityResult(requestCode, resultCode, data)
  }

  fun setSwipeRefresh() {
    swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
      override fun onRefresh() {
        swiperefresh.isRefreshing = true
        presenter.doCheckForUpdates()
      }
    })
  }

  fun checkSwipeRefresh() {
    if (swiperefresh.isRefreshing) swiperefresh.isRefreshing = false
  }

  fun deleteFromList(fbId: String) {
    presenter.doDeleteFromList(fbId)
  }
}
