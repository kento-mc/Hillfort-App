package org.wit.hillfort.models

interface HillfortStore {
    fun findAll(): List<HillfortModel>
    fun findAllByUser(user: UserModel): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun findById(id: Long): HillfortModel?
    fun findByFbId(fbId: String): HillfortModel?
    fun clear()
}