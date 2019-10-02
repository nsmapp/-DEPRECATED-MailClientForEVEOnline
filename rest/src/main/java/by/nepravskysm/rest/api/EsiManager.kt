package by.nepravskysm.rest.api

import by.nepravskysm.rest.entity.request.MailMetadataRequest
import by.nepravskysm.rest.entity.request.MailRequest
import by.nepravskysm.rest.entity.response.*
import kotlinx.coroutines.Deferred

class EsiManager{

    private val esiApi: EsiApi = createRetrofit(
        "https://esi.evetech.net/",
        30
    )
        .create(EsiApi::class.java)

    fun getCharecterInfo(accessToken :String) : Deferred<CharacterInfoResponse> {

        return esiApi.getCharacterInfo("Bearer $accessToken",
            "tranquility")
    }


    fun getMailsHeaders(accessToken :String,
                        characterId :Long): Deferred<List<MailHeaderResponse>>{
        return esiApi.getFirst50MailsHeader("Bearer $accessToken",
            characterId,
            "tranquility")
    }

    fun getMailsHeaders(accessToken :String,
                        characterId :Long,
                        minHeaderId: Long): Deferred<List<MailHeaderResponse>>{
        return esiApi.getMailsHeaderBeforeId("Bearer $accessToken",
            characterId,
            "tranquility",
            minHeaderId)
    }

    fun getMail(accessToken: String,
                characterId: Long,
                mailId: Long) : Deferred<MailResponse>{

        return esiApi.getMail("Bearer $accessToken",
            characterId,
            mailId)
    }


    fun postMail(accessToken: String,
                 characterId: Long,
                 mail: MailRequest) : Deferred<Long>{

        return esiApi.sendMail("Bearer $accessToken",
            characterId,
            mail)
    }

    fun postIdsToName(idArray: Array<Long>) : Deferred<List<NamesResponse>>{
        return esiApi.getNameList(idArray)
    }

    fun postNameToIds(nameArray: Array<String>) :Deferred<idsResponse>{
        return  esiApi.getIdList(nameArray)
    }


    fun putMailMetadata(mailMetadata: MailMetadataRequest,
                        accessToken :String,
                        characterId :Long,
                        mailId :Long):Deferred<Unit>{

        return esiApi.putMailMetadata(mailMetadata,
            "Bearer $accessToken",
            characterId,
            mailId)

    }

    fun deleteMail(accessToken :String,
                   characterId :Long,
                   mailId :Long):Deferred<Unit>{

        return esiApi.deleteMail("Bearer $accessToken",
            characterId,
            mailId)
    }

    fun getMailList(accessToken :String,
                    characterId :Long):Deferred<List<MailingListResponse>>{
        return esiApi.getMailingList("Bearer $accessToken",
            characterId)
    }

}