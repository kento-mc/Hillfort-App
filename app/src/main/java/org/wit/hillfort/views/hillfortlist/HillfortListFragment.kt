package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_hillfort_list.view.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

class HillfortListFragment : Fragment(),
  HillfortListener, AnkoLogger {

  lateinit var app: MainApp
  lateinit var presenter: HillfortListPresenter
  private lateinit var viewModel: ViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    app = activity?.application as MainApp

//////
//    init(toolbar, false)
//    presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

//    val layoutManager = LinearLayoutManager(this)
//    recyclerView.layoutManager = layoutManager
//    presenter.getHillforts()

//    if (intent.hasExtra("updateResult")) {
//      val afterUpdate: String = intent.extras?.getParcelable<Message>("updateResult")!!.message
//      toast(afterUpdate)
//    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val root = inflater.inflate(R.layout.activity_hillfort_list, container, false)
    activity?.title = "Placeholder"

    root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
    root.recyclerView.adapter = HillfortAdapter(app.hillforts.findAll(),this)

    setButtonListener(root)
    return root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
//    viewModel = ViewModelProvider(this).get(HillfortListViewModel::class.java)
    // TODO: Use the ViewModel
  }

  companion object {
    @JvmStatic
    fun newInstance() =
      HillfortListFragment().apply {
        arguments = Bundle().apply {}
      }
  }

  fun setButtonListener( layout: View) {
    // set button listeners
  }

  override fun onResume() {
    super.onResume()
//    toolbar.title = "${title}: ${presenter.app.currentUser.email}"
  }

//  fun init(toolbar: Toolbar, upEnabled: Boolean) {
//    toolbar.title = title
//    supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
//    val user = FirebaseAuth.getInstance().currentUser
//    if (user != null) {
//      toolbar.title = "${title}: ${user.email}"
//    }
//  }

  fun showHillforts(hillforts: List<HillfortModel>) {
    recyclerView.adapter = HillfortAdapter(hillforts, this)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      R.id.item_add -> presenter.doAddHillfort()
      R.id.item_map -> presenter.doShowHillfortsMap()
      R.id.item_favorites -> presenter.doShowFavorites()
      R.id.item_settings -> presenter.doShowSettings()
      R.id.item_logout -> presenter.doLogout()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onHillfortClick(hillfort: HillfortModel) {
    presenter.doEditHillfort(hillfort)
  }

  override fun onHillfortCheck(hillfort: HillfortModel) {
    presenter.doUpdateFavorite(hillfort)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.getHillforts()
    super.onActivityResult(requestCode, resultCode, data)
  }






  fun getHillforts() {
    doAsync {
      val hillforts = app.hillforts.findAll()
      uiThread {
        showHillforts(hillforts)
      }
    }
  }
}
