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
package me.luzhuo.tencentcosdemo;

import android.content.Context;

import java.util.List;

import me.luzhuo.lib_cos.ITencentCOSFileServer;
import me.luzhuo.lib_cos.TencentCOSFileServer;
import me.luzhuo.lib_cos.bean.COSCAMBean;
import me.luzhuo.lib_cos.bean.COSFileBean;
import me.luzhuo.lib_cos.callback.ICOSFileCallback;
import me.luzhuo.lib_cos.callback.ICOSFilesCallback;
import me.luzhuo.lib_cos.callback.IProgress;
import me.luzhuo.lib_okhttp.IOKHttpManager;
import me.luzhuo.lib_okhttp.OKHttpManager;
import me.luzhuo.lib_okhttp.interceptor.TokenInterceptor;

/**
 * Tencent COS 文件上传工具类
 */
public class COSUtils {
    private static COSUtils instance;
    private ITencentCOSFileServer tencentCOSFileServer;
    private IOKHttpManager okHttpManager;

    private static final String CAMServer = COSAccount.CAMServer;
    private static final String region = COSAccount.region; // 存储桶所在的地域
    private static final String bucketName = COSAccount.bucketName; // 桶名
    private static final String cosPath = COSAccount.cosPath; // 对象存储在桶中的位置  temp/cos/

    public static COSUtils getInstance(Context context) {
        if (instance == null){
            synchronized (COSUtils.class){
                if (instance == null){
                    instance = new COSUtils(context);
                }
            }
        }
        return instance;
    }

    private COSUtils(Context context) {
        try {
            okHttpManager = new OKHttpManager(
                    new TokenInterceptor() {
                        @Override
                        public String getToken() {
                            return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzMzE2MyIsImlhdCI6MTYwNzc2NjMwMSwiZXhwIjoxNjA4MzcxMTAxfQ.P1-KMLC4xFhjQng9RxiujRazubv6SlxdU3KRT6OhcRrRGE7w4q2yR7VFANGkj7EieBUNXpZP_Pnf861UVPOH3w";
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

        tencentCOSFileServer = new TencentCOSFileServer(context, region, bucketName, cosPath) {
            @Override
            protected COSCAMBean getTokenFromUser() {
                try {
                    // String message = okHttpManager.get(CAMServer);

                    // TODO 解析json数据

                    return new COSCAMBean(COSAccount.SecretId, COSAccount.SecretKey, COSAccount.SessionToken, 1636734436);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public void uploadFileByTencentCOS(final COSFileBean fileBean, final IProgress progress, final ICOSFileCallback callback) {
        tencentCOSFileServer.uploadFileByTencentCOS(fileBean, progress, callback);
    }

    public void uploadFilesByTencentCOS(final List<COSFileBean> fileBeans, final IProgress progress, final ICOSFilesCallback callback) {
        tencentCOSFileServer.uploadFilesByTencentCOS(fileBeans, progress, callback);
    }
}
