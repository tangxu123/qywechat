package com.jfinal.qyweixin.sdk.msg.send;

/**
 * 企业文本卡片消息
 *
 * @author Javen
 */
public class QiYeTextCardMsg extends QiYeBaseMsg {

	/**
	 * 消息内容
	 */
	private TextCard textcard;

	public QiYeTextCardMsg() {
		this.msgtype = MessageType.textcard.name();
	}

	public QiYeTextCardMsg(TextCard textcard) {
		this.textcard = textcard;
		this.msgtype = MessageType.textcard.name();
	}

	public TextCard getTextcard() {
		return textcard;
	}

	public void setTextcard(TextCard textcard) {
		this.textcard = textcard;
	}

}