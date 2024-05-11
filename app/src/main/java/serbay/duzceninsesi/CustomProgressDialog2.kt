package serbay.duzceninsesi

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.loading_layout_2.*

internal class CustomProgressDialog2(context: Context, full: Int) :

    Dialog(context, R.style.full_screen_dialog) {

    fun setYuzde(value: Int){
        yuzde.progress=value
        txtYuzde.text="${value.toString()}%"
    }
    init {
        val params = window!!.attributes

        //params.gravity= Gravity.CENTER_HORIZONTAL;
        window!!.attributes = params
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setTitle("asd")
        setCancelable(false)
        setOnCancelListener(null)
        val view = LayoutInflater.from(context).inflate(R.layout.loading_layout_2, null)
        setContentView(view)
    }


}