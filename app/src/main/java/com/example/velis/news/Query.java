package com.example.velis.news;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

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
import java.util.List;

import static com.example.velis.news.MainActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving articles data from guardian.com.
 * Return a list of {@link Article} objects that has been built up from
 * parsing the given JSON response.
 */
public class Query {
    /*
      Create a private constructor because no one should ever create a {@link Query} object.
      This class is only meant to hold static variables and methods, which can be accessed
      directly from the class name Query (and an object instance of Query is not needed).
     */

    private Query() {
    }

    @Nullable
    private static List<Article> extractFeatureFromJson(String articleJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles
        List<Article> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject root = new JSONObject(articleJSON);

            JSONObject nextObject=root.getJSONObject("response");

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features.
            JSONArray articlesArray = nextObject.getJSONArray("results");

            // For each articles in the articleArray, create an {@link Article} object
            for (int i = 0; i < articlesArray.length(); i++) {

                // Get a single article at position i within the list of articles
                JSONObject currentArticle =articlesArray.getJSONObject(i);


                // For a given article, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that article.
               String name= currentArticle.getString("webTitle");

                //extract publication date
                String date= currentArticle.getString("webPublicationDate");

                // Extract the value for the key called "url"
                String url = currentArticle.getString("webUrl");

               //Extract the value of the section
                String section=currentArticle.getString("sectionName");

                // Extract the JSONArray associated with the key called "tags",
                // which represents a list of features.
                JSONArray tagsArray = currentArticle.getJSONArray("tags");
                String author="N/A";
                if (tagsArray.length()>0){
                    // Get the first position tag element within the list of tags
                    JSONObject currentTag =tagsArray.getJSONObject(0);
                    author=currentTag.getString("webTitle");
                }

                // Create a new {@link Article} object with the name, date, url,
                // from the JSON response.
                Article article = new Article(name, date, section, url, author);
                // Add the new {@link article} to the list of articles.
               articles.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of articles
        return articles;
    }


    /**
     * Query the USGS dataset and return a list of {@link Article} objects.
     */
    public static List<Article> fetchArticle(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract and return relevant fields from the JSON response and create a list of {@link Article}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
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
}
