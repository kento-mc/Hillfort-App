//package org.wit.hillfort.models.json
//
//import android.content.Context
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import com.google.gson.reflect.TypeToken
//import org.jetbrains.anko.AnkoLogger
//import org.wit.hillfort.helpers.*
//import org.wit.hillfort.models.HillfortModel
//import org.wit.hillfort.models.HillfortStore
//import org.wit.hillfort.models.UserModel
//import java.util.*
//
//val JSON_FILE = "hillforts.json"
//val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
//val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type
//
//fun generateRandomId(): Long {
//  return Random().nextLong()
//}
//
//class HillfortJSONStore : HillfortStore, AnkoLogger {
//
//  val context: Context
//  var hillforts = mutableListOf<HillfortModel>()
//
//  constructor (context: Context) {
//    this.context = context
//    if (exists(context, JSON_FILE)) {
//      deserialize()
//    }
//  }
//
//  override fun findAll(): MutableList<HillfortModel> {
//    return hillforts
//  }
//
//  override fun findAllByUser(user: UserModel): MutableList<HillfortModel> {
//    return hillforts.filter { it.contributor == user.id }.toMutableList()
//  }
//
//  override fun create(hillfort: HillfortModel) {
//    hillfort.id = generateRandomId()
//    hillforts.add(hillfort)
//    serialize()
//  }
//
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
//      serialize()
//    }
//  }
//
//  override fun delete(hillfort: HillfortModel) {
//    hillforts.remove(hillfort)
//    serialize()
//  }
//
//  private fun serialize() {
//    val jsonString = gsonBuilder.toJson(hillforts,
//      listType
//    )
//    write(context, JSON_FILE, jsonString)
//  }
//
//  private fun deserialize() {
//    val jsonString = read(context, JSON_FILE)
//    hillforts = Gson().fromJson(jsonString,
//      listType
//    )
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