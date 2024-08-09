package com.dullfan.common.constant;

import com.sun.jna.platform.win32.WinDef;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 通用常量
 */

public class Constants {
    /**
     * 通用分隔符
     */
    public static final String COMMON_SEPARATOR = "__,__";
    /**
     * 点常量
     */
    public static final String POINT_STR = ".";
    /**
     * 空字符串
     */
    public static final String EMPTY_STR = StringUtils.EMPTY;
    /**
     * 斜杠字符串
     */
    public static final String SLASH_STR = "/";
    /**
     * 斜杆字符串
     */
    public static final Long ZERO_LONG = 0L;
    /**
     * Int 0
     */
    public static final Integer ZERO_INT = 0;
    /**
     * Int 1
     */
    public static final Integer ONE_INT = 1;
    /**
     * Int 2
     */
    public static final Integer TWO_INT = 2;
    /**
     * Int -1
     */
    public static final Integer MINUS_ONE_INT = -1;
    /**
     * true字符串
     */
    public static final String TURE_STR = "ture";
    /**
     * false字符串
     */
    public static final String FALSE_STR = "false";

    /**
     * 组件扫描基础路径
     */
    public static final String BASE_COMPONENT_SCAN_PATH = "com.imooc.pan";

    /**
     * 问号常量
     */
    public static final String QUESTION_MARK_STR = "?";

    /**
     * 等号常量
     */
    public static final String EQUALS_MARK_STR = "=";

    /**
     * 逻辑与常量
     */
    public static final String AND_MARK_STR = "&";

    /**
     * 左中括号常量
     */
    public static final String LEFT_BRACKET_STR = "[";

    /**
     * 右中括号常量
     */
    public static final String RIGHT_BRACKET_STR = "]";

    /**
     * 公用加密字符串
     */
    public static final String COMMON_ENCRYPT_STR = "****";
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 15;


    /**
     * 分享码Token有效时间（天）
     */
    public static final Integer SHARE_CODE_EXPIRATION = 1;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;


    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";


    /**
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    public static final String[] JSON_WHITELIST_STR = {"org.springframework", "com.dullfan"};
}
