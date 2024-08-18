package id.kris.lenganrobot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.List;

public class KontrolActivity extends AppCompatActivity {

    Slider baseslider, shoulderslider, elbowslider;
    Button inputbase, buttonkontrolgrip, buttonDecrease, buttonIncrease, kontrolnilaisudut;
    TextView database, datashoulder, dataelbow, datagrip;
    RadioGroup sliderOptions;
    ImageView menuImageView, bantuanKontrol;
    Switch gripswitch;
    boolean isDecreasing = false;
    boolean isIncreasing = false;

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable repeatAction = new Runnable() {
        @Override
        public void run() {
            if (isDecreasing) {
                adjustSliderValue(-1);
                handler.postDelayed(this, 100); // Adjust the interval here (milliseconds)
            } else if (isIncreasing) {
                adjustSliderValue(1);
                handler.postDelayed(this, 100); // Adjust the interval here (milliseconds)
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontrol);

        baseslider = findViewById(R.id.slider1);
        shoulderslider = findViewById(R.id.slider2);
        elbowslider = findViewById(R.id.slider3);
        database = findViewById(R.id.base2);
        datashoulder = findViewById(R.id.shoulder2);
        dataelbow = findViewById(R.id.elbow2);
        datagrip = findViewById(R.id.grip4);
        inputbase = findViewById(R.id.inputnilaibase);
        sliderOptions = findViewById(R.id.sliderOptions);
        buttonDecrease = findViewById(R.id.buttonDecrease);
        buttonIncrease = findViewById(R.id.buttonIncrease);
        menuImageView = findViewById(R.id.menu);
        gripswitch = findViewById(R.id.grip_switch);
        buttonkontrolgrip = findViewById(R.id.kontrolgrip);
        bantuanKontrol = findViewById(R.id.bantuan);
        kontrolnilaisudut = findViewById(R.id.kontrolinput);

        loadSliderValues();

        bantuanKontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KontrolActivity.this);
                builder.setTitle("Tutorial (swipe right)");

                ViewPager viewPager = new ViewPager(KontrolActivity.this);
                List<Integer> images = new ArrayList<>();
                images.add(R.drawable.kontrol1);
                images.add(R.drawable.kontrol2);
                images.add(R.drawable.kontrol3);
                images.add(R.drawable.kontrol4);
                images.add(R.drawable.kontrol5);
                ImagePagerAdapter adapter = new ImagePagerAdapter(KontrolActivity.this, images);
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

        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        baseslider.setEnabled(true);
        shoulderslider.setEnabled(true);
        elbowslider.setEnabled(true);

        buttonDecrease.setOnTouchListener(new RepeatListener(-1));
        buttonIncrease.setOnTouchListener(new RepeatListener(1));

        buttonkontrolgrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gripswitch.setChecked(!gripswitch.isChecked());
            }
        });

        gripswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Action when switch is ON
                    new PublishToSliderGrip(KontrolActivity.this).execute("0°");
                    datagrip.setText("Grip Opened");
                    datagrip.setTextColor(getResources().getColor(R.color.green));
                } else {
                    // Action when switch is OFF
                    new PublishToSliderGrip(KontrolActivity.this).execute("180°");
                    datagrip.setText("Grip Closed");
                    datagrip.setTextColor(getResources().getColor(R.color.Red));
                }
                saveSliderValues();
            }
        });

        kontrolnilaisudut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = sliderOptions.getCheckedRadioButtonId();
                String angleValue = "";
                String partName = "";

                switch (selectedId) {
                    case R.id.rbBase:
                        angleValue = String.format("%d°", (int) baseslider.getValue());
                        partName = "Base";
                        new PublishToSliderBase(KontrolActivity.this).execute(angleValue);
                        break;
                    case R.id.rbShoulder:
                        angleValue = String.format("%d°", (int) shoulderslider.getValue());
                        partName = "Shoulder";
                        new PublishToSliderShoulder(KontrolActivity.this).execute(angleValue);
                        break;
                    case R.id.rbElbow:
                        angleValue = String.format("%d°", (int) elbowslider.getValue());
                        partName = "Elbow";
                        new PublishToSliderElbow(KontrolActivity.this).execute(angleValue);
                        break;
                    case R.id.rbAll:
                        // Format pesan untuk mengontrol semua servos
                        String baseAngle = String.valueOf((int) baseslider.getValue());
                        String shoulderAngle = String.valueOf((int) shoulderslider.getValue());
                        String elbowAngle = String.valueOf((int) elbowslider.getValue());
                        String message = String.format("0 %s, 1 %s, 2 %s", baseAngle, shoulderAngle, elbowAngle);

                        partName = "Base, Shoulder, Elbow";
                        new PublishToSliderAll(KontrolActivity.this).execute(message);
                        break;
                }

                Toast.makeText(KontrolActivity.this, "Sudut " + partName + " " + angleValue + " telah dikirim", Toast.LENGTH_SHORT).show();
                saveSliderValues();
            }
        });
        baseslider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                database.setText(String.format("%d°", (int) value));
                saveSliderValues();
            }
        });

        shoulderslider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                datashoulder.setText(String.format("%d°", (int) value));
                saveSliderValues();
            }
        });

        elbowslider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                dataelbow.setText(String.format("%d°", (int) value));
                saveSliderValues();
            }
        });
    }

    private void adjustSliderValue(int adjustment) {
        int selectedId = sliderOptions.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.rbBase:
                int baseValue = (int) baseslider.getValue() + adjustment;
                if (baseValue >= 0 && baseValue <= 180) {
                    baseslider.setValue(baseValue);
                    database.setText(String.format("%d°", baseValue));
                }
                break;
            case R.id.rbShoulder:
                int shoulderValue = (int) shoulderslider.getValue() + adjustment;
                if (shoulderValue >= 0 && shoulderValue <= 180) {
                    shoulderslider.setValue(shoulderValue);
                    datashoulder.setText(String.format("%d°", shoulderValue));
                }
                break;
            case R.id.rbElbow:
                int elbowValue = (int) elbowslider.getValue() + adjustment;
                if (elbowValue >= 0 && elbowValue <= 180) {
                    elbowslider.setValue(elbowValue);
                    dataelbow.setText(String.format("%d°", elbowValue));
                }
                break;
        }
        saveSliderValues();
    }
    private void saveSliderValues() {
        SharedPreferences preferences = getSharedPreferences("sliderValues", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("base", (int) baseslider.getValue());
        editor.putInt("shoulder", (int) shoulderslider.getValue());
        editor.putInt("elbow", (int) elbowslider.getValue());
        editor.apply();
    }

    private void loadSliderValues() {
        SharedPreferences preferences = getSharedPreferences("sliderValues", MODE_PRIVATE);
        int baseValue = preferences.getInt("base", 90); // Default value is 90
        int shoulderValue = preferences.getInt("shoulder", 90); // Default value is 90
        int elbowValue = preferences.getInt("elbow", 90); // Default value is 90

        baseslider.setValue(baseValue);
        shoulderslider.setValue(shoulderValue);
        elbowslider.setValue(elbowValue);

        database.setText(String.format("%d°", baseValue));
        datashoulder.setText(String.format("%d°", shoulderValue));
        dataelbow.setText(String.format("%d°", elbowValue));
    }

    class RepeatListener implements View.OnTouchListener {
        private int adjustment;
        private static final int INITIAL_INTERVAL = 400; // Interval awal (milliseconds)
        private static final int REPEAT_INTERVAL = 100; // Interval pengulangan (milliseconds)
        private static final int LONG_PRESS_THRESHOLD = 2000; // Threshold untuk long press (milliseconds)
        private Handler handler = new Handler(Looper.getMainLooper());
        private Runnable repeatAction = new Runnable() {
            @Override
            public void run() {
                adjustSliderValue(adjustment);
                handler.postDelayed(this, REPEAT_INTERVAL);
            }
        };

        RepeatListener(int adjustment) {
            this.adjustment = adjustment;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacksAndMessages(null); // Hapus semua callbacks yang tertunda
                    if (v.getId() == R.id.buttonDecrease) {
                        adjustment = -1;
                    } else if (v.getId() == R.id.buttonIncrease) {
                        adjustment = 1;
                    }
                    handler.postDelayed(repeatAction, INITIAL_INTERVAL); // Mulai dengan interval awal
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (handler.hasCallbacks(repeatAction)) {
                                adjustment *= 10; // Perbesar penyesuaian setelah long press
                                handler.removeCallbacks(repeatAction);
                                handler.post(repeatAction);
                            }
                        }
                    }, LONG_PRESS_THRESHOLD);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacksAndMessages(null); // Hapus semua callbacks yang tertunda
                    break;
            }
            return true;
        }
    }

    public void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delay:
                        // Tindakan untuk menu Delay Test
                        Intent tesdelay = new Intent(KontrolActivity.this, TesDelay.class);
                        startActivity(tesdelay);
                        return true;
                    case R.id.combination:
                        // Tindakan untuk menu Combination Test
                        Intent kombinasi = new Intent(KontrolActivity.this, KombinasiActivity.class);
                        startActivity(kombinasi);
                        return true;
                    case R.id.bantuan:
                        AlertDialog.Builder builder = new AlertDialog.Builder(KontrolActivity.this);
                        builder.setTitle("Tutorial (swipe right)");

                        ViewPager viewPager = new ViewPager(KontrolActivity.this);
                        List<Integer> images = new ArrayList<>();
                        images.add(R.drawable.kontrol1);
                        images.add(R.drawable.kontrol2);
                        images.add(R.drawable.kontrol3);
                        images.add(R.drawable.kontrol4);
                        images.add(R.drawable.kontrol5);
                        ImagePagerAdapter adapter = new ImagePagerAdapter(KontrolActivity.this, images);
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
                    case R.id.logout:
                        AlertDialog.Builder alert_back = new AlertDialog.Builder(KontrolActivity.this);
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
        popup.inflate(R.menu.bottom_menu);
        popup.show();
    }
}
