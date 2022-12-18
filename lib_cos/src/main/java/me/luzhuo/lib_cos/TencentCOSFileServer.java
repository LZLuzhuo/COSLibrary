/* Copyright 2021 Luzhuo. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.luzhuo.lib_cos;

import android.content.Context;
import android.net.Uri;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.luzhuo.lib_cos.bean.COSCAMBean;
import me.luzhuo.lib_cos.bean.COSFileBean;
import me.luzhuo.lib_cos.callback.ICOSFileCallback;
import me.luzhuo.lib_cos.callback.ICOSFilesCallback;
import me.luzhuo.lib_cos.callback.IProgress;
import me.luzhuo.lib_file.FileManager;


/**
 * Description: Tencent COS 文件上传服务
 * @Author: Luzhuo
 * @Creation Date: 2021/11/12 23:31
 * @Copyright: Copyright 2021 Luzhuo. All rights reserved.
 **/
public abstract class TencentCOSFileServer implements ITencentCOSFileServer {
    // --- Please set this information ---
    protected String region = "ap-shanghai"; // 存储桶所在的地域
    protected String bucketName = "cos-demo-1257757876"; // 桶名
    protected String cosPath = "temp/cos/"; // 对象存储在桶中的位置  temp/cos/

    /**
     * The default true is for testing only.
     */
    protected boolean isDefaultRequest;

    protected BasicLifecycleCredentialProvider camProvider;
    protected CosXmlSimpleService cosXmlService;
    protected TransferManager transferManager;
    private FileManager fileManager;

    /**
     * 用于测试
     * @param context
     */
    @Deprecated
    public TencentCOSFileServer(Context context) {
        isDefaultRequest = true;
        this.fileManager = new FileManager(context);

        initCAM();
        init(context);
    }

    public TencentCOSFileServer(Context context, String region, String bucketName, String cosPath) {
        isDefaultRequest = false;
        this.region = region;
        this.bucketName = bucketName;
        this.cosPath = cosPath;
        this.fileManager = new FileManager(context);

        initCAM();
        init(context);
    }

    protected void initCAM() {
        if (isDefaultRequest) {
            // 用于测试的永久秘钥
            final String SecretId = "AKID86rMaYFfohJglZ6MM5YkgLjqgFXYJn0o";
            final String SecretKey = "e5GCsKWS9bolSt51h8KGwNYVGnBJAk36";
            final long KeyDuration = 300; // 永久授权需要的 秘钥有效期, 单位s
            camProvider = new ShortTimeCredentialProvider(SecretId, SecretKey, KeyDuration);
        } else {
            // 生产环境的临时秘钥
            camProvider = new BasicLifecycleCredentialProvider() {
                @Override
                protected QCloudLifecycleCredentials fetchNewCredentials() {
                    // Run on child thread
                    final COSCAMBean cam = getTokenFromUser();
                    if (cam == null) return null;
                    return new SessionQCloudCredentials(cam.getSecretId(), cam.getSecretKey(), cam.getSessionToken(), cam.getExpiredTime());
                }
            };
        }
    }

    protected void init(Context context) {
        // init cos cam
        final CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
                .setDebuggable(true)
                .builder();
        cosXmlService = new CosXmlSimpleService(context.getApplicationContext(), serviceConfig, camProvider);

        // init cos
        final TransferConfig transferConfig = new TransferConfig.Builder().build();
        transferManager = new TransferManager(cosXmlService, transferConfig);
    }

    protected abstract COSCAMBean getTokenFromUser();

    @Override
    public void uploadFileByTencentCOS(COSFileBean fileBean, IProgress iProgress, ICOSFileCallback callback) {
        // 上传文件任务
        final COSXMLUploadTask cosxmlUploadTask;
        if (fileManager.isUriForFile(fileBean.getFilePath())) cosxmlUploadTask = transferManager.upload(bucketName, cosPath + fileBean.getFileNameKey(), Uri.parse(fileBean.getFilePath()), null/*不分块, 直传给null*/);
        else cosxmlUploadTask = transferManager.upload(bucketName, cosPath + fileBean.getFileNameKey(), fileBean.getFilePath(), null/*不分块, 直传给null*/);

        // 上传进度回调
        if (iProgress != null) {
            cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(long complete, long target) {
                    // 用于分块上传的 uploadId, 不分块, 直接传null
                    // String uploadId = cosxmlUploadTask.getUploadId();

                    int progress = (int) (100 * complete / target);
                    iProgress.onProgress(progress, complete, target);
                }
            });
        }

        // 返回结果回调
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
                // Run on child thread
                if (callback == null) return;
                COSXMLUploadTask.COSXMLUploadTaskResult cosXmlUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) cosXmlResult;
                fileBean.setNetworkPath(cosXmlUploadTaskResult.accessUrl);
                callback.onSuccess(fileBean);
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                // Run on child thread
                if (callback == null) return;
                if (clientException != null) { // 客户端异常
                    callback.onError(clientException.getMessage());
                } else { // 服务端异常
                    callback.onError(serviceException.getMessage());
                }
            }
        });
    }

    @Override
    public void uploadFilesByTencentCOS(List<COSFileBean> fileBeans, IProgress iProgress, ICOSFilesCallback callback) {
        final AtomicInteger currentSize = new AtomicInteger(0);
        final int totalSize = fileBeans.size();

        final List<COSFileBean> networkUrl = new ArrayList<>();
        final boolean[] isNormal = {true};

        for (COSFileBean fileBean : fileBeans) {

            final COSXMLUploadTask cosxmlUploadTask;
            if (fileManager.isUriForFile(fileBean.getFilePath())) cosxmlUploadTask = transferManager.upload(bucketName, cosPath + fileBean.getFileNameKey(), Uri.parse(fileBean.getFilePath()), null/*不分块, 直传给null*/);
            else cosxmlUploadTask = transferManager.upload(bucketName, cosPath + fileBean.getFileNameKey(), fileBean.getFilePath(), null/*不分块, 直传给null*/);

            cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
                    // Run on child thread
                    COSXMLUploadTask.COSXMLUploadTaskResult cosXmlUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) cosXmlResult;
                    fileBean.setNetworkPath(cosXmlUploadTaskResult.accessUrl);
                    networkUrl.add(fileBean);

                    int progress = (int) (100 * currentSize.addAndGet(1) / totalSize);
                    if(iProgress != null) iProgress.onProgress(progress, currentSize.get(), totalSize);

                    if (callback != null && isNormal[0] && currentSize.get() == totalSize) callback.onSuccess(fileBeans);
                }

                @Override
                public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    // Run on child thread
                    isNormal[0] = false;
                    if (callback != null) {
                        if (clientException != null) callback.onError(clientException.getMessage()); // 客户端异常
                        else callback.onError(serviceException.getMessage()); // 服务端异常
                    }
                }
            });
        }
    }
}
