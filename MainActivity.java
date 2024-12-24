package com.example.newsaggregator;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsItem> newsList;
    
    // Replace with your NewsAPI Key and endpoint
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=YOUR_API_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(newsAdapter);

        // Fetch news
        fetchNews();
    }

    private void fetchNews() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL, null, 
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray articles = response.getJSONArray("articles");

                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject article = articles.getJSONObject(i);

                            String title = article.getString("title");
                            String description = article.getString("description");
                            String imageUrl = article.getString("urlToImage");

                            NewsItem newsItem = new NewsItem(title, description, imageUrl);
                            newsList.add(newsItem);
                        }

                        newsAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error parsing news", Toast.LENGTH_SHORT).show();
                        Log.e("MainActivity", "Error: " + e.getMessage());
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error fetching news", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error: " + error.getMessage());
                }
            });

        // Add the request to the Volley queue
        Volley.newRequestQueue(this).add(request);
    }
}
