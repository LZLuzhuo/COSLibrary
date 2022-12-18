package me.luzhuo.tencentcosdemo

//import me.luzhuo.lib_video_upload.VideoUploadManager
//import me.luzhuo.lib_video_upload.IVideoUploadCallback
import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.luzhuo.lib_core.app.base.CoreBaseActivity
import me.luzhuo.lib_core.app.base.CoreBaseApplication
import me.luzhuo.lib_core.data.hashcode.HashManager
import me.luzhuo.lib_cos.TencentCOSFileServer
import me.luzhuo.lib_cos.bean.COSCAMBean
import me.luzhuo.lib_cos.bean.COSFileBean
import me.luzhuo.lib_cos.callback.ICOSFileCallback
import me.luzhuo.lib_cos.callback.ICOSFilesCallback
import me.luzhuo.lib_cos.callback.IProgress
import me.luzhuo.lib_file.FileManager
import me.luzhuo.lib_permission.Permission
import me.luzhuo.lib_permission.PermissionCallback
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var fileServer: TencentCOSFileServer
    private lateinit var filePath: File
    // private lateinit var videoUpload: VideoUploadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Permission.request(this, object : PermissionCallback() {
            override fun onGranted() {}
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)


//        startActivity(new Intent(this, SecondActivity.class));

//        for (String volumeName : MediaStore.getExternalVolumeNames(this)) {
//            Uri uri = MediaStore.Audio.Media.getContentUri(volumeName);
//        }

        val fileManager = FileManager(CoreBaseApplication.appContext)
         filePath = File(fileManager.cacheDirectory, "file.mp4")
//        filePath = File(fileManager.getCacheDirectory(this), "file.mp4")
        if (!filePath.exists()) {
             fileManager.Stream2File(assets.open("file.mp4"), filePath.absolutePath)
//            fileManager.Stream2File(assets.open("file.mp4"), filePath.absolutePath)
        }
        Log.e(TAG, "filePath: " + filePath.absoluteFile);

//         initCOS()
//        initCOSS()
//        initVideoUpload()

        findViewById<View>(R.id.upload).setOnClickListener{
            /*COSUtils.getInstance(this).uploadFileByTencentCOS(COSFileBean(filePath.absolutePath, HashManager.getInstance().uuid), { progress, currentSize, totalSize -> Log.e(TAG, "progress: $progress - $currentSize - $totalSize"); }, object : ICOSFileCallback {
                override fun onSuccess(fileBean: COSFileBean?) {
                    Log.e(TAG, "success: $fileBean");
                }

                override fun onError(errorMessage: String?) {
                    Log.e(TAG, "error: $errorMessage")
                }
            })*/

            val files = arrayListOf<COSFileBean>().apply {
                repeat(10) {
                    add(COSFileBean(Uri.parse("content://media/external/file/404487"), HashManager.getInstance().uuid))
                }
            }
            COSUtils.getInstance(this).uploadFilesByTencentCOS(files, { progress, currentSize, totalSize -> Log.e(TAG, "progress: $progress - $currentSize - $totalSize"); }, object : ICOSFilesCallback {
                override fun onSuccess(fileBeans: MutableList<COSFileBean>?) {
                    fileBeans?.forEach {
                        Log.e(TAG, "success: $it");
                    }
                }

                override fun onError(errorMessage: String?) {
                    Log.e(TAG, "error: $errorMessage")
                }
            })
        }
    }

