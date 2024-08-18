//package id.kris.lenganrobot;
//
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
//
//import java.util.UUID;
//
//public class PublishToTopicShoulder extends AsyncTask<String, Void, String> {
//
//    private static final String TAG = "PublishToTopicTask";
//    private MainActivity mainActivity;
//
//    public PublishToTopicShoulder(MainActivity mainActivity) {
//        this.mainActivity = mainActivity;
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        try {
//            String username = params[0];
//
//            Mqtt5Client client = Mqtt5Client.builder()
//                    .identifier(UUID.randomUUID().toString())
//                    .serverHost("3050d84e162d45f39d5e75d157f241e9.s2.eu.hivemq.cloud")
//                    .serverPort(8883)
//                    .sslWithDefaultConfig()
//                    .simpleAuth()
//                    .username("lengankrisnha")
//                    .password("Riders12".getBytes())
//                    .applySimpleAuth()
//                    .build();
//
//            client.toBlocking().connect();
//
//            client.toBlocking().publishWith()
//                    .topic("shoulder")
//                    .payload(username.getBytes())
//                    .send();
//
//            client.toAsync().subscribeWith()
//                    .topicFilter("shoulder")
//                    .callback(publish -> {
//                        String shoulder = new String(publish.getPayloadAsBytes());
//                        Log.i(TAG, "Menerima sudut shoulder: " + shoulder);
//
//                        // Update UI dengan umur
//                        mainActivity.updateShoulderOnUI(shoulder);
//
//                        // Tampilkan pesan Toast saat menerima pesan
//                        mainActivity.showToast("Menerima pesan dari topik 'shoulder': " + shoulder);
//                    })
//                    .send();
//
//            // Disconnect jika diperlukan
//            // client.toBlocking().disconnect();
//
//            return "Username berhasil dipublish ke topik 'nama' dan subscribe ke topik 'umur'";
//        } catch (Exception e) {
//            Log.e(TAG, "Gagal mempublish username atau subscribe umur", e);
//            return "Gagal mempublish username atau subscribe umur: " + e.getMessage();
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        Log.i(TAG, result);
//    }
//}
