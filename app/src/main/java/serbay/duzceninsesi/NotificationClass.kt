package serbay.duzceninsesi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.*
import android.provider.Settings
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.notifications_layout.*


class NotificationClass: AppCompatActivity() {
    companion object {
        private const val ACCESS_NOTIFICATION_POLICY=35
        val permission = Array<String>(1){"android.Manifest.permission.ACCESS_NOTIFICATION_POLICY"}
    }

    private fun requestPermission()=
        ActivityCompat.requestPermissions(this, permission, ACCESS_NOTIFICATION_POLICY )
    fun notificationPermission(context: Context) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        this.finish()
        startActivity(Intent(this, MainActivity::class.java))
        startActivity(intent)

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.notifications_layout)
        var sharedPreferences=this.getSharedPreferences("haber8", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor=sharedPreferences.edit()
        var renk1=sharedPreferences.getString("jaRenk1","#262626")!!
        var renk2=sharedPreferences.getString("jaRenk2","#b90000")!!

        txtDurum.setTextColor(Color.parseColor(renk1))
        buttonNotification.backgroundTintList= ColorStateList.valueOf(Color.parseColor(renk2))
        window.statusBarColor= Color.parseColor(renk1)
        window.navigationBarColor= Color.parseColor(renk2)
        buttonNotification.setOnClickListener {
            notificationPermission(this)
        }//tamamla butonuna tıklandığında çalışacak




        if (sharedPreferences.getBoolean("permissionState",true)!!) {
            txtDurum.text="Bildirimler Açık"
        }else{
            txtDurum.text="Bildirimler Kapalı"
        }



        /*
        if(editor.remove("notification") == null) {
            editor.putBoolean("notification", true)
            editor.apply()
        }else {
            editor.remove("notification")
            editor.putBoolean("notification", true)
            editor.apply()
        }//switch ögesinin değiştirilmediği durumda tamamla butonuna tıklanması durumu için


        switchNotification.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked == null) {
                Toast.makeText(this@NotificationClass, "null değer",
                    Toast.LENGTH_SHORT).show()
            }else{
                if(isChecked==true){
                    editor.remove("notification")
                    editor.putBoolean("notification",isChecked)
                    editor.apply()
                    Toast.makeText(this@NotificationClass, "if içine girdi",
                        Toast.LENGTH_SHORT).show()
                }else{
                    editor.remove("notification")
                    editor.putBoolean("notification",isChecked)
                    editor.apply()
                    Toast.makeText(this@NotificationClass, "else içine girdi",
                    Toast.LENGTH_SHORT).show()
                }
            }
        }//switch ögesi değişince olacaklar*/
    }
}