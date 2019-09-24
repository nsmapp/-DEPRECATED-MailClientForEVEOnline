package by.nepravskysm.rest.repoimpl.esi

import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.rest.api.EsiManager
import by.nepravskysm.rest.entity.response.NamesResponse

class NamesRepoImpl(private val esiManager: EsiManager) : NamesRepository{


    override suspend fun getNameMap(idArray: Array<Long>): HashMap<Long, String> {

        val names : List<NamesResponse> = esiManager.postIdsToName(idArray).await()
        val nameMap = mutableMapOf<Long, String>()

        for ( name in names){
            nameMap[name.id] = name.name
        }

        return HashMap(nameMap)
    }

}