package kr.ac.duksung.duksung;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ArrayList<String> items;
    ArrayList<String> dates;
    ArrayAdapter<String> adapter;
    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<String>();
        dates = new ArrayList<String>();
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        textView = (TextView) findViewById(R.id.textView);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        makeRequest();
    }

    public void makeRequest() {
        String url = "http://www.duksung.ac.kr/";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /* for Chosun page (homework)
                        try {
                            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            Toast.makeText(getApplicationContext(), "encoding error",
                                    Toast.LENGTH_LONG).show();
                        }
                        */
                        parseHtml(response);
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
        requestQueue.add(request);
    }

    public void parseHtml(String html) {
        Document doc = Jsoup.parse(html);
        String titleStr = doc.title();
        Elements itemElements = doc.select("ul li p.con");   // XPath '//ul/li/p[@class="con"]'
        Elements dateElements = doc.select("ul li p.date");  // XPath '//ul/li/p[@class="date"]'
        textView.setText(titleStr + " 일정");
        for(Element element : itemElements) {
            items.add(element.text().trim());
            android.util.Log.d("test: ", element.text());
        }
        for(Element element : dateElements) {
            dates.add(element.text().trim());
            android.util.Log.d("test: ", element.text());
        }
        for(int i=0; i<items.size(); i++) {
            items.set(i, items.get(i) + " (" + dates.get(i) + ")");
        }
        android.util.Log.d("test: ", items.size() + " items");
        adapter.notifyDataSetChanged();
    }
}