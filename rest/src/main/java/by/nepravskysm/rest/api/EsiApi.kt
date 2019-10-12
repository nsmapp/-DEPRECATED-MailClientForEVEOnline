package by.nepravskysm.rest.api

import by.nepravskysm.rest.entity.request.MailMetadataRequest
import by.nepravskysm.rest.entity.request.MailRequest
import by.nepravskysm.rest.entity.response.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface EsiApi{


    @GET("/verify/")
    fun getCharacterInfo(
        @Header("Authorization") bearerToken :String,
        @Query("datasource") serverName :String
    ) : Deferred<CharacterInfoResponse>

    @GET("/latest/characters/{character_id}/mail/")
    fun getFirst50MailsHeader(
        @Header("Authorization") bearerToken :String,
        @Path("character_id") characterId :Long,
        @Query("datasource") serverName :String
        ): Deferred<List<MailHeaderResponse>>

    @GET("/latest/characters/{character_id}/mail/")
    fun getMailsHeaderBeforeId(
        @Header("Authorization") bearerToken :String,
        @Path("character_id") characterId :Long,
        @Query("datasource") serverName :String,
        @Query("last_mail_id") minHeaderId: Long
    ): Deferred<List<MailHeaderResponse>>



    @GET("/latest/characters/{character_id}/mail/{mail_id}/")
    fun getMail(
        @Header("Authorization") bearerToken :String,
        @Path("character_id") characterId :Long,
        @Path("mail_id") mailId :Long
    ): Deferred<MailResponse>

    @POST("/latest/characters/{character_id}/mail/")
    fun sendMail(
        @Header("Authorization") bearerToken :String,
        @Path("character_id") characterId :Long,
        @Body mail: MailRequest
    ): Deferred<Long>

    @POST("/latest/universe/names/")
    fun getNameList(@Body idArray: Array<Long>): Deferred<List<NamesResponse>>

    @POST("/latest/universe/ids/")
    fun getIdList(@Body nameArray: Array<String>): Deferred<idsResponse>

    @PUT("/latest/characters/{character_id}/mail/{mail_id}/")
    fun putMailMetadata(@Body mailMetadata: MailMetadataRequest,
                           @Header("Authorization") bearerToken :String,
                           @Path("character_id") characterId :Long,
                           @Path("mail_id") mailId :Long
    ): Deferred<Unit>

    @DELETE("/latest/characters/{character_id}/mail/{mail_id}/")
    fun deleteMail(@Header("Authorization") bearerToken :String,
                   @Path("character_id") characterId :Long,
                   @Path("mail_id") mailId :Long
    ): Deferred<Unit>

    @GET("/latest/characters/{character_id}/mail/lists/")
    fun getMailingList(@Header("Authorization") bearerToken :String,
                   @Path("character_id") characterId :Long
    ): Deferred<List<MailingListResponse>>

    @GET("/latest/characters/{character_id}/contacts/")
    fun getContactList(@Header("Authorization") bearerToken :String,
                       @Path("character_id") characterId :Long
    ): Deferred<List<ContactsResponse>>
}

