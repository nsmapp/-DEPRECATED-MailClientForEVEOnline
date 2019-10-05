package by.nepravskysm.domain.repository.database
import by.nepravskysm.domain.entity.AuthInfo


interface AuthInfoRepository {

    suspend fun saveAuthInfo(accessToken :String,
                             refreshToken :String,
                             characterId :Long,
                             characterName :String)

    suspend fun getAuthInfo() : AuthInfo

    suspend fun getAllCharacters() : List<AuthInfo>

    suspend fun saveNewToken(accessToken :String,
                             refreshToken :String)
}