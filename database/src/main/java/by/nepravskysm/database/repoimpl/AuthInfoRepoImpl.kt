package by.nepravskysm.database.repoimpl

import android.util.Log
import by.nepravskysm.database.AppDatabase
import by.nepravskysm.database.entity.AuthInfoDBE
import by.nepravskysm.domain.entity.AuthInfo
import by.nepravskysm.domain.repository.database.AuthInfoRepository

class AuthInfoRepoImpl(private val appDatabase: AppDatabase) :
    AuthInfoRepository {


    override suspend fun getAllCharacters(): List<AuthInfo> {

        val authInfoList = appDatabase.authInfoDao().getAllCharactersAuthInfo()
        val domainAuthList = mutableListOf<AuthInfo>()
        for (info in authInfoList){
            Log.d("logd", "${info.id } ${info.characterId} ${info.characterName}")
            val authInfo = AuthInfo(info.accessToken,
                info.refreshToken,
                info.characterId,
                info.characterName)

            domainAuthList.add(authInfo)
        }
        return domainAuthList
    }


    override suspend fun saveAuthInfo(accessToken :String,
                                      refreshToken :String,
                                      characterId :Long,
                                      characterName :String) {
        appDatabase.authInfoDao()
            .insertAuthInfo(
                AuthInfoDBE(
                accessToken,
                refreshToken,
                characterId,
                characterName)
            )

    }

    override suspend fun saveNewToken(accessToken: String, refreshToken: String, characterName: String) {
        appDatabase.authInfoDao().updateToken(accessToken, refreshToken, characterName)
    }

    override suspend fun getAuthInfo(characterName: String) : AuthInfo {
        val authInfoDBE = appDatabase.authInfoDao()
            .getAuthInfo(characterName)
        return AuthInfo(authInfoDBE.accessToken,
            authInfoDBE.refreshToken,
            authInfoDBE.characterId,
            authInfoDBE.characterName)
    }

}