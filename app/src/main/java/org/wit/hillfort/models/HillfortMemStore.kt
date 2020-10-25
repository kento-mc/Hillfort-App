package org.wit.hillfort.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
  return lastId++
}

class HillfortMemStore : HillfortStore, AnkoLogger {

  val hillforts = ArrayList<HillfortModel>()

  override fun findAll(): List<HillfortModel> {
    return hillforts
  }

  override fun create(hillfort: HillfortModel) {
    hillfort.id = getId()
    hillforts.add(hillfort)
    logAll()
  }

  override fun update(hiillfort: HillfortModel) {
    var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hiillfort.id }
    if (foundHillfort != null) {
      foundHillfort.title = hiillfort.title
      foundHillfort.description = hiillfort.description
      foundHillfort.image = hiillfort.image

      logAll()
    }
  }

  fun logAll() {
    hillforts.forEach{ info("${it}") }
  }
}