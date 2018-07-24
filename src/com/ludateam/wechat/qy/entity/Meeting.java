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

    /**
     * 录入时间
     */
    private String  lrsj;

    /**
     * 修改时间
     */
    private String  xgsj;
    /**
     * 录入人员代码
     */
    private String  lrrydm;

    /**
     * 修改人员代码
     */
    private String  xgrydm;
    /**
     * 录入局领导
     */
    private String  jld;

    /**
     * 指派标记
     * N 不可指派  Y 可以指派
     */
    private String zpbj;

    private String zpzj;

    public String getZpzj() {
        return zpzj;
    }

    public void setZpzj(String zpzj) {
        this.zpzj = zpzj;
    }

    public String getZpbr() {
        return zpbr;
    }

    public void setZpbr(String zpbr) {
        this.zpbr = zpbr;
    }

    private String zpbr;
    /**
     * 查看标记
     * readed 代表该会议信息已读
     * noread 代表该会议信息未读
     */
    private String ckbj;

    public String getCkbj() {
        return ckbj;
    }

    public void setCkbj(String ckbj) {
        this.ckbj = ckbj;
    }

    public String getZpbj() {
        return zpbj;
    }

    public void setZpbj(String zpbj) {
        this.zpbj = zpbj;
    }

    public String getLrsj() {
        return lrsj;
    }

    public void setLrsj(String lrsj) {
        this.lrsj = lrsj;
    }

    public String getXgsj() {
        return xgsj;
    }

    public void setXgsj(String xgsj) {
        this.xgsj = xgsj;
    }

    public String getLrrydm() {
        return lrrydm;
    }

    public void setLrrydm(String lrrydm) {
        this.lrrydm = lrrydm;
    }

    public String getXgrydm() {
        return xgrydm;
    }

    public void setXgrydm(String xgrydm) {
        this.xgrydm = xgrydm;
    }

    public String getJld() {
        return jld;
    }

    public void setJld(String jld) {
        this.jld = jld;
    }

    public String getCxdx() {
        return cxdx;
    }

    public void setCxdx(String cxdx) {
        this.cxdx = cxdx;
    }

    /**
     * 修改出席对象
     */

    private String  cxdx;




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
