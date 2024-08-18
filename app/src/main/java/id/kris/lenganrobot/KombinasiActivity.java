package id.kris.lenganrobot;

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
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KombinasiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kombinasi);

        Button buttonback, buttonstart;
        ImageView menuKombinasi, bantuanKombinasi;
        TextView statuskombinasi;

        buttonback = findViewById(R.id.exit);
        buttonstart = findViewById(R.id.playcombination);
        statuskombinasi = findViewById(R.id.status2);
        menuKombinasi = findViewById(R.id.menu);
        bantuanKombinasi = findViewById(R.id.bantuan);

        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(KombinasiActivity.this, KontrolActivity.class);
                startActivity(back);
            }
        });

        buttonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statuskombinasi.setText("   In Progress...");
                statuskombinasi.setTextColor(getResources().getColor(R.color.green));
                kombinasiGerakan(statuskombinasi);
            }
        });

        bantuanKombinasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KombinasiActivity.this);
                builder.setTitle("Tutorial (swipe right)");

                ViewPager viewPager = new ViewPager(KombinasiActivity.this);
                List<Integer> images = new ArrayList<>();
                images.add(R.drawable.kombinasi1);
                images.add(R.drawable.kombinasi2);
                images.add(R.drawable.kombinasi3);
                images.add(R.drawable.kombinasi4);
                ImagePagerAdapter adapter = new ImagePagerAdapter(KombinasiActivity.this, images);
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

        menuKombinasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void kombinasiGerakan(TextView statuskombinasi) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(() -> new PublishToSliderBaseKombinasi(KombinasiActivity.this).execute("30°"));
                    runOnUiThread(() -> new PublishToSliderGripKombinasi(KombinasiActivity.this).execute("90°"));
                    runOnUiThread(() -> new PublishToSliderElbowKombinasi(KombinasiActivity.this).execute("90°"));
                    runOnUiThread(() -> new PublishToSliderShoulderKombinasi(KombinasiActivity.this).execute("60°"));
                    runOnUiThread(() -> new PublishToSliderGripKombinasi(KombinasiActivity.this).execute("180°"));
                    runOnUiThread(() -> new PublishToSliderShoulderKombinasi(KombinasiActivity.this).execute("0°"));
                    runOnUiThread(() -> new PublishToSliderElbowKombinasi(KombinasiActivity.this).execute("0°"));
                    runOnUiThread(() -> new PublishToSliderBaseKombinasi(KombinasiActivity.this).execute("150°"));
                    runOnUiThread(() -> new PublishToSliderElbowKombinasi(KombinasiActivity.this).execute("90°"));
                    runOnUiThread(() -> new PublishToSliderShoulderKombinasi(KombinasiActivity.this).execute("60°"));
                    runOnUiThread(() -> new PublishToSliderGripKombinasi(KombinasiActivity.this).execute("90°"));
                    runOnUiThread(() -> new PublishToSliderShoulderKombinasi(KombinasiActivity.this).execute("10°"));
                    runOnUiThread(() -> new PublishToSliderElbowKombinasi(KombinasiActivity.this).execute("0°"));
                    runOnUiThread(() -> new PublishToSliderBaseKombinasi(KombinasiActivity.this).execute("90°"));
                    runOnUiThread(() -> new PublishToSliderGripKombinasi(KombinasiActivity.this).execute("180°"));
                    Thread.sleep(25000);
                    // Setelah semua gerakan selesai, update statuskombinasi
                    runOnUiThread(() -> {
                        statuskombinasi.setText("   Stand By");
                        statuskombinasi.setTextColor(getResources().getColor(R.color.Red));
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delay:
                        // Tindakan untuk menu Delay Test
                        Intent tesdelay = new Intent(KombinasiActivity.this, TesDelay.class);
                        startActivity(tesdelay);
                        return true;
                    case R.id.kontrol:
                        // Tindakan untuk menu Combination Test
                        Intent kombinasi = new Intent(KombinasiActivity.this, KontrolActivity.class);
                        startActivity(kombinasi);
                        //kombinasiGerakan();
                        return true;
                    case R.id.bantuan:
                        AlertDialog.Builder builder = new AlertDialog.Builder(KombinasiActivity.this);
                        builder.setTitle("Tutorial (swipe right)");

                        ViewPager viewPager = new ViewPager(KombinasiActivity.this);
                        List<Integer> images = new ArrayList<>();
                        images.add(R.drawable.kombinasi1);
                        images.add(R.drawable.kombinasi2);
                        images.add(R.drawable.kombinasi3);
                        images.add(R.drawable.kombinasi4);
                        ImagePagerAdapter adapter = new ImagePagerAdapter(KombinasiActivity.this, images);
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
                        AlertDialog.Builder alert_back =new AlertDialog.Builder(KombinasiActivity.this);
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
        popup.inflate(R.menu.menu_kombinasi);
        popup.show();
    }
}
