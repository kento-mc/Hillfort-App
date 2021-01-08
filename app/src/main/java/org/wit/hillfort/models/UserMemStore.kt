//package org.wit.hillfort.models
//
//import org.jetbrains.anko.AnkoLogger
//import org.jetbrains.anko.info
//
//var lastUserId = 0L
//
//internal fun getUserId(): Long {
//  return lastUserId++
//}
//
//class UserMemStore : UserStore, AnkoLogger {
//
//  val users = ArrayList<UserModel>()
//
//  override fun findAll(): List<UserModel> {
//    return users
//  }
//
//  override fun findOne(user: UserModel): UserModel? {
//    var foundUser: UserModel? = users.find { p -> p.id == user.id }
//    if (foundUser != null) {
//      return foundUser
//    } else return null
//  }
//
//  override fun findOneByID(id: Long): UserModel? {
//    var foundUser: UserModel? = users.find { p -> p.id == id }
//    if (foundUser != null) {
//      return foundUser
//    } else return null
//  }
//
//  override fun findOneByEmail(user: UserModel): UserModel? {
//    var foundUser: UserModel? = users.find { p -> p.email == user.email }
//    if (foundUser != null) {
//      return foundUser
//    } else return null
//  }
//
//  override fun create(user: UserModel): Boolean {
//    var foundUser: UserModel? = users.find { p -> p.email == user.email }
//    if (foundUser == null) {
//      user.id = getUserId()
//      users.add(user)
//      logAll()
//      return true
//    } else return false
//  }
//
//  override fun update(user: UserModel) {
//    var foundUser: UserModel? = users.find { p -> p.id == user.id }
//    if (foundUser != null) {
//      foundUser.email = user.email
//      foundUser.password = user.password
//      foundUser.userName = user.userName
//      logAll()
//    }
//  }
//
//  override fun delete(user: UserModel) {
//    users.remove(user)
//  }
//
//  override fun validate(user: UserModel): UserModel? {
//    var foundUser: UserModel? = users.find { p ->
//      p.email == user.email
//    }
//    if (foundUser != null && user.password == foundUser.password) {
//      return foundUser
//    } else return null
//  }
//
//  fun logAll() {
//    users.forEach{ info("${it}") }
//  }
//}