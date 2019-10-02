package by.nepravskysm.rest.repoimpl.esi

import by.nepravskysm.domain.repository.utils.NamesRepository
import by.nepravskysm.rest.api.EsiManager
import by.nepravskysm.rest.entity.response.NamesResponse

class NamesRepoImpl(private val esiManager: EsiManager) : NamesRepository{

    override suspend fun getNameMap(idList: List<Long>): HashMap<Long, String> {

        val oldCharacter = mutableListOf<Long>()
        val oldCorporation = mutableListOf<Long>()
        val oldAlliance = mutableListOf<Long>()
        val newNames = mutableListOf<Long>()
        val newestNames = mutableListOf<Long>()

        val nameMap = mutableMapOf<Long, String>()

        for(id in idList){
            when(id){
                in 90_000_000 until 98_000_000 -> oldCharacter.add(id)
                in 98_000_000 until 99_000_000 -> oldCorporation.add(id)
                in 99_000_000 until 100_000_000 -> oldAlliance.add(id)
                in 100_000_000 until 2_100_000_000 -> newNames.add(id)
                in 2_100_000_000 until 2_147_483_647 -> newestNames.add(id)
            }
        }
        val names = mutableListOf<NamesResponse>()

        if(oldCharacter.isNotEmpty()){
            names.addAll(getNames(oldCharacter.toTypedArray()))
        }

        if(oldCorporation.isNotEmpty()){
            names.addAll(getNames(oldCorporation.toTypedArray()))
        }

        if(oldAlliance.isNotEmpty()){
            names.addAll(getNames(oldAlliance.toTypedArray()))
        }
        if(newNames.isNotEmpty()){
            names.addAll(getNames(newNames.toTypedArray()))
        }
        if(newestNames.isNotEmpty()){
            names.addAll(getNames(newestNames.toTypedArray()))
        }


        for ( name in names){
            nameMap[name.id] = name.name
        }

        return HashMap(nameMap)
    }

    private suspend fun getNames(idArray: Array<Long>):List<NamesResponse>{
       return esiManager.postIdsToName(idArray).await()
    }

}