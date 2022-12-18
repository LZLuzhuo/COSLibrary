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

/**
 * Description: tencent cam bean
 * @Author: Luzhuo
 * @Creation Date: 2021/11/12 23:32
 * @Copyright: Copyright 2021 Luzhuo. All rights reserved.
 **/
public class COSCAMBean {

    private String SecretId;
    private String SecretKey;
    private String SessionToken;
    private long expiredTime = 1l;

    /**
     * 临时秘钥
     * @param SecretId SecretId
     * @param SecretKey SecretKey
     * @param SessionToken SessionToken
     * @param expiredTime 临时秘钥有效截止时间戳, 单位s
     */
    public COSCAMBean(String SecretId, String SecretKey, String SessionToken, long expiredTime) {
        this.SecretId = SecretId;
        this.SecretKey = SecretKey;
        this.SessionToken = SessionToken;
        this.expiredTime = expiredTime;
    }

    public String getSecretId() {
        return SecretId;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public String getSessionToken() {
        return SessionToken;
    }

    public long getExpiredTime() {
        return expiredTime;
    }
}
