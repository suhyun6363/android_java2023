package kr.ac.duksung.advisor2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;


public class MainActivity extends AppCompatActivity {
    RadioButton rdoWeb, rdoNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rdoWeb = (RadioButton) findViewById(R.id.radioButton1);
        rdoNative = (RadioButton) findViewById(R.id.radioButton2);

        rdoWeb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                startActivity(intent);
            }
        });

        rdoNative.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfessorActivity.class);
                startActivity(intent);
            }
        });
    }
}