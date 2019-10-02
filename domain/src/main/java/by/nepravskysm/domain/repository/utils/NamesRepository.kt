package by.nepravskysm.domain.repository.utils

interface NamesRepository {

    suspend fun getNameMap(idArray: List<Long>):HashMap<Long, String>
}