package kr.ac.duksung.departments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;    // DOM 객체
import org.jsoup.nodes.Element;     // tag 노드
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {
    private Document doc;
    ArrayList<String> employees;
    ArrayList<String> employeeIds;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        employees = new ArrayList<String>();
        employeeIds = new ArrayList<String>();
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, employees);
        listView.setAdapter(adapter);
        String urlString = "http://203.252.213.36:8080/FinalProject/advisorProHome.jsp";
        Intent intent = getIntent();
        String department = intent.getStringExtra("department");

        urlString = urlString + "?dept=" + department;
        JsoupAsyncTask employeeTask = new JsoupAsyncTask();
        employeeTask.execute(urlString);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "사번 : " + employeeIds.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class JsoupAsyncTask extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... params) {
            try {
                doc = Jsoup.connect(params[0]).get();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "network error",
                        Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            Elements elements = doc.select("h5");
            for(Element element : elements) {
                employees.add(element.text());
            }
            Elements emplIds = doc.select("h6");
            for(Element emplId : emplIds) {
                employeeIds.add(emplId.text());
            }
            adapter.notifyDataSetChanged();
        }
    }
}
