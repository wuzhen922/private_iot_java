package com.iot.basesvr.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zc on 16-8-9.
 */
public interface AccountService {
    JSONObject regist(String username, String password) throws Exception;
    JSONObject login(String username, String password) throws Exception;
}
