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

import java.util.List;

import me.luzhuo.lib_cos.bean.COSFileBean;
import me.luzhuo.lib_cos.callback.ICOSFileCallback;
import me.luzhuo.lib_cos.callback.ICOSFilesCallback;
import me.luzhuo.lib_cos.callback.IProgress;

public interface ITencentCOSFileServer {
    /**
     * upload file to tencent cos
     *
     * @param fileBean
     * @param progress
     * @param callback
     */
    public void uploadFileByTencentCOS(final COSFileBean fileBean, IProgress progress, final ICOSFileCallback callback);

    /**
     * upload files to tencent cos
     * @param fileBeans
     * @param iProgress
     * @param callback
     */
    public void uploadFilesByTencentCOS(final List<COSFileBean> fileBeans, final IProgress iProgress, final ICOSFilesCallback callback);
}
