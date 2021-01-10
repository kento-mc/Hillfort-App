package org.wit.hillfort.views.favoritelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel

interface FavoriteListener {
  fun onHillfortClick(hillfort: HillfortModel)
}

class FavoriteAdapter constructor(
  private var hillforts: ArrayList<HillfortModel>,
  private val listener: FavoriteListener
) : RecyclerView.Adapter<FavoriteAdapter.MainHolder>() {

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

  fun removeAt(position: Int) {
    hillforts.removeAt(position)
    notifyItemRemoved(position)
  }

  class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(hillfort: HillfortModel, listener: FavoriteListener) {
      itemView.tag = hillfort.fbId
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
      itemView.favoriteStar.isClickable = false
    }
  }
}