package org.wit.hillfort.models

interface UserStore {
  fun findAll(): List<UserModel>
  fun findOne(user: UserModel): UserModel?
  fun findOneByID(id: Long): UserModel?
  fun findOneByEmail(uer: UserModel): UserModel?
  fun create(user: UserModel): Boolean
  fun update(user: UserModel)
  fun delete(user: UserModel)
  fun validate(user: UserModel): UserModel?
}