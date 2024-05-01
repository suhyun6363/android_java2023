package kr.ac.duksung.advisor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;    // DOM 객체
import org.jsoup.nodes.Element;     // tag 노드
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    private Document doc;
    ArrayList<String> students;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        students = new ArrayList<String>();
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, students);
        listView.setAdapter(adapter);
        // local server
        //       String urlString = "http://10.0.2.2:8080/FinalProject/advisorPro.jsp";
        String urlString = "http://203.252.219.241:8080/FinalProject/advisorPro.jsp";
        Intent intent = getIntent();
        String professor = intent.getStringExtra("professor");

        urlString = urlString + "?advisor=" + professor;
        JsoupAsyncTask advisorTask = new JsoupAsyncTask();
        advisorTask.execute(urlString);         // 스레드 분기됨
    }

    private class JsoupAsyncTask extends AsyncTask<String, Void, Document> {    // 독립된 스레드 클래스

        @Override
        protected Document doInBackground(String... params) {   // 가변매개변수, 현재는 urlString 매개변수 1개 받음
            try {
                doc = Jsoup.connect(params[0]).get();   // get방식으로 접속이 이루어짐
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
                students.add(element.text());
            }
            adapter.notifyDataSetChanged();
        }
    }
}
