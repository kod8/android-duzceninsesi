package serbay.duzceninsesi.util

/**
 * Created by Abdullah Öğük and Feyzanur Keskin on 01.10.2021.
 */

object Constant {

    var baseurl = "https://m.duzceninsesi.com.tr"
    var url = baseurl+"/?uygulama=android"
    var urlComment = baseurl+"/okuyucu-yorumlari"
    var urlVideo = baseurl+"/video-haber"
    var urlArchive = baseurl+"/arsiv"
    var url_js=baseurl+"/js/android.js"
    var url_css=baseurl+"/css/android.css"
    var settingsUrl = "https://api.haber8.pro/json/duzceninsesi.com.tr.json"

    var js_script="(function() {var parent = document.getElementsByTagName('head').item(0);" +
            "var script = document.createElement('script');" +
            "script.type = 'text/javascript';" +
            "script.src = '"+url_js+"';" +
            "parent.appendChild(script)})()"
    var css_script="(function() {var parent = document.getElementsByTagName('head').item(0);" +
            "var link = document.createElement('link');" +
            "link.type = 'text/css';" +
            "link.rel = 'stylesheet';" +
            "link.href = '"+url_css+"';" +
            "parent.appendChild(link)})()"

}
