package com.ludateam.wechat.qy.entity;

public class HostServer {

	/** 主机序号 */
	private String ord;
	/** 主机描述 */
	private String hostDesc;
	/** 当前状态 */
	private String text;
	/** 报告时间 */
	private String reporttime;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHostDesc() {
		return hostDesc;
	}

	public void setHostDesc(String hostDesc) {
		this.hostDesc = hostDesc;
	}

	public String getOrd() {
		return ord;
	}

	public void setOrd(String ord) {
		this.ord = ord;
	}

	public String getReporttime() {
		return reporttime;
	}

	public void setReporttime(String reporttime) {
		this.reporttime = reporttime;
	}

}
