package com.iot.tcpserver;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.constant.Cmds;
import com.iot.common.constant.RespCode;
import com.iot.common.kafka.BaseKafkaConsumer;
import com.iot.common.model.BaseMsg;
import com.iot.common.model.KafkaMsg;
import com.iot.common.util.JsonUtil;
import com.iot.tcpserver.client.AppClient;
import com.iot.tcpserver.client.Client;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zc on 16-8-8.
 */
public class ServiceRespHandler implements BaseKafkaConsumer.KafkaProcessor{

    @Override
    public void process(String topic, Integer key, KafkaMsg.KafkaMsgPb value) {
        if(key==null || value==null){
            return;
        }

        for(int i=0;i<value.getChannelIdCount();i++){
            //channelId
            ChannelHandlerContext ctx = CtxPool.getContext(value.getChannelId(i));
            if(ctx==null){//不是此server
                continue;
            }
            if(key == Cmds.CMD_APP_AUTH){
                //server需要特殊处理，记录app auth信息
                doAppAuth(value,ctx);
            }else{
                //原样返回
                BaseMsg.BaseMsgPb.Builder baseMsg = BaseMsg.BaseMsgPb.newBuilder();
                baseMsg.setMsgId(value.getMsgId());
                baseMsg.setCmd(key);
                baseMsg.setData(value.getData());
                ctx.writeAndFlush(baseMsg);
            }
        }
    }

    private void doAppAuth(KafkaMsg.KafkaMsgPb value, ChannelHandlerContext ctx){
        JSONObject json = JsonUtil.bytes2Json(value.getData().toByteArray());
        if (json==null){
            return;
        }
        int statusCode = json.getIntValue("code");
        if(statusCode == RespCode.COMMON_OK){//login ok
            String username = json.getString("username");
            ChannelHandlerContext oldCtx = CtxPool.getClient(username);
            if(oldCtx!=null && !oldCtx.channel().id().asLongText().equals(ctx.channel().id().asLongText())){
                oldCtx.writeAndFlush(BaseMsg.BaseMsgPb.newBuilder().setCmd(Cmds.CMD_ANOTHOR_LOGIN));
                oldCtx.close();
            }

            Client client = new AppClient(username,json.getString("version"));
            ctx.channel().attr(ServerEnv.CLIENT).set(client);
            CtxPool.putClient(username,ctx);

            json.remove("version");
            json.remove("username");
        }
        BaseMsg.BaseMsgPb.Builder baseMsg = BaseMsg.BaseMsgPb.newBuilder();
        baseMsg.setMsgId(value.getMsgId());
        baseMsg.setCmd(Cmds.CMD_APP_AUTH);
        baseMsg.setData(value.getData());
        ctx.writeAndFlush(baseMsg);
    }
}
