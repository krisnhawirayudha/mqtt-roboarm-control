package id.kris.lenganrobot;

import android.os.AsyncTask;
import android.util.Log;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.util.UUID;
import java.util.zip.CRC32;

public class PublishToSliderBase extends AsyncTask<String, Void, String> {

    private static final String TAG = "PublishToTopicTask";
    private KontrolActivity kontrolActivity;

    public PublishToSliderBase(KontrolActivity kontrolActivity) {
        this.kontrolActivity = kontrolActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String username = params[0];
            String dataWithCRC = addCRCToPayload(username);

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
                    .payload(dataWithCRC.getBytes())
                    .send();

            return "Data berhasil dipublish ke topik 'sliderbase'";
        } catch (Exception e) {
            Log.e(TAG, "Gagal mempublish sliderbase", e);
            return "Gagal mempublish sliderbase: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i(TAG, result);
    }

    // Fungsi untuk menghitung CRC16
    public static int crc16(byte[] data) {
        int crc = 0xFFFF;

        for (int i = 0; i < data.length; i++) {
            crc ^= data[i] & 0xFF;

            for (int j = 0; j < 8; j++) {
                if ((crc & 1) != 0) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }

        return crc & 0xFFFF;
    }

    // Fungsi untuk menambahkan CRC ke payload
    public static String addCRCToPayload(String data) {
        byte[] dataBytes = data.getBytes();
        int crcValue = crc16(dataBytes);
        return data + "," + Integer.toHexString(crcValue);
    }
}