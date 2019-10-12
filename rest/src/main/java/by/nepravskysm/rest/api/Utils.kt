package by.nepravskysm.rest.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.timeunit.TimeUnit
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level


private fun createHttpClient( timeOut :Long) : OkHttpClient{

    val interceptor = HttpLoggingInterceptor(
        object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                java.util.logging.Logger.getLogger("interceptor").log(Level.INFO, "$message")
            }
        }
    )
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .writeTimeout(timeOut, TimeUnit.SECONDS)
        .readTimeout(timeOut, TimeUnit.SECONDS)
        .connectTimeout(timeOut, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}

fun createGson() :Gson{
    return GsonBuilder()
        .setLenient()
        .create()
}

fun createRetrofit(url: String, timeOut :Long ) : Retrofit{

    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(createGson()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(url)
        .client(createHttpClient(timeOut))
        .build()
}

fun createAuthUrl(): String{

    return HttpUrl.Builder()
        .scheme("https")
        .host("login.eveonline.com")
        .addPathSegments("v2")
        .addPathSegment("oauth")
        .addPathSegments("authorize")
        .addQueryParameter("response_type", "code")
        .addQueryParameter("redirect_uri", "eveauthnsmmailclient://gesgrailclient/")
        .addQueryParameter("client_id", "743ea7773e4940aeba49b2ada5cbd911")
        .addQueryParameter("scope", "esi-mail.organize_mail.v1 esi-mail.read_mail.v1 esi-mail.send_mail.v1 esi-characters.read_notifications.v1 esi-characters.read_contacts.v1")
        .addQueryParameter("state", "statestringooo")
        .build()
        .toString()

}
