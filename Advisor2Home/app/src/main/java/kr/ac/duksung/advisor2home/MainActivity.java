package kr.ac.duksung.advisor2home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.advisor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nativeappItem:
                Toast.makeText(getApplicationContext(), "Native", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), ProfessorActivity.class);
                startActivity(intent1);
                return true;
            case R.id.webappItem:
                Uri uri = Uri.parse("http://203.252.213.36:8080/FinalProject/advisorForm.jsp");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent2);
                return true;
        }
        return false;
    }
}