package com.iot.client;

import com.iot.client.netty.DeviceHandler;
import com.iot.client.netty.NettyClient;

public class DeviceMain {

    public static void main(String[] args) throws Exception {
        NettyClient.start(new DeviceHandler());
    }

}
