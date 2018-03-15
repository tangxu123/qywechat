package com.ludateam.wechat.qy.controller;

import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ConDepartmentApi;
import com.jfinal.qyweixin.sdk.api.ConTagApi;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

import java.util.HashMap;

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
 * Created by Him on 2017/9/11.
 */
@Controller("/wechat/department")
public class QyDepartmentController extends BaseController {
	private static final Log log = Log.getLog(QyTagController.class);

	public void create() {
		String jsonStr = HttpKit.readData(getRequest());
		ApiResult apiResult = ConDepartmentApi.createDepartment(jsonStr);
		renderText(apiResult.getJson());
	}

	public void update() {
		String jsonStr = HttpKit.readData(getRequest());
		ApiResult apiResult = ConDepartmentApi.updateDepartment(jsonStr);
		renderText(apiResult.getJson());
	}

	public void delete() {
		String id = HttpKit.readData(getRequest());
		ApiResult apiResult = ConDepartmentApi.deleteDepartment(id);
		renderText(apiResult.getJson());
	}

	public void list() {
		String id = HttpKit.readData(getRequest());
		ApiResult apiResult = ConDepartmentApi.getDepartment(id);
		renderText(apiResult.getJson());
	}
}
