package id.kris.lenganrobot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonkeluarr, buttonmulaii;
    ImageView lengangambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonkeluarr = findViewById(R.id.buttonkeluar);
        buttonmulaii = findViewById(R.id.buttonmulai);
        lengangambar = findViewById(R.id.gambarlengan);

        lengangambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kontrol = new Intent(MainActivity.this, KontrolActivity.class);
                startActivity(kontrol);
            }
        });

        buttonkeluarr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonmulaii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mulai = new Intent(MainActivity.this, KontrolActivity.class);
                startActivity(mulai);
            }
        });
    }
}