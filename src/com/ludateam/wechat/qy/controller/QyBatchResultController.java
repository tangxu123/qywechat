package com.ludateam.wechat.qy.controller;

import java.util.HashMap;

import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ConBatchApi;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

@Controller({ "/wechat/batch/result" })
public class QyBatchResultController extends BaseController {
	
	private static final Log log = Log.getLog(QyBatchResultController.class);

	public void index() {
		String jsonStr = HttpKit.readData(getRequest());
		HashMap map = (HashMap) FastJson.getJson().parse(jsonStr, HashMap.class);
		ApiResult apiResult = ConBatchApi.batchGetResult((String) map.get("jobid"));
		log.info(apiResult.getJson());
		renderText(apiResult.getJson());
	}
}