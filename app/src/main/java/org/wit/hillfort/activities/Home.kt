package org.wit.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.wit.hillfort.R
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.firebase.HillfortFireStore
import org.wit.hillfort.views.hillfortlist.HillfortListFragment

class Home : AppCompatActivity(),
  NavigationView.OnNavigationItemSelectedListener {

  lateinit var ft: FragmentTransaction
  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.home)
    setSupportActionBar(toolbar)
    app = application as MainApp

    var fireStore: HillfortFireStore? = null

    if (app.hillforts is HillfortFireStore) {
      fireStore = app.hillforts as HillfortFireStore
    }
    if (fireStore != null) {
      fireStore!!.fetchHillforts {}
    }
      app.currentUser = FirebaseAuth.getInstance().currentUser!!


    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action",
        Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }

    navView.setNavigationItemSelectedListener(this)
    val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
      R.string.navigation_drawer_open, R.string.navigation_drawer_close
    )
    drawerLayout.addDrawerListener(toggle)
    toggle.syncState()

    navView.getHeaderView(0).nav_header_email.text = app.currentUser?.email

    ft = supportFragmentManager.beginTransaction()

    val fragment = HillfortListFragment.newInstance()
    ft.replace(R.id.homeFrame, fragment)
    ft.commit()
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {

    when (item.itemId) {
//      R.id.nav_donate ->
//        navigateTo(DonateFragment.newInstance())
//      R.id.nav_report ->
//        navigateTo(ReportFragment.newInstance())
//      R.id.nav_aboutus ->
//        navigateTo(AboutUsFragment.newInstance())
//      R.id.nav_sign_out ->
//        signOut()

      else -> toast("You Selected Something Else")
    }
    drawerLayout.closeDrawer(GravityCompat.START)
    return true
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_home, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
//      R.id.item_add -> presenter.doAddHillfort()
//      R.id.item_map -> presenter.doShowHillfortsMap()
//      R.id.item_favorites -> presenter.doShowFavorites()
//      R.id.item_settings -> presenter.doShowSettings()
//      R.id.item_logout -> presenter.doLogout()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START))
      drawerLayout.closeDrawer(GravityCompat.START)
    else
      super.onBackPressed()
  }

  private fun navigateTo(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
      .replace(R.id.homeFrame, fragment)
      .addToBackStack(null)
      .commit()
  }

  private fun signOut()
  {
//    app.auth.signOut()
//    startActivity<Login>()
    finish()
  }
}