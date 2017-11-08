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
 * Created by Him on 2017/9/29.
 */

import com.jfinal.kit.JsonKit;

public class AjaxResult {
    // 标记成功失败，默认0：成功，1：失败、用于alert，2：失败、用于confirm
    private int code = 0;

    // 返回的中文消息
    private String message;

    // 成功时携带的数据
    private Object data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // 校验错误
    public boolean hasError() {
        return this.code != 0;
    }

    // 添加错误，用于alertError
    public AjaxResult addError(String message) {
        this.message = message;
        this.code = 1;
        return this;
    }

    /**
     * 用于Confirm的错误信息
     * @param addConfirmError
     * @return AjaxResult
     */
    public AjaxResult addConfirmError(String message) {
        this.message = message;
        this.code = 2;
        return this;
    }

    /**
     * 封装成功时的数据
     * @param data
     * @return AjaxResult
     */
    public AjaxResult success(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JsonKit.toJson(this);
    }
}
