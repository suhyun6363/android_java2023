package kr.ac.duksung.dust;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("서울 구별 미세먼지");

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(null);
                makeRequest();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void makeRequest() {
        String url = "http://openapi.seoul.go.kr:8088/43684b4e5373756837346651414654/json/" +
                "ListAirQualityByDistrictService/1/25/";
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
                        println("에러 -> " + error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void println(String data) {
        textView.append(data + "\n");
    }

    public void parseJson(String json) {
        try {
            JSONObject object1 = new JSONObject(json);
            JSONObject object2 = object1.getJSONObject("ListAirQualityByDistrictService");
            JSONArray array = object2.getJSONArray("row");
            for(int i=0; i<array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String ku = obj.getString("MSRSTENAME");
                String pm10 = obj.getString("PM10");
                String grade = obj.getString("GRADE");
                println(ku + " / " + pm10 + " --> " + grade);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
