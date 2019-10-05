package by.nepravskysm.rest.api
import by.nepravskysm.rest.entity.response.AuthTokenResponse
import by.nepravskysm.rest.entity.response.RefreshTokenResponse
import kotlinx.coroutines.Deferred



class AuthManager {

    val authApi: AuthApi = createRetrofit(
        "https://login.eveonline.com/",
        50
    )
        .create(AuthApi::class.java)


    private val clientId = "743ea7773e4940aeba49b2ada5cbd911"
    private val secretKey = "0joT0xP5d1ax4EXcudbzRaj0GGEFmc3IdMwRLf6j"


    fun getAuthToken(code :String): Deferred<AuthTokenResponse> {


//        val authKey: String = Base64.encode("$clientId:$secretKey".toByteArray()).toString()

        val header = "Basic NzQzZWE3NzczZTQ5NDBhZWJhNDliMmFkYTVjYmQ5MTE6MGpvVDB4UDVkMWF4NEVYY3VkYnpSYWowR0dFRm1jM0lkTXdSTGY2ag=="
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