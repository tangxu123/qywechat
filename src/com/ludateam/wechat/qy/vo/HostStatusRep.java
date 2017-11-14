package com.ludateam.wechat.qy.vo;

import java.util.ArrayList;
import java.util.List;

import com.ludateam.wechat.qy.entity.HostServer;

public class HostStatusRep {

	/** 返回代码 */
	private String retcode = "";
	/** 返回结果描述 */
	private String retmsg = "";
	/** 返回内容 */
	private List<HostServer> result = new ArrayList<HostServer>();

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public List<HostServer> getResult() {
		return result;
	}

	public void setResult(List<HostServer> result) {
		this.result = result;
	}
}
