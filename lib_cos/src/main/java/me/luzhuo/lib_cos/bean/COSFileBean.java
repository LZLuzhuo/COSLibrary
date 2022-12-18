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
package me.luzhuo.lib_cos.bean;

import android.net.Uri;

/**
 * Description: Tencent cos bean
 * @Author: Luzhuo
 * @Creation Date: 2021/11/12 23:33
 * @Copyright: Copyright 2021 Luzhuo. All rights reserved.
 **/
public class COSFileBean {
    /**
     * Original file path or file uri
     */
    private String filePath;

    /**
     * network file path
     */
    private String networkPath;

    /**
     * file name
     */
    private String fileNameKey;

    /**
     * this tag will not be processed in any way.
     */
    public Object tag;

    public COSFileBean(String filePath, String fileNameKey) {
        this(filePath, fileNameKey, null);
    }

    public COSFileBean(String filePath, String fileNameKey, Object tag) {
        this.filePath = filePath;
        this.fileNameKey = fileNameKey;
        this.tag = tag;
    }

    public COSFileBean(Uri fileUri, String fileNameKey) {
        this(fileUri, fileNameKey, null);
    }

    public COSFileBean(Uri fileUri, String fileNameKey, Object tag) {
        this(fileUri.toString(), fileNameKey, tag);
    }

    // ===== System us =====
    public String getFilePath() {
        return filePath;
    }

    public String getFileNameKey() {
        return fileNameKey;
    }

    public void setNetworkPath(String networkPath) {
        this.networkPath = networkPath;
    }
    // ===== System us =====

    public String getNetworkPath() {
        return networkPath;
    }

    @Override
    public String toString() {
        return "COSFileBean{" +
                "filePath='" + filePath + '\'' +
                ", networkPath='" + networkPath + '\'' +
                ", fileNameKey='" + fileNameKey + '\'' +
                ", tag=" + tag +
                '}';
    }
}
