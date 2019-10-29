package by.nepravskysm.rest.repoimpl.esi

import by.nepravskysm.domain.entity.subentity.Recipient
import by.nepravskysm.domain.repository.utils.IdsRepository
import by.nepravskysm.rest.api.EsiManager

class IdsRepoImpl(private val esiManager: EsiManager) : IdsRepository{



    override suspend fun getRecepientList(nameList: Array<String>): List<Recipient> {
        val namesLists = esiManager.postNameToIds(nameList).await()
        val recipients = mutableListOf<Recipient>()

        if (namesLists.alliances != null){
            for( item in namesLists.alliances){
                recipients.add(Recipient(item.id, "alliance"))
            }
        }

        if(namesLists.corporations != null){
            for( item in namesLists.corporations){
                recipients.add(Recipient(item.id, "corporation"))
            }
        }

        if(namesLists.characters != null){
            for( item in namesLists.characters){
                recipients.add(Recipient(item.id, "character"))
            }
        }
        return recipients
    }
}