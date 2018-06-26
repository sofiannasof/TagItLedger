package eu.ubitech.smenesidou.tagitledger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.evrythng.thng.resource.model.store.Thng;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import eu.ubitech.smenesidou.tagitledger.data.AppRemoteDataStore;
import eu.ubitech.smenesidou.tagitledger.data.TagItLedgerApplication;



public class MainActivity extends AppCompatActivity implements MainContract.View {

    public String KEY = "temperature";
    public String VALUE = "10.1";
    //TODO: replace the thngID you want to simulate scans
    public String TOPIC = "/thngs/thngID/properties/"+KEY;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Inject
    AppRemoteDataStore appRemoteDataStore;
    private MainContract.Presenter mPresenter;
    public MqttAndroidClient CLIENT;

    TextView dataReceived;
    private NotificationManager notifManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.putString(KEY,VALUE);
        editor.putString("TOPIC",TOPIC);
        editor.apply();

        /**
         * Set up Android CardView/RecycleView
         */
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CardAdapter mCardAdapter = new CardAdapter();
        mRecyclerView.setAdapter(mCardAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Scanned data simulation
                if(KEY.equals("temperature")) {
                    mPresenter.publish(TOPIC, "[{\"value\": " + VALUE + "}]", getApplicationContext());
                } else {
                    mPresenter.publish(TOPIC, "[{\"value\": \""+ VALUE +"\"}]",getApplicationContext());
                }

                Snackbar.make(view, "Publish Scanned Value", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TagItLedgerApplication.getAppComponent().inject(this);
        new MainPresenter(appRemoteDataStore, this);

        //MQTT
        CLIENT = mPresenter.mqttSetup(getBaseContext());
        mPresenter.mqttConnect();

        CLIENT.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d("token:",cause.toString());
            }

            //background notification
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("topic:" + topic, "message:" + message.toString());

                // Post to Nxt blockchain
                if(KEY.equals("temperature")) {
                    mPresenter.postTaggedData(message.toString());
                } else if (KEY.equals("owner") && VALUE.equals("manufacturer")){
                    mPresenter.issueAsset(message.toString());
                } else if (KEY.equals("owner")&& !VALUE.equals("manufacturer")) {
                    mPresenter.transferAsset(pref.getString("nxt",""));
                } else {
                    // on nxt
                }

                createNotification(message.toString());

                // Create thng object to show on cardview - simulate scan data
                Thng thng = new Thng();
                thng.setId("thngID");
                Map<String, Object> properties = new HashMap<>();
                properties.put(KEY, pref.getString(KEY, ""));
                thng.setProperties(properties);
                thng.setProduct("Beer");
                // Show card
                mCardAdapter.addData(thng);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("token:",token.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_digital_product_manufacturer) {
             KEY = "owner";
             VALUE = "manufacturer";
             TOPIC = "/thngs/thngID/properties/"+KEY;
             editor.putString(KEY,VALUE);
             editor.putString("TOPIC",TOPIC);
             editor.apply();
             System.out.println("VALUE"+ VALUE);
             return true;
        } else if (id == R.id.action_digital_product_retailer) {
             KEY = "owner";
             VALUE = "retailer";
             TOPIC = "/thngs/thngID/properties/"+KEY;
             editor.putString(KEY,VALUE);
             editor.putString("TOPIC",TOPIC);
             editor.apply();
             System.out.println("VALUE"+ VALUE);
             return true;
        } else if (id == R.id.action_dynamic_pricing) {
             KEY = "temperature";
             VALUE = "1.4";
             TOPIC = "/thngs/thngID/properties/"+KEY;
             editor.putString(KEY,VALUE);
             editor.putString("TOPIC",TOPIC);
             editor.apply();
             System.out.println("VALUE"+ VALUE);
             return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "onDestroy");
        mPresenter.unsubscribe();
    }

    public void showNotification(String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());
    }

    public void createNotification(String aMessage) {
        final int NOTIFY_ID = 1002;

        // There are hardcoding only for show it's just strings
        String name = "my_package_channel";
        String id = "my_package_channel_1"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle(this.getString(R.string.notification_title))  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(aMessage)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(aMessage))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(this);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle(this.getString(R.string.notification_title)) // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(aMessage)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(aMessage))
                    .setPriority(Notification.PRIORITY_HIGH);
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

    @Override
    public void updateNxtData(String data) {
        KEY = "nxt";
        VALUE = data.toString();
        TOPIC = "/thngs/thngID/properties/"+KEY;
        editor = pref.edit();
        editor.putString(KEY,VALUE);
        editor.putString("TOPIC",TOPIC);
        editor.apply();
        mPresenter.publish(TOPIC, "[{\"value\": \""+ data.toString()+"\"}]",getApplicationContext());
    }
}
