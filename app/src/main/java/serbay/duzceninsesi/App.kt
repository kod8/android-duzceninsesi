package serbay.duzceninsesi

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import com.github.kittinunf.fuel.httpGet
import serbay.duzceninsesi.util.Constant
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal
import com.onesignal.OneSignal.OSNotificationOpenedHandler
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

class App : Application() {


    companion object {
        const val ONESIGNAL_APP_ID = "eb7c4756-8316-461c-8356-de41f4be84f0"
        var mesajUrl = ""

    }
    private open class NotificationOpenedHandler : OSNotificationOpenedHandler {

        override fun notificationOpened(result: OSNotificationOpenedResult) {
            var additionalMessage = ""
            try {
                if (result.toJSONObject() != null) {
                    val url = result.notification.additionalData.getString("url")
                    if (!url.isNullOrEmpty()) {
                        mesajUrl = url
                    }
                    additionalMessage = """
                    Full additionalData:
                    ${result.toJSONObject().toString()}
                    """.trimIndent()
                }
                Log.d(
                    "OneSignalExample",
                    "message:\n \nadditionalMessage:\n$additionalMessage"
                )
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        var sharedPreferences=this.getSharedPreferences("haber8",Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor=sharedPreferences.edit()


        runBlocking{
            val (request, response, result) = Constant.settingsUrl
                .httpGet()
                .awaitStringResponse()
            val jsonResponse = JSONObject(result)
            val jaLogo=jsonResponse.getString("logo")
            val jaRenk1=jsonResponse.getString("renk1")
            val jaRenk2=jsonResponse.getString("renk2")
            val jaLoading=jsonResponse.getString("loading")
            val jaUrl=jsonResponse.getString("url")
            val jaMenu: JSONArray = jsonResponse.getJSONArray("menu")
            for (i in 0 until jaMenu.length()) {
                val joDeger = jaMenu.getJSONObject(i)
                val id = joDeger.getString("id")
                val url = joDeger.getString("url")
                val name = joDeger.getString("name")
                val state = joDeger.getBoolean("state")
                editor.putString("jaId_${id}",id)
                editor.putString("jaUrl_${id}",url)//alt bardan yönlenecek url
                editor.putString("jaName_${id}",name)
                editor.putBoolean("jaState_${id}",state)
            }
            editor.putInt("elemanSayisi",jaMenu.length())
            editor.putString("jaLogo",jaLogo)
            editor.putString("jaRenk1",jaRenk1)
            editor.putString("jaRenk2",jaRenk2)
            editor.putString("jaLoading",jaLoading)
            editor.putString("url",jaUrl)
            editor.apply()
        }


        /*Constant.settingsUrl.httpGet().responseJson{_,response,result->
            //println(result.component2().toString())
            val jaLogo=result.get().obj().getString("logo")
            val jaRenk1=result.get().obj().getString("renk1")
            val jaRenk2=result.get().obj().getString("renk2")
            val jaLoading=result.get().obj().getString("loading")
            val jaUrl=result.get().obj().getString("url")
            editor.putString("jaLogo",jaLogo)
            editor.putString("jaRenk1",jaRenk1)
            editor.putString("jaRenk2",jaRenk2)
            editor.putString("jaLoading",jaLoading)
            editor.putString("jaUrl",jaUrl)
            editor.apply()
        }*/

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        OneSignal.setNotificationOpenedHandler(NotificationOpenedHandler())
    }

    fun isInternetOn(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager
            .activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}