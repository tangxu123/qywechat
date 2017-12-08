package com.ludateam.wechat.qy.controller;

import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ConDepartmentApi;
import com.jfinal.qyweixin.sdk.api.ConTagApi;
import com.jfinal.qyweixin.sdk.api.ConUserApi;
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
@Controller("/wechat/user")
public class QyUserController extends BaseController {
    private static final Log log = Log.getLog(QyUserController.class);

	/**
	 * 获取部门成员(详情)
	 * @param departmentId 获取的部门id
	 * @param fetch_child 1/0：是否递归获取子部门下面的成员
	 * @param status 0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加，未填写则默认为4
	 * @return
	 */
    public void list() {
        String jsonStr = HttpKit.readData(getRequest());
        HashMap<String, String> map = FastJson.getJson().parse(jsonStr, HashMap.class);
        String department_id = map.get("department_id"); //获取的部门id
        String fetch_child = map.get("fetch_child"); //1/0：是否递归获取子部门下面的成员
        String status = map.get("status");
        ApiResult apiResult = ConUserApi.getDepartmentUserList(department_id, fetch_child, status);
        renderText(apiResult.getJson());
    }

	/**
	 * 获取部门成员
	 * @param departmentId 获取的部门id
	 * @param fetch_child 1/0：是否递归获取子部门下面的成员
	 * @param status 0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加，未填写则默认为4
	 * @return
	 */
    public void simplelist() {
        String jsonStr = HttpKit.readData(getRequest());
        HashMap<String, String> map = FastJson.getJson().parse(jsonStr, HashMap.class);
        String department_id = map.get("department_id"); //获取的部门id
        String fetch_child = map.get("fetch_child"); //1/0：是否递归获取子部门下面的成员
        String status = map.get("status"); 
        ApiResult apiResult = ConUserApi.getDepartmentUserSimpleList(department_id, fetch_child, status);
        renderText(apiResult.getJson());
    }
    
    public void departmentlist() {
        String jsonStr = HttpKit.readData(getRequest());
        HashMap<String, String> map = FastJson.getJson().parse(jsonStr, HashMap.class);
        String id = map.get("id");
        ApiResult apiResult = ConDepartmentApi.getDepartment(id); //部门id。获取指定部门及其下的子部门。 如果不填，默认获取全量组织架构
        renderText(apiResult.getJson());
    }
}
