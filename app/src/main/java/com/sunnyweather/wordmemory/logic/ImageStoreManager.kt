package com.sunnyweather.wordmemory.logic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object ImageStoreManager {

    private const val IMAGE_FILE = "app_images"

    fun saveImage(context: Context, bitmap: Bitmap, imageName: String) : Boolean {

        return try {

            val dir = File(context.filesDir, IMAGE_FILE)
            if(!dir.exists()) dir.mkdirs()

            val file = File(dir, imageName)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()

            true
        }
        catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    fun loadImage(context: Context, imageName: String) : Bitmap? {
        return try {
            val dir = File(context.filesDir, IMAGE_FILE)
            val file = File(dir, imageName)
            if(!file.exists()) return null

            BitmapFactory.decodeStream(FileInputStream(file))
        }
        catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}