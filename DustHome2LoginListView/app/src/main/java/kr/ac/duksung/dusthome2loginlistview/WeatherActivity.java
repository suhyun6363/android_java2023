package kr.ac.duksung.dusthome2loginlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AirActivity.class);
                startActivity(intent);
            }
        });

        makeRequest();
    }

    public void makeRequest() {
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd0600");
        String nowFormatted = dateFormat.format(now);

        Calendar startDayTime = Calendar.getInstance();
        startDayTime.set(Calendar.HOUR_OF_DAY, 0);
        startDayTime.set(Calendar.MINUTE, 0);
        startDayTime.set(Calendar.SECOND, 0);
        startDayTime.set(Calendar.MILLISECOND, 0);

        Calendar morningTime = Calendar.getInstance();
        morningTime.set(Calendar.HOUR_OF_DAY, 6);
        morningTime.set(Calendar.MINUTE, 0);
        morningTime.set(Calendar.SECOND, 0);
        morningTime.set(Calendar.MILLISECOND, 0);

        Calendar endDayTime = Calendar.getInstance();
        endDayTime.set(Calendar.HOUR_OF_DAY, 0);
        endDayTime.set(Calendar.MINUTE, 0);
        endDayTime.set(Calendar.SECOND, 0);
        endDayTime.set(Calendar.MILLISECOND, 0);
        endDayTime.add(Calendar.DAY_OF_MONTH, 1);

        if (now.after(morningTime.getTime()) && now.before(endDayTime.getTime())) {
            String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidFcst?serviceKey=a6jQW%2BATBaBB7AO0RCdEwEkq4kqdUcMq4OnSC5MG8oOkjeLNhT0tN17AX5XW%2FlIyCFAIDjjmY%2FYhGv%2BHtjot4Q%3D%3D"
                    + "&pageNo=1&numOfRows=10&dataType=json&stnId=109&tmFc=" + nowFormatted;

            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            parseJson(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "network error",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            );

            request.setShouldCache(false);
            MainActivity.requestQueue.add(request);
        } else if (now.after(startDayTime.getTime()) && now.before(morningTime.getTime())) {
            println("not available until 06:00");
        } else {
            println("갱신중입니다. 다시 접속해주세요.");
        }
    }

    public void println(String data) {
        textView.append(data + "\n");
    }

    public void parseJson(String jsonData) {
        try {
            JSONObject json = new JSONObject(jsonData);
            JSONObject response = json.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray itemArray = items.getJSONArray("item");
            JSONObject firstItem = itemArray.getJSONObject(0);
            String wfSv = firstItem.getString("wfSv");

            String[] lines = wfSv.split("\\n");
            for (String line : lines) {
                println(line);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}