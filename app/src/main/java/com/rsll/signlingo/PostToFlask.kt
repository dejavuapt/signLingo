package com.rsll.signlingo

import android.util.Log
import okhttp3.*
import org.apache.bcel.generic.RETURN
import java.io.File
import java.io.IOException
import java.lang.Exception


class PostToFlask {

    private val mediaType = MediaType.parse("text/plain")
    private var client = OkHttpClient().newBuilder().build()
    var getAnswer: String = ""

    fun post(url: String, pathImg: String, fileStr: File): String{
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                pathImg,
                RequestBody.create(
                    MediaType.parse("application/octet-stream"),
                    fileStr))
            .build()

        val request = Request.Builder()
            .url(url)
            .method("POST",body)
            .build()

        client.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body()
                    if(!response.isSuccessful){
                        throw IOException("unix$response")
                    }
                    if(responseBody != null){
                        getAnswer = response.body().toString()
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }

        })
        return getAnswer
    }
}