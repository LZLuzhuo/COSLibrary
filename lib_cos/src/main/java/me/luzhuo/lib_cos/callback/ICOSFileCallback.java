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
package me.luzhuo.lib_cos.callback;

import me.luzhuo.lib_cos.bean.COSFileBean;

/**
 * 单个文件请求回调
 */
public interface ICOSFileCallback {
    /**
     * 上传文件成功
     *
     * Note: 运行于子线程
     * @param fileBean
     */
    public void onSuccess(COSFileBean fileBean);

    /**
     * 上传文件失败
     *
     * Note: 运行于子线程
     * @param errorMessage
     */
    public void onError(String errorMessage);
}
