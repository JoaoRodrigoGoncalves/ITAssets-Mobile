package pt.itassets.lite.listeners;

import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

public interface MQTTMessageListener
{
    void onMQTTMessageRecieved(String message);
}
