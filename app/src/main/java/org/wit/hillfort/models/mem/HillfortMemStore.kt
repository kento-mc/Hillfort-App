//package org.wit.hillfort.models.mem
//
//import org.jetbrains.anko.AnkoLogger
//import org.jetbrains.anko.info
//import org.wit.hillfort.models.HillfortModel
//import org.wit.hillfort.models.HillfortStore
//import org.wit.hillfort.models.UserModel
//
//var lastId = 0L
//
//internal fun getId(): Long {
//  return lastId++
//}
//
//class HillfortMemStore : HillfortStore, AnkoLogger {
//
//  val hillforts = ArrayList<HillfortModel>()
//
//  override fun findAll(): MutableList<HillfortModel> {
//    return hillforts
//  }
//
//  override fun findAllByUser(user: UserModel): List<HillfortModel> {
//    return hillforts.filter { it.contributor == user.id }.toMutableList()
//  }
//
//  override fun create(hillfort: HillfortModel) {
//    hillfort.id = getId()
//    hillforts.add(hillfort)
//    logAll()
//  }
//
//  override fun update(hillfort: HillfortModel) {
//    var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
//    if (foundHillfort != null) {
//      foundHillfort.title = hillfort.title
//      foundHillfort.description = hillfort.description
//      foundHillfort.contributor = hillfort.contributor
//      foundHillfort.isVisited = hillfort.isVisited
//      foundHillfort.dateVisited = hillfort.dateVisited
////      foundHillfort.image = hillfort.image
//      foundHillfort.images = hillfort.images
//      foundHillfort.location = hillfort.location
//      logAll()
//    }
//  }
//
//  override fun delete(hillfort: HillfortModel) {
//    hillforts.remove(hillfort)
//  }
//
//  fun logAll() {
//    hillforts.forEach{ info("${it}") }
//  }
//
//  override fun findById(id:Long) : HillfortModel? {
//    val foundHillfort: HillfortModel? = hillforts.find { it.id == id }
//    return foundHillfort
//  }
//
//  override fun clear() {
//    hillforts.clear()
//  }
//}