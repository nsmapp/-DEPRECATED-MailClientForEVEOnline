package by.nepravskysm.domain.repository.utils

import by.nepravskysm.domain.entity.subentity.Recipient

interface IdsRepository {

    suspend fun getRecepientList(nameList: Array<String>) : List<Recipient>
}