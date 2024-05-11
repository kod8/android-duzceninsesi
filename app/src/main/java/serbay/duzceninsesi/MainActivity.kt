package serbay.duzceninsesi

import android.Manifest
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import android.webkit.*
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import serbay.duzceninsesi.util.Constant
import serbay.duzceninsesi.util.Util
import java.lang.ref.WeakReference
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.loading_layout_2.*

import android.graphics.Bitmap
import androidx.core.app.NotificationManagerCompat

import com.squareup.picasso.Picasso
import androidx.core.content.ContextCompat
import com.github.ybq.android.spinkit.style.Wave

import info.androidhive.fontawesome.FontDrawable
import kotlinx.android.synthetic.main.loading_layout_2.view.*


class MainActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {
    private var mContext: MainActivity? = null

    object renk{
        var renkler=arrayOf<String>("#000000","#ff0000")
        fun getColor(i:Int):Int{
            return Color.parseColor(renkler[i])
        }

        fun getFilter(i:Int):PorterDuffColorFilter{
            return  PorterDuffColorFilter(getColor(i),PorterDuff.Mode.SRC_ATOP)
        }
    }
    fun notificationPermission(){
        var intent = Intent(this, NotificationClass::class.java)
        startActivity(intent)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val dialog2 = CustomProgressDialog2(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)

        mainLoading.visibility = View.VISIBLE
        mContext = this
        //App.arkaPlan().execute(Constant.customurl)

        var sharedPreferences=this.getSharedPreferences("haber8",Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor=sharedPreferences.edit()
        var renk1=sharedPreferences.getString("jaRenk1","#000000")!!
        var renk2=sharedPreferences.getString("jaRenk2","#ff0000")!!


        editor.putBoolean("permissionState", NotificationManagerCompat.from(this).areNotificationsEnabled())
        editor.apply()


        if (renk1!=""){
            renk.renkler[0]=renk1
        }else{
            renk.renkler[0]="#000000"

        }
        if (renk2!=""){
            renk.renkler[1]=renk2
        }else{
            renk.renkler[1]="#ff0000"

        }
        val elemanSayisi=sharedPreferences.getInt("elemanSayisi",0)

        bottomAppBar.backgroundTint=ColorStateList.valueOf(renk.getColor(0))
        btnShare.backgroundTintList=ColorStateList.valueOf(renk.getColor(1))
        dialog2.spin_kit.setColor(renk.getColor(1))
        var threeBounce =  Wave()
        threeBounce.setColor(renk.getColor(1))
        progressBar.setIndeterminateDrawable(threeBounce)
        //progressBar.spin_kit.setColor(renk.getColor(1))
        dialog2.yuzde.progressDrawable.colorFilter=renk.getFilter(1)

        Picasso.get().load(sharedPreferences.getString("jaLogo","logo yok")).into(imgLogo);

        window.statusBarColor= renk.getColor(0)
        window.navigationBarColor=renk.getColor(1)

        val altMenuIcons = bottomAppBar.menu
        fun changeIcon(drawable:FontDrawable, menuItem: MenuItem){
            drawable.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            drawable.textSize= 20F
            menuItem.setIcon(drawable)
        }
        for (i in 0..7){
            var menuItem=altMenuIcons.getItem(i).toString()
                when (menuItem) {
                    "menuNav" -> {
                        val drawable = FontDrawable(this, R.string.fa_bars_solid, true, false)
                        changeIcon(drawable,altMenuIcons.getItem(i))
                        true
                    }
                    "menuHome" -> {
                        val drawable = FontDrawable(this, R.string.fa_home_solid, true, false)
                        changeIcon(drawable,altMenuIcons.getItem(i))
                        true
                    }
                    "yorum"-> {
                        val drawable = FontDrawable(this, R.string.fa_comments_solid, true, false)
                        changeIcon(drawable,altMenuIcons.getItem(i))
                        true
                    }
                    "videohaber" -> {
                        val drawable = FontDrawable(this, R.string.fa_video_solid, true, false)
                        changeIcon(drawable,altMenuIcons.getItem(i))
                        true
                    }
                    "arsiv" -> {
                        val drawable = FontDrawable(this, R.string.fa_archive_solid, true, false)
                        changeIcon(drawable,altMenuIcons.getItem(i))
                        true
                    }
                    "canli" -> {
                        val drawable = FontDrawable(this, R.string.fa_broadcast_tower_solid, true, false)
                        changeIcon(drawable,altMenuIcons.getItem(i))
                        true
                    }
                    "gazete" -> {
                        val drawable = FontDrawable(this, R.string.fa_newspaper_solid, true, false)
                        changeIcon(drawable,altMenuIcons.getItem(i))
                        true
                    }
                    "yazar" -> {
                        val drawable = FontDrawable(this, R.string.fa_pen_fancy_solid, true, false)
                        changeIcon(drawable,altMenuIcons.getItem(i))
                        true
                    }
                    "setting" -> {
                        val drawable = FontDrawable(this, R.string.fa_sliders_h_solid, true, false)
                        changeIcon(drawable,altMenuIcons.getItem(i))
                        true
                    }
                    else -> false
                }
        }




        val altMenuView: BottomAppBar = findViewById(R.id.bottomAppBar)
        val altMenu:Menu=altMenuView.menu
        for (i in 2..elemanSayisi+1){
            if(altMenu.getItem(i).toString()==sharedPreferences.getString("jaId_${altMenu.getItem(i).toString()}","id yok")){
                altMenu.getItem(i).setVisible(sharedPreferences.getBoolean("jaState_${altMenu.getItem(i).toString()}",false))

            }
        }

        altMenu.findItem(R.id.notification).setVisible(true)//notification turned on

        //altMenu.getItem(2).setVisible(false)

        bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuNav -> {
                    webview.evaluateJavascript("document.querySelector(\"#tost-2\").click()",null)
                    true
                }
                R.id.notification -> {
                    notificationPermission()
                    true
                }
                R.id.menuHome -> {
                    webview.loadUrl(sharedPreferences.getString("url",Constant.url)!!)

                    true
                }
                R.id.yorum -> {
                    webview.loadUrl(sharedPreferences.getString("jaUrl_yorum",Constant.url)!!)
                    true
                }
                R.id.videohaber -> {
                    webview.loadUrl(sharedPreferences.getString("jaUrl_videohaber",Constant.url)!!)
                    true
                }
                R.id.arsiv -> {
                    webview.loadUrl(sharedPreferences.getString("jaUrl_arsiv",Constant.url)!!)
                    true
                }R.id.canli -> {
                    webview.loadUrl(sharedPreferences.getString("jaUrl_canli",Constant.url)!!)
                    true
                }R.id.gazete -> {
                    webview.loadUrl(sharedPreferences.getString("jaUrl_gazete",Constant.url)!!)
                    true
                }R.id.yazar -> {
                    webview.loadUrl(sharedPreferences.getString("jaUrl_yazar",Constant.url)!!)
                    true
                }
                else -> false
            }
        }

        btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, webview.title + "\n\n" +webview.url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        swp_refresh_view!!.setOnRefreshListener(this)
        webview!!.settings.javaScriptEnabled = true
        webview!!.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        WebView.setWebContentsDebuggingEnabled(true)

        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                println(newProgress)
                if (mainLoading.visibility == View.GONE){
                    dialog2.show()
                }

                if (dialog2.isShowing){
                    dialog2.setYuzde(newProgress)

                }
                if (dialog2.isShowing && newProgress==100){
                    dialog2.setYuzde(newProgress)
                    Handler().postDelayed({
                        dialog2.dismiss()
                    },300)

                }


            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                return super.onConsoleMessage(consoleMessage)
                Log.d("uyarı", " "+consoleMessage)
            }
        }

        webview!!.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            //dosya izni
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it")
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, 1)
                }
            }
            var fileName=URLUtil.guessFileName(url, contentDisposition, mimeType)
            val uzanti=url.split(".").reversed()[0];


            if(mimeType=="application/octet-stream"){fileName+= ".$uzanti"}
            request.setMimeType(mimeType)
            request.addRequestHeader(
                "cookie",
                CookieManager.getInstance().getCookie(url)
            )
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(fileName)
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS,fileName)
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            dm.enqueue(request)
            if (dialog2.isShowing){
                dialog2.dismiss()
            }
            Toast.makeText(applicationContext, "Downloading File", Toast.LENGTH_LONG)
                .show()
        }

        val wvClient = object : WebViewClient() {


            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {

            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                if (dialog2.isShowing && url!!.endsWith("#")){
                    dialog2.dismiss()

                }
                super.doUpdateVisitedHistory(view, url, isReload)
            }
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                val util = Util()
                var splitUrl = url.split("/")
                var appUrl= sharedPreferences.getString("url",Constant.url)!!.split("/").get(2)
                println("appurl= "+appUrl)
                if(App().isInternetOn(this@MainActivity) && splitUrl.size > 2 && splitUrl.get(2).contains(appUrl)) {
                    view.loadUrl(url)
                }else if(App().isInternetOn(this@MainActivity) && splitUrl.size > 2 && !splitUrl.get(2).contains(appUrl)){
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                    return true
                }else if (url.startsWith(("tel:"))) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                    return true
                }else if (url.startsWith(("mailto:"))) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                    return true
                }else {
                    offlineMessage()
                }
                return true
            }


            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                //webview!!.evaluateJavascript(""+Constant.js_script,null)
               //webview!!.evaluateJavascript(""+Constant.css_script,null)

                if (mainLoading.visibility == View.VISIBLE) {
                    mainLoading.visibility = View.GONE
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                if (mainLoading.visibility == View.GONE){
                    dialog2.show()
                }

                webview!!.isFocusableInTouchMode = false
            }
        }

        webview.webViewClient = wvClient
        webview.settings.useWideViewPort = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.saveFormData = false
        webview.settings.pluginState = WebSettings.PluginState.ON
        webview.settings.javaScriptEnabled = true
        webview.settings.allowFileAccess = true
        webview.settings.domStorageEnabled = true
        webview.settings.setAppCacheEnabled(false)
        webview.requestFocus(View.FOCUS_DOWN)

        if (App().isInternetOn(this)){
            if(App.mesajUrl.isEmpty()){
                if (Intent.ACTION_VIEW == intent.action) {
                    val data = intent.dataString ?: sharedPreferences.getString("url",Constant.url)!!
                    webview!!.loadUrl(data)
                    println("is internet on if")
                }else{
                    webview!!.loadUrl(sharedPreferences.getString("url",Constant.url)!!)
                    //println(sharedPreferences.getString("url",Constant.urlArchive))
                    println("is internet on else")
                }
            }else{
                webview!!.loadUrl(App.mesajUrl)
            }
        }else
            offlineMessage()

        swp_refresh_view!!.viewTreeObserver.addOnScrollChangedListener {
            swp_refresh_view!!.isEnabled =webview!!.scrollY == 0
        }


    }

    fun offlineMessage() {
        var sharedPreferences=this.getSharedPreferences("haber8",Context.MODE_PRIVATE)
        val dialog = Dialog(WeakReference<Context>(this).get()!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.comp_logout_dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        val txt = dialog.findViewById<TextView>(R.id.txt)
        val titleTextView = dialog.findViewById<TextView>(R.id.title)
        val cancelButton = dialog
            .findViewById<RelativeLayout>(R.id.rl_cancel)
        val continueButton = dialog
            .findViewById<RelativeLayout>(R.id.rl_continue)
        titleTextView.setText(R.string.warning)
        txt.setText(R.string.msg_no_internet)
        dialog.show()

        cancelButton.setOnClickListener { finish() }

        continueButton.setOnClickListener {
            dialog.hide()
            if (App().isInternetOn(this@MainActivity)){
                if (Intent.ACTION_VIEW == intent.action) {
                    val data = intent.dataString ?: sharedPreferences.getString("url",Constant.url)!!
                    webview!!.loadUrl(data)
                }else{
                    webview!!.loadUrl(sharedPreferences.getString("url",Constant.url)!!)
                }
            }
            else
                offlineMessage()
        }
    }

    override fun onResume() {
        super.onResume()
        if(App.mesajUrl.isNotEmpty()){
            webview.loadUrl(App.mesajUrl)
            App.mesajUrl = ""
        }
    }

    override fun onBackPressed() {

        if (webview!!.canGoBack())
            webview!!.goBack()
        else
            super.onBackPressed()
    }

    override fun onRefresh() {
        if (webview != null)
            webview!!.reload()
        swp_refresh_view!!.isRefreshing = false
    }
}
