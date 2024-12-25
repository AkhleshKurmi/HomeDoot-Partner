package com.example.akhleshkumar.homedootpartner.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult

class ImagePicker(val context: Context, val activity: Activity) {
    val REQUEST_IMAGE_PICK = 300
    @SuppressLint("NewApi")
    fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.onActivityResult(REQUEST_IMAGE_PICK ,Activity.RESULT_OK,intent,activity.caller!!)
    }
    fun jj(){

    }

}