package com.jfinal.qyweixin.sdk.jfinal;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.qyweixin.sdk.api.ApiConfigKit;
import com.jfinal.qyweixin.sdk.kit.SignatureCheckKit;

/**
 * Msg 拦截器
 * 1：通过 MsgController.getApiConfig() 得到 ApiConfig 对象，并将其绑定到当前线程之上(利用了 ApiConfigKit 中的 ThreadLocal 对象)
 * 2：响应开发者中心服务器配置 URL 与 Token 请求
 * 3：签名检测
 * 注意： MsgController 的继承类如果覆盖了 index 方法，则需要对该 index 方法声明该拦截器
 * 		因为子类覆盖父类方法会使父类方法配置的拦截器失效，从而失去本拦截器的功能
 */
public class MsgInterceptor implements Interceptor {
    private static CorpIdParser _parser = new CorpIdParser.DefaultParameterCorpIdParser();

    public static void setAppIdParser(CorpIdParser parser) {
        _parser = parser;
    }

	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		if (controller instanceof MsgController == false)
			throw new RuntimeException("控制器需要继承 MsgController");
		
		try {
            String corpId = _parser.getCorpId(controller);
			// 将 ApiConfig 对象与当前线程绑定，以便在后续操作中方便获取该对象： ApiConfigKit.getApiConfig();
			ApiConfigKit.setThreadLocalCorpId(corpId);
			
			// 如果是服务器配置请求，则配置服务器并返回
			if (isConfigServerRequest(controller)) {
				configServer(controller);
				return ;
			}
			
			 //对开发测试更加友好
			if (ApiConfigKit.isDevMode()) {
				inv.invoke();
			} else {
				inv.invoke();
				// 签名检测
//				if (checkSignature(controller)) {
//					inv.invoke();
//				}
//				else {
//					controller.renderText("签名验证失败，请确定是微信服务器在发送消息过来");
//				}
			}
			
		}
		finally {
			ApiConfigKit.removeThreadLocalCorpId();
		}
	}
	
	
	
	/**
	 * 是否为开发者中心保存服务器配置的请求
	 */
	private boolean isConfigServerRequest(Controller controller) {
		return StrKit.notBlank(controller.getPara("echostr"));
	}
	
	/**
	 * 配置开发者中心微信服务器所需的 url 与 token
	 * @return true 为config server 请求，false 正式消息交互请求
	 */
	public void configServer(Controller c) {
		// 通过 echostr 判断请求是否为配置微信服务器回调所需的 url 与 token
		String echostr = c.getPara("echostr");
		String signature = c.getPara("msg_signature");
		String timestamp = c.getPara("timestamp");
		String nonce = c.getPara("nonce");
		
		c.renderText(SignatureCheckKit.me.VerifyURL(signature, timestamp, nonce, echostr));
	}
}



