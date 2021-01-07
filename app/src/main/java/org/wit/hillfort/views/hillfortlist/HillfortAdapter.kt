package org.wit.hillfort.views.hillfortlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.jetbrains.anko.colorAttr
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.hillfort.HillfortPresenter

interface HillfortListener {
  fun onHillfortClick(hillfort: HillfortModel)
  fun onHillfortCheck(hillfort: HillfortModel)
}

class HillfortAdapter constructor(
  private var hillforts: List<HillfortModel>,
  private val listener: HillfortListener
) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
    return MainHolder(
      LayoutInflater.from(parent?.context).inflate(
        R.layout.card_hillfort,
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: MainHolder, position: Int) {
    val hillfort = hillforts[holder.adapterPosition]
    holder.bind(hillfort, listener)
  }

  override fun getItemCount(): Int = hillforts.size

  class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(hillfort: HillfortModel, listener: HillfortListener) {
      itemView.hillfortTitle.text = hillfort.title
      itemView.description.text = hillfort.description
      itemView.ratingBar.rating = hillfort.rating.toFloat()
      itemView.favoriteStar.isChecked = hillfort.favorite
      if (hillfort.isVisited) {
        itemView.isVisited.text = "Visited on\n" + hillfort.dateVisited
      }
      if (itemView.favoriteStar.isChecked) {
        hillfort.favorite = true
      }
      if (hillfort.images.isNotEmpty()) {
//        itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.images[0]))
        Glide.with(itemView.context).load(hillfort.images[0]).into(itemView.imageIcon)
      }
      itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
      itemView.favoriteStar.setOnClickListener {
        hillfort.favorite = !hillfort.favorite
        listener.onHillfortCheck(hillfort)
      }
    }
  }
}