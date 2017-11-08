package com.ludateam.wechat.qy.vo;/*
 * Copyright 2017 Luda Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Created by Him on 2017/11/8.
 */

public class SubmitReq {
    private  String ApId;
    private  String EcName;
    private  String SecretKey;
    private  String Content;
    private  String Mobiles;
    private  String AddSerial;
    private  String Sign;
    private  String Mac;

    public String getApId() {
        return ApId;
    }

    public void setApId(String apId) {
        ApId = apId;
    }

    public String getEcName() {
        return EcName;
    }

    public void setEcName(String ecName) {
        EcName = ecName;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMobiles() {
        return Mobiles;
    }

    public void setMobiles(String mobiles) {
        Mobiles = mobiles;
    }

    public String getAddSerial() {
        return AddSerial;
    }

    public void setAddSerial(String addSerial) {
        AddSerial = addSerial;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }
}
