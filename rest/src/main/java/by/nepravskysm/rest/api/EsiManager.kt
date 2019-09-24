package by.nepravskysm.rest.api

import by.nepravskysm.rest.entity.request.MailRequest
import by.nepravskysm.rest.entity.response.*
import kotlinx.coroutines.Deferred

class EsiManager{

    private val esiApi: EsiApi = createRetrofit(
        "https://esi.evetech.net/",
        50
    )
        .create(EsiApi::class.java)

    fun getCharecterInfo(accessToken :String) : Deferred<CharacterInfoResponse> {

        return esiApi.getCharacterInfo("Bearer $accessToken",
            "tranquility")
    }


    fun getMailsHeaders(accessToken :String,
                        characterId :Long): Deferred<List<MailHeaderResponse>>{
        return esiApi.getMailsHeader("Bearer $accessToken",
            characterId,
            "tranquility")
    }

    fun getMail(accessToken: String,
                characterId: Long,
                mailId: Long) : Deferred<MailResponse>{

        return esiApi.getMail("Bearer $accessToken",
            characterId,
            mailId)
    }


    fun sendMail(accessToken: String,
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


}