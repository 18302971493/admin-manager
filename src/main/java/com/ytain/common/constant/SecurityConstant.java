package com.ytain.common.constant;

/**
 * @author Exrickx
 */
public interface SecurityConstant {

    /**
     * token分割
     */
    String TOKEN_SPLIT = "Bearer ";

    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "admin";

    /**
     * token参数头
     */
    String HEADER = "accessToken";

    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";

    /**
     * 用户选择JWT保存时间参数头
     */
    String SAVE_LOGIN = "saveLogin";

    /**
     * github保存state前缀key
     */
    String GITHUB_STATE = "ADMIN_GITHUB:";

    /**
     * qq保存state前缀key
     */
    String QQ_STATE = "ADMIN_QQ:";

    /**
     * qq保存state前缀key
     */
    String WEIBO_STATE = "ADMIN_WEIBO:";

    /**
     * 交互token前缀key
     */
    String TOKEN_PRE = "ADMIN_TOKEN_PRE:";

    /**
     * 用户token前缀key 单点登录使用
     */
    String USER_TOKEN = "ADMIN_USER_TOKEN:";
}
