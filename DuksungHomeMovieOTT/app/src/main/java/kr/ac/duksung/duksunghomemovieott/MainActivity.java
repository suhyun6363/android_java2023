package kr.ac.duksung.duksunghomemovieott;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> movies;
    ArrayList<String> details;
    ArrayAdapter<String> adapter;
    boolean reservation = true;
    String url;
    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("예매 순위");

        movies = new ArrayList<String>();
        details = new ArrayList<String>();
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, movies);
        listView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        makeRequest();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), movies.get(i), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("detailUrl", details.get(i));
                startActivity(intent);
            }
        });
    }

    public void makeRequest() {
        if (reservation) {
            url = "https://movie.daum.net/ranking/reservation/";
        }
        else {
            url = "https://movie.daum.net/ranking/ott/";
        }
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
        Elements elements;
        if (reservation) {
            elements = doc.select("li div.item_poster div.thumb_cont strong.tit_item a");
            // XPath '//li/div[@class='item_poster']/div[@class='thumb_cont']/strong[@class='tit_item']/a[@class='link_txt']
        }
        else {
            elements = doc.select("div ol li div.item_poster div.thumb_cont strong.tit_item a");
            // XPath
            // '//div[@class='detail_rankinginfo']/ol[@class='list_movieranking aniposter_ott']/li/div[@class='item_poster']/div[@class='thumb_cont']/strong[@class='tit_item']/a[@class='link_txt']'
        }
        for(Element element : elements) {
            movies.add(element.text().trim());
            details.add(element.attr("href"));
            android.util.Log.d("test: ", element.text());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.movieott_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.movieOTTItem) {
            if (reservation) {
                reservation = false;            // reservation = !reservation
                setTitle("OTT 순위");
                movies.clear();
                details.clear();
                makeRequest();
                return true;
            } else {
                reservation = true;
                setTitle("예매 순위");
                movies.clear();
                details.clear();
                makeRequest();
                return true;
            }
        }
        return false;
    }
}