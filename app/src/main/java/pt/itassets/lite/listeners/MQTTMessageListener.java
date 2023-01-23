package pt.itassets.lite.listeners;


public interface MQTTMessageListener
{
    void onMQTTMessageRecieved(String message);
}
