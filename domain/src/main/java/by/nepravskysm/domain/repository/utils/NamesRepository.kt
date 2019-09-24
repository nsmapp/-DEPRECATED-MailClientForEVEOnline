package by.nepravskysm.domain.repository.utils


interface NamesRepository {

    suspend fun getNameMap(idArray: Array<Long>):HashMap<Long, String>
}