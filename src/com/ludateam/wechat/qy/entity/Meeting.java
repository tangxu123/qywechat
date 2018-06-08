package com.ludateam.wechat.qy.entity;
/*
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
 * Created by Him on 2018/4/17.
 */



public class Meeting{
    /**
     * 会议编号
     */
    private String hybh;
    /**
     * 会议地点
     */
    private String hydd;
    /**
     * 会议名称
     */
    private String hymc;
    /**
     * 会议内容
     */
    private String hynr;
    /**
     * 开始日期
     */
    private String  ksrq;

    /**
     * 开始时间
     */
    private String  kssj;

    /**
     * 结束日期
     */
    private String  jsrq;

    /**
     * 结束时间
     */
    private String  jssj;


    public String getHybh() {
        return hybh;
    }

    public void setHybh(String hybh) {
        this.hybh = hybh;
    }

    public String getHydd() {
        return hydd;
    }

    public void setHydd(String hydd) {
        this.hydd = hydd;
    }

    public String getHymc() {
        return hymc;
    }

    public void setHymc(String hymc) {
        this.hymc = hymc;
    }

    public String getHynr() {
        return hynr;
    }

    public void setHynr(String hynr) {
        this.hynr = hynr;
    }

    public String getKsrq() {
        return ksrq;
    }

    public void setKsrq(String ksrq) {
        this.ksrq = ksrq;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJsrq() {
        return jsrq;
    }

    public void setJsrq(String jsrq) {
        this.jsrq = jsrq;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }
}
