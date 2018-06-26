package eu.ubitech.smenesidou.tagitledger.mqtt;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;

import eu.ubitech.smenesidou.tagitledger.R;

public interface MqttService {
    String BROKER = "ssl://mqtt.evrythng.com:8883";
    String USERNAME = "authorization";
    String PASSWORD = "place your authorisation"; //TODO: place your EVT key

    MqttAndroidClient mqttSetup(Context context);

    void mqttConnect();

    void mqttDisconnect();

    void subscribe(String topic, byte qos);

    void unsubscribe(String topic);

    void publish(String topic, String qos, Context context);
}
