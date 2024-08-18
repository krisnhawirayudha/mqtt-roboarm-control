package id.kris.lenganrobot;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.util.UUID;

public class PublishToSliderBaseTest extends AsyncTask<String, Void, String> {

    private static final String TAG = "PublishToTopicTask";
    private TesDelay tesDelayActivity;

    public PublishToSliderBaseTest(TesDelay tesDelayActivity) {
        this.tesDelayActivity = tesDelayActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String message = params[0];

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

            client.toBlocking().publishWith()
                    .topic("sliderbase")
                    .payload(message.getBytes())
                    .send();

            return "Message successfully published to topic 'sliderbase'";
        } catch (Exception e) {
            Log.e(TAG, "Failed to publish message", e);
            return "Failed to publish message: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i(TAG, result);
        // Tambahkan timestamp setelah pesan berhasil dikirim
        tesDelayActivity.addTimestamp();
    }
}
