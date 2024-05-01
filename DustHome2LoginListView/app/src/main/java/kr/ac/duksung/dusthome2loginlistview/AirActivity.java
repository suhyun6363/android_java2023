package kr.ac.duksung.dusthome2loginlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirActivity extends AppCompatActivity {
    private TextView textView1, textView2;
    private List districts, airQualityInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);

        districts = new ArrayList<String>();
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView);
        ListView districtList = (ListView) findViewById(R.id.listView);

        updateTime();
        airQualityInfoList = new ArrayList();
        makeRequest();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, districts);
        districtList.setAdapter(adapter);

        districtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, String> selectedInfoMap = (Map<String, String>) airQualityInfoList.get(i);
                textView2.setText(null);
                println("오존: " + selectedInfoMap.get("오존"));
                println("이산화질소: " + selectedInfoMap.get("이산화질소"));
                println("일산화탄소: " + selectedInfoMap.get("일산화탄소"));
                println("아황산가스: " + selectedInfoMap.get("아황산가스"));
                println("미세먼지: " + selectedInfoMap.get("미세먼지"));
                println("초미세먼지: " + selectedInfoMap.get("초미세먼지"));
                println("통합대기환경지수 등급: " + selectedInfoMap.get("통합대기환경지수 등급"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                updateTime();
                districts.clear();
                airQualityInfoList.clear();
                makeRequest();
                textView2.setText(null);
                return true;
        }
        return false;
    }

    public void updateTime() {
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 기준");
        String nowFormatted = dateFormat.format(now);
        textView1.setText(nowFormatted);
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
                        Toast.makeText(getApplicationContext(), "network error",
                                Toast.LENGTH_LONG).show();
                    }
                }
        );

        request.setShouldCache(false);
        MainActivity.requestQueue.add(request);
    }

    public void println(String data) {
        textView2.append(data + "\n");
    }

    public void parseJson(String json) {
        try {
            JSONObject object1 = new JSONObject(json);
            JSONObject object2 = object1.getJSONObject("ListAirQualityByDistrictService");
            JSONArray array = object2.getJSONArray("row");
            for(int i=0; i<array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String gu = obj.getString("MSRSTENAME");
                districts.add(gu);
                Map<String, String> airQualityInfoMap = new HashMap<>();
                airQualityInfoMap.put("자치구", gu);
                airQualityInfoMap.put("오존", obj.getString("OZONE"));
                airQualityInfoMap.put("이산화질소", obj.getString("NITROGEN"));
                airQualityInfoMap.put("일산화탄소", obj.getString("CARBON"));
                airQualityInfoMap.put("아황산가스", obj.getString("SULFUROUS"));
                airQualityInfoMap.put("미세먼지", obj.getString("PM10"));
                airQualityInfoMap.put("초미세먼지", obj.getString("PM25"));
                String grade = obj.getString("GRADE");
                if (grade.equals("")) {
                    grade = "N/A";
                }
                airQualityInfoMap.put("통합대기환경지수 등급", grade);
                airQualityInfoList.add(airQualityInfoMap);
            }
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) ((ListView)findViewById(R.id.listView)).getAdapter();
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}