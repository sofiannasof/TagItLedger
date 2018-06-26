package eu.ubitech.smenesidou.tagitledger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import eu.ubitech.smenesidou.tagitledger.data.AppRemoteDataStore;
import eu.ubitech.smenesidou.tagitledger.model.evt.Product;
import eu.ubitech.smenesidou.tagitledger.model.nxt.IssueAssetResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TaggedDataResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TimeResp;
import eu.ubitech.smenesidou.tagitledger.model.nxt.TransferAssetResp;
import eu.ubitech.smenesidou.tagitledger.mqtt.MqttService;
import eu.ubitech.smenesidou.tagitledger.mqtt.SocketFactory;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {
    private Subscription subscription;
    private AppRemoteDataStore appRemoteDataStore;
    private MainContract.View view;

    public MqttAndroidClient CLIENT;
    public MqttConnectOptions MQTT_CONNECTION_OPTIONS;

    public MainPresenter(AppRemoteDataStore appRemoteDataStore,
                         MainContract.View view) {
        this.appRemoteDataStore = appRemoteDataStore;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadBlockchainInfo() {
        if (appRemoteDataStore == null) {
            appRemoteDataStore = new AppRemoteDataStore();
        }

        subscription = appRemoteDataStore.getBlockchainStatus("getTime")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TimeResp>() {
                    @Override
                    public final void onCompleted() {
                        Log.d("nxt","completed");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("nxt",e.getMessage());
                    }

                    @Override
                    public void onNext(TimeResp timeresp) {
                        Log.d("nxt","transfer success");
                    }
                });
    }

    @Override
    public void postTaggedData(String message) {
        if (appRemoteDataStore == null) {
            appRemoteDataStore = new AppRemoteDataStore();
        }

        subscription = appRemoteDataStore.uploadTaggedData("uploadTaggedData", message, "Name", "Descriton", "tag", "channel", "0", "1", "bf0ced0472d8ba3df9e21808e98e61b34404aad737e2bae1778cebc698b40f37", "60")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TaggedDataResp>() {
                    @Override
                    public final void onCompleted() {
                        Log.d("nxt","completed");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("nxt",e.getMessage());
                    }

                    @Override
                    public void onNext(TaggedDataResp data) {
                        Log.d("nxt","transfer success");
                    }
                });
    }

    @Override
    public void issueAsset(String message) {
        if (appRemoteDataStore == null) {
            appRemoteDataStore = new AppRemoteDataStore();
        }

        subscription = appRemoteDataStore.issueAsset("issueAsset", "Beer", "Descriton", "0", "100000000", "bf0ced0472d8ba3df9e21808e98e61b34404aad737e2bae1778cebc698b40f37", "1","60", true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IssueAssetResp>() {
                    @Override
                    public final void onCompleted() {
                        Log.d("nxt","completed");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("nxt",e.getMessage());
                    }

                    @Override
                    public void onNext(IssueAssetResp data) {
                        Log.d("nxt","transfer success");
                        view.updateNxtData(data.getAssetID());
                    }
                });
    }

    @Override
    public void transferAsset(String asset) {
        if (appRemoteDataStore == null) {
            appRemoteDataStore = new AppRemoteDataStore();
        }

        subscription = appRemoteDataStore.transferAsset("transferAsset", asset,"0", "100000000", "bf0ced0472d8ba3df9e21808e98e61b34404aad737e2bae1778cebc698b40f37", "1","60", "NXT-X5JH-TJKJ-DVGC-5T2V8")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TransferAssetResp>() {
                    @Override
                    public final void onCompleted() {
                        Log.d("nxt","completed");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("nxt",e.getMessage());
                    }

                    @Override
                    public void onNext(TransferAssetResp data) {
                        Log.d("nxt","transfer success");
                    }
                });
    }

    @Override
    public void loadProducts() {
        if (appRemoteDataStore == null) {
            appRemoteDataStore = new AppRemoteDataStore();
        }

        subscription = appRemoteDataStore.getProducts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public final void onCompleted() {
                        Log.d("evt","completed");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e("evt",e.getMessage());
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        Log.d("evt","transfer success");
                    }
                });
    }

    @Override
    public void subscribe(int page) {

    }

    @Override
    public void unsubscribe() {
        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public MqttAndroidClient mqttSetup(Context context) {
        CLIENT = new MqttAndroidClient(context, BROKER, MqttClient.generateClientId());
        MQTT_CONNECTION_OPTIONS = new MqttConnectOptions();
        MQTT_CONNECTION_OPTIONS.setUserName(USERNAME);
        MQTT_CONNECTION_OPTIONS.setPassword(PASSWORD.toCharArray());
        MQTT_CONNECTION_OPTIONS.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1); //newest version


        /**
         * SSL broker requires a certificate to authenticate their connection
         * Certificate can be found in resources folder /res/raw/
         */
        if (BROKER.contains("ssl")) {
            SocketFactory.SocketFactoryOptions socketFactoryOptions = new SocketFactory.SocketFactoryOptions();
            try {
                socketFactoryOptions.withCaInputStream(context.getResources().openRawResource(R.raw.cert1));
                MQTT_CONNECTION_OPTIONS.setSocketFactory(new SocketFactory(socketFactoryOptions));
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException | KeyManagementException | UnrecoverableKeyException e) {
                e.printStackTrace();
            }
        }
        return CLIENT;
    }

    @Override
    public void mqttConnect() {
        try {
            final IMqttToken token = CLIENT.connect(MQTT_CONNECTION_OPTIONS);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("mqtt:", "connected, token:" + asyncActionToken.toString());
                    //Thng will received upon scan, we need them for simulation
                    subscribe("/thngs/thngID/properties/temperature", (byte) 1);
                    subscribe("/thngs/thngID/properties/owner", (byte) 1);
                    subscribe("/thngs/thngID/properties/nxt", (byte) 1);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("mqtt:", "not connected" + asyncActionToken.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mqttDisconnect() {
        try {
            IMqttToken disconToken = CLIENT.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("mqtt:", "disconnected");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.d("mqtt:", "couldnt disconnect");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe(String topic, byte qos) {
        try {
            IMqttToken subToken = CLIENT.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("mqtt:", "subscribed" + asyncActionToken.toString());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.d("mqtt:", "subscribing error");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe(String topic) {
        try {
            IMqttToken unsubToken = CLIENT.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("mqtt:", "unsubcribed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.d("mqtt:", "couldnt unregister");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(String topic, final String msg, Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            CLIENT.publish(pref.getString("TOPIC",""),msg.getBytes(),1,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
