## Mail client for EVE Online
Applications to view and send mail in the game EVE Online
- Read and send emails
- Calculate a reinforce timer
- Receive emails from mailing lists

### Libraries used in the project
   - Room
   - OkHttp
   - Retrofit
   - Kotlin coroutines
   - Koin
   - AndroidX
   - Picasso
   
###    How use
Create your app: https://developers.eveonline.com/applications

##### Edit File
./rest/src/main/java/by/nepravskysm/rest/api/AuthManager.kt

```java
//    Example
//    private val clientId = "oi3fh984fshf4wp39hf3jj3h"
    private val clientId = "PAST_YOUR_CLIENT_ID"

//   Example
//   create string "clientId:secretKey" and encode in base64 Y2xpZW50SWQ6c2VjcmV0S2V5IGpucm5uZTtvZm1uczttc25yZw==
//   val header = "Basic Y2xpZW50SWQ6c2VjcmV0S2V5IGpucm5uZTtvZm1uczttc25yZw=="
     val header = "Basic PAST_YOUR_ENCODED_BASE64_CLIENT_ID_AND_SECRET_KEY"
```



##### Edit file 
/app/src/main/AndroidManifest.xml
```xml
<!-- Example callbeck eveauthmyschemename://myhostname/ -->
<!-- android:host="myhostname"-->
<!-- android:scheme="eveauthmyschemename"/>-->
     android:host="PAST_YOUR_HOST"
     android:scheme="PASR_YOUR_SCHEME"/>
```

