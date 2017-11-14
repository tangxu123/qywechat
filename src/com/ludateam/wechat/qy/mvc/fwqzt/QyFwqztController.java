package com.ludateam.wechat.qy.mvc.fwqzt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.json.FastJson;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.utils.HttpUtils;
import com.ludateam.wechat.qy.entity.HostServer;
import com.ludateam.wechat.qy.vo.HostStatusRep;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

@Controller("/wechat/fwqzt")
public class QyFwqztController extends BaseController {

	private static final Log log = Log.getLog(QyFwqztController.class);

	/**
	 * 列表
	 */
	public void index() {

		String ord = getRequest().getParameter("ord");
		if (ord == null) {
			ord = "";
		}
		//https 有时候访问会报错
		//String requrl = "https://www.tax.sh.gov.cn/jkfw/api/v1.0/services/fwqzt/list";
		String requrl = "http://www.tax.sh.gov.cn/jkfw/api/v1.0/services/fwqzt/list";
		String reqdata = "param={\"ord\":\"" + ord + "\"}";
		//HashMap<String, String> headers = new HashMap<String, String>();
		//headers.put("Content-type", "application/json");
		
		String resText = HttpUtils.post(requrl, reqdata);
		
		HostStatusRep hostStatusRep = FastJson.getJson().parse(resText,
				HostStatusRep.class);
		String retcode = hostStatusRep.getRetcode();
		List<HostServer> hostList = new ArrayList<HostServer>();
		if ("00".equals(retcode)) {
			log.info("-------call shsw--------" + hostStatusRep.getRetmsg());
			hostList = hostStatusRep.getResult();
		} else {
			log.info("-------call shsw--------" + hostStatusRep.getRetmsg());
		}

		setAttr("hostList", hostList);
		render("/fwqzt/list.html");
	}
}
