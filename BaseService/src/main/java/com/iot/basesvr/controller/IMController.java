package com.iot.basesvr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.iot.basesvr.annotation.Cmd;
import com.iot.basesvr.service.IMService;
import com.iot.common.constant.Cmds;
import com.iot.common.constant.Topics;
import com.iot.common.kafka.BaseKafkaProducer;
import com.iot.common.model.KafkaMsg;
import com.iot.common.util.JsonUtil;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zc on 16-8-26.
 */
@Controller
public class IMController {

    @Resource
    private IMService imService;

    @Cmd(value = Cmds.CMD_ADD_DEVICE)
    public byte[] doAddDevice(KafkaMsg.KafkaMsgPb param) throws Exception {
        JSONObject data = JsonUtil.bytes2Json(param.getData().toByteArray());
        JSONObject result;
        if (data != null) {
            result = imService.addDevice(param.getClientId(),data.getString("deviceId"));
        } else {
            result = imService.addDevice(param.getClientId(),null);
        }
        return JsonUtil.json2Bytes(result);
    }

    @Cmd(value = Cmds.CMD_DEL_DEVICE)
    public byte[] doDelDevice(KafkaMsg.KafkaMsgPb param) throws Exception {
        JSONObject data = JsonUtil.bytes2Json(param.getData().toByteArray());
        JSONObject result;
        if (data != null) {
            result = imService.delDevice(param.getClientId(),data.getString("deviceId"));
        } else {
            result = imService.delDevice(param.getClientId(),null);
        }
        return JsonUtil.json2Bytes(result);
    }

    @Cmd(value = Cmds.CMD_IM)
    public void doIm(KafkaMsg.KafkaMsgPb param) throws Exception{
        JSONObject imData = JsonUtil.bytes2Json(param.getData().toByteArray());
        if (imData==null){
            return;
        }
        KafkaMsg.KafkaMsgPb.Builder imBuilder = KafkaMsg.KafkaMsgPb.newBuilder();
        JSONArray jsonArray = new JSONArray();
        String to = imData.getString("to");
        JSONObject json = new JSONObject();
        json.put("from",param.getClientId());
        json.put("to",to);
        json.put("msg",imData.getString("msg"));
        json.put("msgid",param.getMsgId());
        jsonArray.add(json);
        imBuilder.setClientId(to);
        imBuilder.setData(ByteString.copyFrom(JsonUtil.json2Bytes(jsonArray)));
        imBuilder.setIsEncrypt(true);
        //put to redis
        imService.putImMsg(to,param.getMsgId(),json.toJSONString());
        //send to tcp server
        BaseKafkaProducer.getInstance().send(Topics.TOPIC_IM_RESP, Cmds.CMD_IM_PUSH, imBuilder);
    }

    @Cmd(value = Cmds.CMD_IM_PUSH)
    public void doImPush(KafkaMsg.KafkaMsgPb param){
        JSONArray jsonArray = JsonUtil.bytes2JsonArray(param.getData().toByteArray());
        if (jsonArray==null){
            return;
        }
        for (int i=0;i<jsonArray.size();i++){
            imService.removeImMsg(param.getClientId(),jsonArray.getJSONObject(i).getString("msgid"));
        }
    }

    @Cmd(value = Cmds.CMD_GET_IM_OFFLINE_MSG)
    public void doOfflineMsg(KafkaMsg.KafkaMsgPb param){
        Map<String,String> offlineMsgs = imService.getAllImMsg(param.getClientId());
        if (offlineMsgs!=null && !offlineMsgs.isEmpty()){
            KafkaMsg.KafkaMsgPb.Builder imBuilder = KafkaMsg.KafkaMsgPb.newBuilder();
            imBuilder.setClientId(param.getClientId());
            imBuilder.setIsEncrypt(true);
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<String,String> entry: offlineMsgs.entrySet()){
                jsonArray.add(JSON.parseObject(entry.getValue()));
            }
            imBuilder.setData(ByteString.copyFrom(JsonUtil.json2Bytes(jsonArray)));
            BaseKafkaProducer.getInstance().send(Topics.TOPIC_IM_RESP, Cmds.CMD_IM_PUSH, imBuilder);
        }
    }
}
