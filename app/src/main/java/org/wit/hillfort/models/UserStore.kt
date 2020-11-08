package org.wit.hillfort.models

interface UserStore {
  fun findAll(): List<UserModel>
  fun findOne(user: UserModel): UserModel?
  fun create(user: UserModel)
  fun update(user: UserModel)
  fun delete(user: UserModel)
  fun validate(user: UserModel): UserModel?
}