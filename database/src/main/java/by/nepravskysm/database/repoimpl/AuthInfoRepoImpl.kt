package by.nepravskysm.database.repoimpl

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
                AuthInfoDBE(0,
                accessToken,
                refreshToken,
                characterId,
                characterName)
            )

    }

    override suspend fun saveNewToken(accessToken: String, refreshToken: String) {
        appDatabase.authInfoDao().updateToken(accessToken, refreshToken)
    }

    override suspend fun getAuthInfo() : AuthInfo {
        val authInfoDBE = appDatabase.authInfoDao()
            .getAuthInfo()
        return AuthInfo(authInfoDBE.accessToken,
            authInfoDBE.refreshToken,
            authInfoDBE.characterId,
            authInfoDBE.characterName)
    }

}