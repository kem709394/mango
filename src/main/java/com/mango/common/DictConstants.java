package com.mango.common;

/**
 * 数据字典映射
 */
@SuppressWarnings("ALL")
public interface DictConstants {

    String DICT_TYPE = "DICT_TYPE";
    String DICT_TYPE_SYS = "0";
    String DICT_TYPE_BUS = "1";

    String USE_STATE = "USE_STATE";
    String USE_STATE_DISABLED = "0";
    String USE_STATE_ENABLED = "1";

    String REQUEST_METHOD = "REQUEST_METHOD";
    String REQUEST_METHOD_GET = "GET";
    String REQUEST_METHOD_POST = "POST";
    String REQUEST_METHOD_PUT = "PUT";
    String REQUEST_METHOD_DELETE = "DELETE";

    String MENU_TYPE = "MENU_TYPE";
    String MENU_TYPE_CLN = "0";
    String MENU_TYPE_MEN = "1";
    String MENU_TYPE_BTN = "2";

    String MESSAGE_TYPE = "MESSAGE_TYPE";
    String MESSAGE_TYPE_SYS = "1";
    String MESSAGE_TYPE_BUS = "2";

    String MESSAGE_STATE = "MESSAGE_STATE";
    String MESSAGE_STATE_NRD = "0";
    String MESSAGE_STATE_RED = "1";
    String MESSAGE_STATE_DEL = "2";

    String TASK_STATE = "TASK_STATE";
    String TASK_STATE_STP = "0";
    String TASK_STATE_RUN = "1";

    String STORE_MODE = "STORE_MODE";
    String STORE_MODE_LOCAL = "local";
    String STORE_MODE_ALIYUN = "aliyun";
    String STORE_MODE_QCLOUD = "qcloud";

    String MAIL_STATE = "MAIL_STATE";
    String MAIL_STATE_NONE = "0";
    String MAIL_STATE_SEND = "1";
    String MAIL_STATE_CANCEL = "2";
    String MAIL_STATE_FAIL = "-1";
}
