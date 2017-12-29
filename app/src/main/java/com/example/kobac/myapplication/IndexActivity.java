package com.example.kobac.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kobac.myapplication.itemDivider.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;


public class IndexActivity extends AppCompatActivity {


    public static final String LOG_TAG = IndexActivity.class.getSimpleName();

    public static final String index_URL = "http://intellexweb.dev.intellex.rs/api/v1/catalogue";

    private IndexAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private ArrayList<IndexModel> indexArray;

    Context context;
    private boolean doubleBackToExitPressedOnce = false;

    private SearchView searchView;


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index__);

        if (!isNetworkAvailable()) {

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.login__dialog);
            dialog.setTitle("Login results");

            TextView dialogText = dialog.findViewById(R.id.succesfullyLoginText);
            dialogText.setText("There is no internet conection");
            Button dialogButton = dialog.findViewById(R.id.dialogButtonCancel);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        searchView = (SearchView) findViewById(R.id.search);

        indexArray = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.indexRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        adapter = new IndexAdapter(this, indexArray);
        SlideInLeftAnimationAdapter alphaAdapter = new SlideInLeftAnimationAdapter(adapter);
        recyclerView.setAdapter(alphaAdapter);
        new IndexAsyncTask().execute();

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private class IndexAsyncTask extends AsyncTask<URL, Void, ArrayList<IndexModel>> {

        @Override
        protected ArrayList<IndexModel> doInBackground(URL... urls) {

            URL url = createUrl(index_URL);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return extractFeatureFromJson(jsonResponse);
        }


        @Override
        protected void onPostExecute(ArrayList<IndexModel> index) {
            if (index == null) {
                return;
            }

            adapter.addAll(index);
        }


        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private ArrayList<IndexModel> extractFeatureFromJson(String indexJSON) {
            if (TextUtils.isEmpty(indexJSON)) {
                return null;
            }
            ArrayList<IndexModel> index_information = new ArrayList<>();
            try {
                JSONObject baseJsonResponse = new JSONObject(indexJSON);
                JSONArray indexArray = baseJsonResponse.getJSONArray("data");
                for (int i = 0; i < indexArray.length(); i++) {
                    JSONObject index = indexArray.getJSONObject(i);
                    try {
                        index = indexArray.getJSONObject(i);
                        String indexImage = index.getString("project_image");
                        String indexTitle = index.getString("title");
                        String indexDescription = index.getString("intro");
                        String indexID = index.getString("id");

                        IndexModel indexModel = new IndexModel(indexImage, indexTitle, indexDescription, indexID);
                        index_information.add(indexModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
            return index_information;
        }
    }
}


