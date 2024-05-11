package serbay.duzceninsesi.util


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class Util {


    fun shareWhatsapp(mContext: Context, Url: String) {
        var Url = Url
        val pm = mContext.packageManager
        try {

            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.type = "text/plain"

            waIntent.setPackage("com.whatsapp")
            Url = URLDecoder.decode(Url, "UTF-8")
            waIntent.putExtra(Intent.EXTRA_TEXT, Url)
            mContext.startActivity(Intent.createChooser(waIntent, "Share with"))

        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(mContext, "Hata oluştu", Toast.LENGTH_SHORT)
                .show()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }

    fun shareFacebook(mContext: Context, Url: String) {
        var sharedUrl = Url;
        if(Url.split("u=".toRegex()).dropLastWhile({ it.isEmpty() }).size > 1){
            sharedUrl = Url.split("u=".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
            shareOnApp("com.facebook", "https://www.facebook.com/sharer/sharer.php?u=", mContext, sharedUrl)
        }else{
            var uri = Uri.parse("fb://facewebmodal/f?href=" + Url)
            openBrowser(mContext,uri)
        }

    }

    fun shareTwitter(mContext: Context, Url: String) {
        shareOnApp("com.twitter", "http://m.twitter.com/?status=", mContext, Url)
    }

    fun openCall(mContext: Context,  Url: String) {
        val number = Uri.parse(Url)
        val intent = Intent(Intent.ACTION_DIAL, number)

        mContext.startActivity(intent)
    }

    fun openBrowser(mContext: Context, Url: Uri) {
        var intent = Intent(Intent.ACTION_VIEW, Url)
        mContext.startActivity(intent)
    }

    private fun shareOnApp(packageName: String, mobileSite: String, mContext: Context, Url: String) {
        var Url = Url
        var intent = Intent(Intent.ACTION_SEND)

        intent.type = "text/plain"
        try {
            Url = URLDecoder.decode(Url, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        intent.putExtra(Intent.EXTRA_TEXT, Url)
        // See if official  app is found
        var appFound = false
        val matches = mContext.packageManager.queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(packageName)) {
                intent.setPackage(info.activityInfo.packageName)
                appFound = true
                break
            }
        }
        //If app not found, load sharer.php in a browser
        if (!appFound) {
            val sharerUrl: String

            sharerUrl = mobileSite + Url


            intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
        }
        mContext.startActivity(intent)

    }
}
