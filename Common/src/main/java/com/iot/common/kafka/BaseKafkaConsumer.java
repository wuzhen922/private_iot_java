package com.iot.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by zc on 16-8-8.
 */
public class BaseKafkaConsumer implements Runnable{

    private BaseKafkaConsumer(){}
    private static BaseKafkaConsumer instance;
    public static BaseKafkaConsumer getInstance(){
        if(instance==null){
            synchronized (BaseKafkaConsumer.class){
                if (instance==null){
                    instance = new BaseKafkaConsumer();
                }
            }
        }
        return instance;
    }

    private KafkaConsumer<Short, KafkaMsg> consumer;
    private boolean hasInited = false;
    private KafkaProcessor processor;
    public void init(Properties prop, String[] topics, KafkaProcessor processor) throws IOException {
        if(hasInited){
            return;
        }
        if(processor==null){
            throw new NullPointerException("processor = null makes no sense");
        }
        this.processor = processor;
        prop.setProperty("key.deserializer","com.iot.common.kafka.ShortDeserializer");
        prop.setProperty("value.deserializer","com.iot.common.kafka.KafkaMsgDeserializer");
        consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Arrays.asList(topics));
        hasInited = true;
    }

    public interface KafkaProcessor{
        void process(String topic,Short key,KafkaMsg value);
    }

    private boolean isRunning = false;
    @Override
    public void run(){
        isRunning = true;
        while (isRunning && !Thread.interrupted()) {
            for (ConsumerRecord<Short, KafkaMsg> record : consumer.poll(100)) {
                processor.process(record.topic(),record.key(),record.value());
            }
        }
        if(consumer!=null){
            consumer.close();
        }
    }

    public void close(){
        isRunning = false;
    }
}
