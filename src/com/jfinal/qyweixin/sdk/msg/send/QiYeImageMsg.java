package com.jfinal.qyweixin.sdk.msg.send;

/**
 * 图片消息
 *
 * @author Javen
 */
public class QiYeImageMsg extends QiYeBaseMsg {
    private String media_id;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String mediaId) {
        media_id = mediaId;
    }

    public QiYeImageMsg(String mediaId) {
        media_id = mediaId;
        this.msgtype = MessageType.image.name();
    }

    public QiYeImageMsg() {
        this.msgtype = MessageType.image.name();
    }

    private QiYeImage image;

    public QiYeImage getImage() {
        return image;
    }

    public void setImage(QiYeImage image) {
        this.image = image;
    }

    private String msgtype;

    @Override
    public String getMsgtype() {
        return "image";
    }

    @Override
    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
}
