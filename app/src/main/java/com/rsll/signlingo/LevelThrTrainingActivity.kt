package com.rsll.signlingo

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.persistableBundleOf
import com.rsll.signlingo.databinding.ActivityLevelThrTrainingBinding
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.io.File


class LevelThrTrainingActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityLevelThrTrainingBinding
    private lateinit var outputDirectory: File
    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var curPhoto: File

    private val lettersArray = ('а'..'я').toMutableList()
    private var notCompletedRandomNumArray = lettersArray.indices.toMutableList()

    private val url: String = "http://192.168.0.167:5000/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLevelThrTrainingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        // SET random char
        var rndLetter = lettersArray[getRandomNumber()]
        val stndPhrase = viewBinding.textTask.text
        viewBinding.textTask.text = String.format("${stndPhrase}: $rndLetter")
        // Camera Permission and Take the photo
        if(allPermissionsGranted()){
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(
                this,Constants.REQUIRED_PERMISSIONS,Constants.REQUEST_CODE_PERMISSIONS
            )
        }

        outputDirectory = getOutputDirectory()
        viewBinding.takeAnswerButton.setOnClickListener {
            takePhoto()
//            if(true) {
//                //Toast.makeText(this,"it's right go next",Toast.LENGTH_LONG).show()
//                rndLetter = lettersArray[getRandomNumber()]
//                viewBinding.textTask.text = String.format("${stndPhrase}: $rndLetter")
//            }


        }
        cameraExecutor = Executors.newSingleThreadExecutor()
        viewBinding.skipQuestionButton.setOnClickListener{
            val result = uniHttpGetResult(url, curPhoto)
            Toast.makeText(baseContext,result.length.toString(),Toast.LENGTH_LONG).show()
        }
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(Constants.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let{ mFile -> File(
            mFile,resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun takePhoto() {

        val imageCapture = imageCapture?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(Constants.FILE_NAME_FORMAT,
                Locale.getDefault()).format(System.currentTimeMillis()) + ".png")

        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback{
                override fun onError(exc: ImageCaptureException) {
                    Log.e(Constants.TAG, "onError: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){

                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, msg)
                }
            }
        )
        curPhoto = photoFile
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() =
        Constants.REQUIRED_PERMISSIONS.all{
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun uniHttpGetResult(url: String, filePath: File): String{
        // unirest request POST
        val ex = PostToFlask()
        return ex.post(url,filePath.path, filePath)
    }

    private fun getRandomNumber(): Int {
        //using method исключения
        if(notCompletedRandomNumArray.isEmpty()) {
            notCompletedRandomNumArray = lettersArray
                .indices
                .toMutableList() }
        val rndnumValue = notCompletedRandomNumArray[(notCompletedRandomNumArray.indices).random()]
        notCompletedRandomNumArray.remove(rndnumValue)
        return rndnumValue
    }
}