//    /**
//     * 腾讯云点播视频上传
//     */
//    private fun initVideoUpload() {
//        videoUpload = VideoUploadManager(this)
//        videoUpload.setSignature("1TwMNWwkQJHLJrJSVyQ/dUVJIddzZWNyZXRJZD1BS0lEa0liY01wMnhieTR1VGxsVnpDOXJLV1M3V2RUNFBUR20mY3VycmVudFRpbWVTdGFtcD0xNjM2MDk2NDEyJnByb2NlZHVyZT1Vc2VyVXBsb2FkUHJlc2V0JmV4cGlyZVRpbWU9MTYzNjE4MjgxMiZyYW5kb209MTk4Mjk3ODExNg==")
//        videoUpload.setVideoUploadCallback(object : IVideoUploadCallback {
//            override fun onProgress(progress: Int, currentSize: Long, totalSize: Long) {
//                Log.e(TAG, "progress: $progress currentSize: $currentSize totalSize: $totalSize")
//            }
//
//            override fun onSuccess(successCode: Int, videoId: String?, videoUrl: String?) {
//                Log.e(TAG, "onSuccess successCode: $successCode videoId: $videoId videoUrl: $videoUrl")
//            }
//
//            override fun onError(errorCode: Int, errorMessage: String?) {
//                Log.e(TAG, "onError errorCode: $errorCode errorMessage: $errorMessage")
//            }
//        })
//        findViewById<View>(R.id.upload).setOnClickListener{
//            videoUpload.uploadVideo(filePath.absolutePath)
//        }
//    }

    private fun initCOS() {
        fileServer = object : TencentCOSFileServer(this, "ap-shanghai", "cos-demo-1257757876", "temp/cos/"){
            override fun getTokenFromUser(): COSCAMBean {
                // Run on child thread
                // 授权过期不会自己回调: com.tencent.qcloud.core.common.QCloudClientException: fetch credentials error happens: beginTime must be less than expiredTime.
                return COSCAMBean("AKIDvqjCAQiJAqfCVRwrUoe3MZWeAtNOzYzpWG8-sA0gckzAXonssqNEcFp0vpe0pdXF", "Fry08XKBfrKYbVkShovnNIxMZIQdu2O8fNu0XdA4a34=", "aysp7z6TrGXkiEvKxeyDJihWg9UFcq2aa8a4b6b997fd12e363bfc497c82466758tlC1CAqnj6sZTHQIjBcQN3oSHYWwEImXda0mqUUlvhbC57_K60BbBoGO3p6ybBwWNiA6jKcex3zq4MiQvq4oz2V6xoxoC-GsRW16hFLDbE5gf4dKvYmJF1DsyFvB5t5lEzL107y2NTwJclEKdUEZtF5Gl3x3Nkn3tRGjM-5pbKrZfttddb_oYb3zsiUGN_tKBHRnZcHyz7CWINxOqOcYYKvHvHIopJG1lNU-9KoZ44S9N3KGPyFsHjf2KeC6w47R0RMDBfirmMhodWVCo4C9KC1ebt_-IBWeyn2mJA4OhQBsnJAuhFuhnXRtoYI156qmZZSq0mWfl6xtzuxcc6bV7rH_l3ZfivUsS-vPzgoMT8rmn5elNvT55IV7fYj57x2G2UNpGeuxp8Xg5_gM-uu4xJmyRlzd9FLp3kyqpz9nMScNlLQEkZ7MPtGB-xZvbQMeP_a5Ly1wcIuY0A9z0Ml7cCk4mMCnGvvmauWYVPeu_NDaj8-pWC0Eh20v30CR20a", 1636732167)
            }
        }

        findViewById<View>(R.id.upload).setOnClickListener{
            val file = COSFileBean(filePath.absolutePath, HashManager.getInstance().uuid)
            fileServer.uploadFileByTencentCOS(file, object : IProgress {
                override fun onProgress(progress: Int, currentSize: Long, totalSize: Long) {}
            }, object : ICOSFileCallback {
                override fun onSuccess(fileBean: COSFileBean?) {
                    Log.e(TAG, "" + file.networkPath);
                }

                override fun onError(errorMessage: String?) {
                    Log.e(TAG, "" + errorMessage);
                }
            })
        }
    }

    private fun initCOSS() {
        fileServer = object : TencentCOSFileServer(this, "ap-shanghai", "cos-demo-1257757876", "temp/cos/"){
            override fun getTokenFromUser(): COSCAMBean {
                // Run on child thread
                Log.e(TAG, "666: " + Thread.currentThread().name + " === " + Thread.currentThread().id)
                return COSCAMBean("AKIDvqjCAQiJAqfCVRwrUoe3MZWeAtNOzYzpWG8-sA0gckzAXonssqNEcFp0vpe0pdXF", "Fry08XKBfrKYbVkShovnNIxMZIQdu2O8fNu0XdA4a34=", "aysp7z6TrGXkiEvKxeyDJihWg9UFcq2aa8a4b6b997fd12e363bfc497c82466758tlC1CAqnj6sZTHQIjBcQN3oSHYWwEImXda0mqUUlvhbC57_K60BbBoGO3p6ybBwWNiA6jKcex3zq4MiQvq4oz2V6xoxoC-GsRW16hFLDbE5gf4dKvYmJF1DsyFvB5t5lEzL107y2NTwJclEKdUEZtF5Gl3x3Nkn3tRGjM-5pbKrZfttddb_oYb3zsiUGN_tKBHRnZcHyz7CWINxOqOcYYKvHvHIopJG1lNU-9KoZ44S9N3KGPyFsHjf2KeC6w47R0RMDBfirmMhodWVCo4C9KC1ebt_-IBWeyn2mJA4OhQBsnJAuhFuhnXRtoYI156qmZZSq0mWfl6xtzuxcc6bV7rH_l3ZfivUsS-vPzgoMT8rmn5elNvT55IV7fYj57x2G2UNpGeuxp8Xg5_gM-uu4xJmyRlzd9FLp3kyqpz9nMScNlLQEkZ7MPtGB-xZvbQMeP_a5Ly1wcIuY0A9z0Ml7cCk4mMCnGvvmauWYVPeu_NDaj8-pWC0Eh20v30CR20a", 1636732167)
            }
        }

        findViewById<View>(R.id.upload).setOnClickListener{
            val files = arrayListOf<COSFileBean>().apply {
                repeat(10) { add(COSFileBean(filePath.absolutePath, HashManager.getInstance().uuid)) }
            }

            fileServer.uploadFilesByTencentCOS(files, object : IProgress {
                override fun onProgress(progress: Int, currentSize: Long, totalSize: Long) {}
            }, object : ICOSFilesCallback {
                override fun onSuccess(fileBeans: MutableList<COSFileBean>?) {
                    fileBeans?.forEach { Log.e(TAG, "success: ${it.networkPath}"); }
                }

                override fun onError(errorMessage: String?) {
                    Log.e(TAG, "" + errorMessage);
                }
            })
        }
    }
}