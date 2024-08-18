package id.kris.lenganrobot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.material.slider.Slider;

public class TesDelay extends AppCompatActivity {
    private LinearLayout timestampContainer;
    private Button resetButton;
    private Slider baseslider;
    private Button inputbase, back;
    private TextView database;
    private ImageView menuDelay, bantuanDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tes_delay);

        timestampContainer = findViewById(R.id.timestampContainer);
        resetButton = findViewById(R.id.resetButton);
        baseslider = findViewById(R.id.slider1);
        inputbase = findViewById(R.id.inputnilaibase);
        database = findViewById(R.id.base2);
        back = findViewById(R.id.kembali);
        menuDelay = findViewById(R.id.menu);
        bantuanDelay = findViewById(R.id.bantuan);

        baseslider.setValue(0);
        baseslider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                // Update nilai slider baseslider saat berubah
                database.setText(String.format("%.0f°", value));
            }
        });

        bantuanDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TesDelay.this);
                builder.setTitle("Tutorial (swipe right)");

                ViewPager viewPager = new ViewPager(TesDelay.this);
                List<Integer> images = new ArrayList<>();
                images.add(R.drawable.delay1);
                images.add(R.drawable.delay2);
                images.add(R.drawable.delay3);
                ImagePagerAdapter adapter = new ImagePagerAdapter(TesDelay.this, images);
                viewPager.setAdapter(adapter);

                builder.setView(viewPager);
                builder.setCancelable(true);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                builder.show();

            }
        });

        menuDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        inputbase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tombol inputbase ditekan, kirim nilai dari TextView database ke MQTT
                String nilaibaseslidertest = database.getText().toString();
                new PublishToSliderBaseTest(TesDelay.this).execute(nilaibaseslidertest);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimestamps();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kembalikontrol = new Intent(TesDelay.this, KontrolActivity.class);
                startActivity(kembalikontrol);
            }
        });

        // Subscribe to MQTT topic to receive messages
        new SubscribeToSliderBaseTest(this).execute();
    }

    // Method untuk menambahkan timestamp saat mengirim pesan
    public void addTimestamp() {
        String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(new Date());

        TextView newTimestampTextView = new TextView(this);
        newTimestampTextView.setText("Sent: " + currentTimestamp);
        newTimestampTextView.setTextSize(18);
        newTimestampTextView.setPadding(0, 10, 0, 10);

        timestampContainer.addView(newTimestampTextView);
    }

    // Method untuk menambahkan timestamp saat menerima pesan
    public void addReceivedTimestamp() {
        String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(new Date());

        TextView newTimestampTextView = new TextView(this);
        newTimestampTextView.setText("Received: " + currentTimestamp);
        newTimestampTextView.setTextSize(18);
        newTimestampTextView.setPadding(0, 10, 0, 10);

        timestampContainer.addView(newTimestampTextView);
    }


    private void resetTimestamps() {
        timestampContainer.removeAllViews();
    }

    public void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.kontrol:
                        // Tindakan untuk menu Delay Test
                        Intent tesdelay = new Intent(TesDelay.this, KontrolActivity.class);
                        startActivity(tesdelay);
                        return true;
                    case R.id.combination:
                        // Tindakan untuk menu Delay Test
                        Intent kombinasi = new Intent(TesDelay.this, KombinasiActivity.class);
                        startActivity(kombinasi);
                        return true;
                    case R.id.bantuan:
                        AlertDialog.Builder builder = new AlertDialog.Builder(TesDelay.this);
                        builder.setTitle("Tutorial (swipe right)");

                        ViewPager viewPager = new ViewPager(TesDelay.this);
                        List<Integer> images = new ArrayList<>();
                        images.add(R.drawable.delay1);
                        images.add(R.drawable.delay2);
                        images.add(R.drawable.delay3);
                        ImagePagerAdapter adapter = new ImagePagerAdapter(TesDelay.this, images);
                        viewPager.setAdapter(adapter);

                        builder.setView(viewPager);
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        builder.show();
                        return true;
                    case R.id.exit:
                        AlertDialog.Builder alert_back =new AlertDialog.Builder(TesDelay.this);
                        alert_back.setIcon(R.drawable.lengan2);
                        alert_back.setTitle("Exit IoT RoboArm");
                        alert_back.setMessage("Close App?");
                        alert_back.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        alert_back.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                                .show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.delay_menu);
        popup.show();
    }
}
