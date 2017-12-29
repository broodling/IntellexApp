package com.example.kobac.myapplication.details;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kobac.myapplication.R;
import com.example.kobac.myapplication.galery.GalleryAdapter;
import com.example.kobac.myapplication.galery.GalleryModel;

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



/**
 * Created by kobac on 22.8.17..
 */

public class DetailsFragment extends android.support.v4.app.Fragment {

    public static final String BUNDLE_URL = "url";
    public static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private String mURL = null;

    private GalleryAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details__, container, false);
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mURL = (String)getArguments().get(BUNDLE_URL);
        new DetailsAsyncTask().execute();

    }

    private void updateUi(DetailsModel detailsModel) {
        final ArrayList<GalleryModel> galleryList = detailsModel.getProject_gallery();

        adapter = new GalleryAdapter(getActivity(), galleryList, detailsModel) {
            @Override
            protected void onClickView(String imageUrl) {
                showSingleImage(imageUrl);
            }
        };

        recyclerView = (RecyclerView) getView().findViewById(R.id.galleryRecyclerView);
        recyclerView.setHasFixedSize(true);

        final int orientation = getResources().getConfiguration().orientation;
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), orientation == 1 ? 2 : 4);
        recyclerView.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    if (orientation == 1) {
                        return 2;
                    } else {
                        return 4;
                    }
                }
                return 1;
            }
        });

        recyclerView.setAdapter(adapter);



    }

    //metoda kojom prikazujemo jednu sliku u full screen i

    public void showSingleImage(final String imageUrl) {

        final RelativeLayout relativeLayout = (RelativeLayout) getView().findViewById(R.id.relativeLayout);
        relativeLayout.setVisibility(View.VISIBLE);

        ImageView imageView = (ImageView) getView().findViewById(R.id.single_image);
        Glide.with(imageView.getContext()).load(imageUrl).into(imageView);

        Button button = (Button) getView().findViewById(R.id.returnButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.GONE);

            }
        });

    }

    private class DetailsAsyncTask extends AsyncTask<URL, Void, ArrayList<DetailsModel>> {


        @Override
        protected ArrayList<DetailsModel> doInBackground(URL... urls) {


            URL url = createUrl(mURL);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return extractFeatureFromJson(jsonResponse);
        }

        @Override
        protected void onPostExecute(ArrayList<DetailsModel> details) {
            if (details == null) {
                return;
            }
            updateUi(details.get(0));
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

        private ArrayList<DetailsModel> extractFeatureFromJson(String detailsJSON) {
            if (TextUtils.isEmpty(detailsJSON)) {
                return null;
            }
            ArrayList<DetailsModel> details_info = new ArrayList<>();
            try {

                JSONObject baseJsonResponse = new JSONObject(detailsJSON);
                JSONArray detailsArray = baseJsonResponse.getJSONArray("data");

                ArrayList<GalleryModel> imageList = new ArrayList<>();

                JSONObject details = detailsArray.getJSONObject(0);
                String title = details.getString("title");
                String client = details.getString("client");
                String year = details.getString("year");
                String text = details.getString("text");
                text = Html.fromHtml(text).toString();
                String services = details.getString("services");
                String id = details.getString("id");
                String frontImage = details.getString("project_image");

                JSONArray images = details.getJSONArray("project_gallery");
                for (int i = 0; i < images.length(); i++) {
                    JSONObject gallery = images.getJSONObject(i);


                    // Get information about image
                    String imageTitle = gallery.getString("title");
                    String imageUrl = gallery.getString("url");


                    // Create gallery model
                    GalleryModel galleryModel = new GalleryModel(imageTitle, imageUrl);

                    // Add to list
                    imageList.add(galleryModel);
                }

                DetailsModel detailsModel = new DetailsModel(title, client, year, text, services, id, imageList, frontImage);
                details_info.add(detailsModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return details_info;
        }
    }
}
