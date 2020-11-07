package org.wit.hillfort.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastUserId = 0L

internal fun getUserId(): Long {
  return lastUserId++
}

class UserMemStore : UserStore, AnkoLogger {

  val users = ArrayList<UserModel>()

  override fun findAll(): List<UserModel> {
    return users
  }

  override fun create(user: UserModel) {
    user.id = getUserId()
    users.add(user)
    logAll()
  }

  override fun update(user: UserModel) {
    var foundUser: UserModel? = users.find { p -> p.id == user.id }
    if (foundUser != null) {
      foundUser.email = user.email
      foundUser.password = user.password
      foundUser.userName = user.userName
      logAll()
    }
  }

  override fun delete(user: UserModel) {
    users.remove(user)
  }

  fun logAll() {
    users.forEach{ info("${it}") }
  }
}