package by.nepravskysm.rest.api
import by.nepravskysm.rest.entity.response.AuthTokenResponse
import by.nepravskysm.rest.entity.response.RefreshTokenResponse
import kotlinx.coroutines.Deferred

class AuthManager {

    val authApi: AuthApi = createRetrofit(
        "https://login.eveonline.com/",
        50
    ).create(AuthApi::class.java)

    //    Example
//    private val clientId = "oi3fh984fshf4wp39hf3jj3h"
    private val clientId = "PAST_YOUR_CLIENT_ID"

    fun getAuthToken(code :String): Deferred<AuthTokenResponse> {

//      Example
//      create string "clientId:secretKey" and encode in base64 Y2xpZW50SWQ6c2VjcmV0S2V5IGpucm5uZTtvZm1uczttc25yZw==
//      val header = "Basic Y2xpZW50SWQ6c2VjcmV0S2V5IGpucm5uZTtvZm1uczttc25yZw=="
        val header = "Basic PAST_YOUR_ENCODED_BASE64_CLIENT_ID_AND_SECRET_KEY"
        return authApi.getAuthInfo(
            header,
            "authorization_code",
            code
        )
    }

    fun refreshToken(refreshToken :String) : Deferred<RefreshTokenResponse> {
        return authApi.getRefreshToken(
            "refresh_token",
            refreshToken,
            clientId
        )
    }
}