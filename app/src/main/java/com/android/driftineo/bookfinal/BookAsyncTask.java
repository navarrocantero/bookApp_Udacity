package com.android.driftineo.bookfinal;

import android.os.AsyncTask;

import com.android.driftineo.bookfinal.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by driftineo on 6/7/17.
 */

public class BookAsyncTask extends AsyncTask<String, Void, ArrayList<Book>> {

    public AsyncResponse asyncResponse;

    public BookAsyncTask(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected ArrayList<Book> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookString = "";
        String blankString = "";

        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if (inputStream == null) {
                bookString = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((blankString = reader.readLine()) != null) {
                stringBuffer.append(blankString + "\n");
            }
            if (stringBuffer.length() == 0) {
                bookString = null;
            }
            bookString = stringBuffer.toString();
        } catch (java.io.IOException e) {
            bookString = null;
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonTobook(bookString);
    }

    @Override
    protected void onPostExecute(ArrayList<Book> books) {

        asyncResponse.processFinish(books);
    }

    private ArrayList<Book> jsonTobook(String bookString) {
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";

        final String TITLE = "title";
        final String AUTHORS = "authors";

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonAllObjects = new JSONObject(bookString);

            if (!jsonAllObjects.isNull(ITEMS)) {
                JSONArray jsonArray = jsonAllObjects.getJSONArray(ITEMS);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObjectTwo = jsonObject.getJSONObject(VOLUME_INFO);

                    String title = jsonObjectTwo.getString(TITLE);
                    String authors = jsonObjectTwo.getString(AUTHORS);

                    if (!jsonObjectTwo.isNull(AUTHORS)) {
                        JSONArray authorsArray = jsonObjectTwo.getJSONArray(AUTHORS);
                        for (int j = 0; j < authorsArray.length(); j++) {
                            books.add(new Book(title, authors));
                        }
                    }
                }
            } else {
                books = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }
}
