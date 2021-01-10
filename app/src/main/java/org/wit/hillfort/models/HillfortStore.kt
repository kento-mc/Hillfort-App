package org.wit.hillfort.models

interface HillfortStore {
    fun findAll(): ArrayList<HillfortModel>
    fun findAllByUser(user: UserModel): ArrayList<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    fun deleteFromList(hillfort: HillfortModel)
    fun findById(id: Long): HillfortModel?
    fun findByFbId(fbId: String): HillfortModel?
    fun clear()
}