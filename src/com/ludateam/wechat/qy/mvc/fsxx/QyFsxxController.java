package com.ludateam.wechat.qy.mvc.fsxx;

import com.jfinal.log.Log;
import com.jfinal.qyweixin.sdk.api.ApiResult;
import com.jfinal.qyweixin.sdk.api.ConDepartmentApi;
import com.jfinal.qyweixin.sdk.api.ConUserApi;
import com.jfinal.qyweixin.sdk.api.SendMessageApi;
import com.jfinal.qyweixin.sdk.msg.send.QiYeTextMsg;
import com.jfinal.qyweixin.sdk.msg.send.Text;
import com.platform.annotation.Controller;
import com.platform.mvc.base.BaseController;

@Controller("/wechat/fsxx")
public class QyFsxxController extends BaseController {

	private static final Log log = Log.getLog(QyFsxxController.class);

	/**
	 * 列表
	 */
	public void index() {

		System.out.println("*****************************************");
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		System.out.println("*****************************************");

		ApiResult subDepts = ConDepartmentApi.getDepartment("1");
		System.out.println("徐汇区通讯录:" + subDepts.getJson());

		setAttr("depts", subDepts);

		render("/fsxx/list.html");
	}

	/**
	 * 查看
	 */
	public void view() {
		String deptid = getPara("deptid");
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		System.out.println("***部门代码***************"+deptid);
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		ApiResult userlistResult = ConUserApi.getDepartmentUserList(deptid, "0", "0");
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		
		renderJson(userlistResult.getJson());
	}

	/**
	 * 保存
	 */
	public void save() {

		String touser = getPara("touser");
		String msgtext = getPara("msgtext");
		
		QiYeTextMsg text = new QiYeTextMsg();
		text.setAgentid("4");
		text.setText(new Text(msgtext));
		text.setSafe("0");
		text.setTouser(touser);
		text.setToparty("|");
		
		ApiResult sendTextMsg = SendMessageApi.sendTextMsg(text);
		System.out.println("AAAAAAAAAAAAAAAAAAAa:" + sendTextMsg.getJson());
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		renderJson(sendTextMsg.getJson());
	}
}
