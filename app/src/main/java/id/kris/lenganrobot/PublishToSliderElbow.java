package id.kris.lenganrobot;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.util.UUID;

public class PublishToSliderElbow extends AsyncTask<String, Void, String> {

    private static final String TAG = "PublishToTopicElbow";
    private KontrolActivity kontrolActivity;

    public PublishToSliderElbow(KontrolActivity kontrolActivity) {this.kontrolActivity = kontrolActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String username = params[0];

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
                    .topic("sliderelbow")
                    .payload(username.getBytes())
                    .send();

            return "Username berhasil dipublish ke topik 'tingkatcahaya'";
        } catch (Exception e) {
            Log.e(TAG, "Gagal mempublish tingkatcahaya", e);
            return "Gagal mempublish tingkatcahaya" + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) { Log.i(TAG,result); }
}
