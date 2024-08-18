package id.kris.lenganrobot;

import android.os.AsyncTask;
import android.util.Log;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SubscribeToSliderBaseTest extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "SubscribeToSliderBaseTest";
    private TesDelay tesDelayActivity;

    public SubscribeToSliderBaseTest(TesDelay tesDelayActivity) {
        this.tesDelayActivity = tesDelayActivity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Mqtt5Client client = Mqtt5Client.builder()
                    .identifier(UUID.randomUUID().toString())
                    .serverHost("b154cc6667e74aafa30a15667698f80e.s1.eu.hivemq.cloud")
                    .serverPort(8883)
                    .sslWithDefaultConfig()
                    .simpleAuth()
                    .username("lengankrisnha")
                    .password("Riders12".getBytes())
                    .applySimpleAuth()
                    .build();

            client.toBlocking().connect();

            client.toAsync().subscribeWith()
                    .topicFilter("sliderbase")
                    .callback(publish -> handleIncomingMessage(publish))
                    .send();
        } catch (Exception e) {
            Log.e(TAG, "Failed to subscribe to topic", e);
        }
        return null;
    }

    private void handleIncomingMessage(Mqtt5Publish publish) {
        String message = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
        Log.i(TAG, "Received message: " + message);

        // Run on UI thread to update timestamp
        tesDelayActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tesDelayActivity.addReceivedTimestamp();
            }
        });
    }
}
