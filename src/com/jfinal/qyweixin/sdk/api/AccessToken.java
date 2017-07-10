package com.jfinal.qyweixin.sdk.api;

import java.io.Serializable;
import java.util.Map;

import com.jfinal.json.FastJson;
import com.jfinal.qyweixin.sdk.utils.JsonUtils;
import com.jfinal.qyweixin.sdk.utils.RetryUtils.ResultCheck;

/**
 * 封装 access_token
 */

public class AccessToken implements ResultCheck, Serializable {
    private static final long serialVersionUID = -822464425433824314L;

    private String access_token;    // 正确获取到 access_token 时有值
    private Integer expires_in;        // 正确获取到 access_token 时有值
    private Integer errcode;        // 出错时有值
    private String errmsg;            // 出错时有值

    private Long expiredTime;        // 正确获取到 access_token 时有值，存放过期时间
    private String json;

    public static void main(String[] args) {
        Map<String, Object> temp = JsonUtils.parse("{\"access_token\":\"TtTjVRQ7SODdeWLK_uq9AlYPIdl0NH1sWnew9Faj5htv1Hsg6b0lqD4flfOk2pov\",\"expires_in\":7200}", Map.class);
        System.out.println(temp);
    }

    public AccessToken(String jsonStr) {
        this.json = jsonStr;
        try {
            Map<String, Object> temp = FastJson.getJson().parse(jsonStr, Map.class); //  JsonUtils.parse(jsonStr, Map.class);
            access_token = (String) temp.get("access_token");
            expires_in = (Integer) temp.get("expires_in");
            errcode = (Integer) temp.get("errcode");
            if (errcode !=null && errcode == 0) {
            	errcode = null;
			}
            errmsg = (String) temp.get("errmsg");

            if (expires_in != null)
                expiredTime = System.currentTimeMillis() + ((expires_in -5) * 1000);
            // 用户缓存时还原
            if (temp.containsKey("expiredTime")) {
                 expiredTime = (Long) temp.get("expiredTime");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getJson() {
        return json;
    }

    public String getCacheJson() {
        Map<String, Object> temp = FastJson.getJson().parse(json, Map.class) ;//JsonUtils.parse(json, Map.class);
        temp.put("expiredTime", expiredTime);
        temp.remove("expires_in");
        return FastJson.getJson().toJson(temp);
    }

    public boolean isAvailable() {
        if (expiredTime == null)
            return false;
        if (errcode != null)
            return false;
        if (expiredTime < System.currentTimeMillis())
            return false;
        return access_token != null;
    }

    public String getAccessToken() {
        return access_token;
    }

    public Integer getExpiresIn() {
        return expires_in;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public Integer getErrorCode() {
        return errcode;
    }

    public String getErrorMsg() {
        if (errcode != null) {
            String result = ReturnCode.get(errcode);
            if (result != null)
                return result;
        }
        return errmsg;
    }

    @Override
    public boolean matching() {
        return isAvailable();
    }
}
