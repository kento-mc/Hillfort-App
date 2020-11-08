package org.wit.hillfort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.*

val JSON_USER_FILE = "users.json"
//val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val userListType = object : TypeToken<java.util.ArrayList<UserModel>>() {}.type

//fun generateRandomId(): Long {
//  return Random().nextLong()
//}

class UserJSONStore : UserStore, AnkoLogger {

  val context: Context
  var users = mutableListOf<UserModel>()

  constructor (context: Context) {
    this.context = context
    if (exists(context, JSON_USER_FILE)) {
      deserialize()
    }
  }

  override fun findAll(): MutableList<UserModel> {
    return users
  }

  override fun findOne(user: UserModel): UserModel? {
    var foundUser: UserModel? = users.find { p -> p.id == user.id }
    if (foundUser != null) {
      return foundUser
    } else return null
  }

  override fun findOneByEmail(user: UserModel): UserModel? {
    var foundUser: UserModel? = users.find { p -> p.email == user.email }
    if (foundUser != null) {
      return foundUser
    } else return null
  }

  override fun create(user: UserModel): Boolean {
    var foundUser: UserModel? = users.find { p -> p.email == user.email }
    if (foundUser == null) {
      user.id = generateRandomId()
      users.add(user)
      serialize()
      return true
    } else return false
  }


  override fun update(user: UserModel) {
    var foundUser: UserModel? = users.find { p -> p.id == user.id }
    if (foundUser != null) {
      foundUser.email = user.email
      foundUser.password = user.password
      foundUser.userName = user.userName
      serialize()
    }
  }

  override fun delete(user: UserModel) {
    users.remove(user)
    serialize()
  }

  override fun validate(user: UserModel): UserModel? {
    var foundUser: UserModel? = users.find { p ->
      p.email == user.email
    }
    if (foundUser != null && user.password == foundUser.password) {
      return foundUser
    } else return null
  }

  private fun serialize() {
    val jsonString = gsonBuilder.toJson(users, userListType)
    write(context, JSON_USER_FILE, jsonString)
  }

  private fun deserialize() {
    val jsonString = read(context, JSON_USER_FILE)
    users = Gson().fromJson(jsonString, userListType)
  }
}