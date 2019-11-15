package com.example.android.booklistingapp;

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

/**
 * Helper methods related to requesting and receiving book data from Google Books API.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the Google Books dataset and return an {@link Book} object to represent a single book.
     */

    public static List<Book> FetchBookData(String requestUrl){

        //create url object
        URL url =createUrl(requestUrl);

        //perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try{
            jsonResponse=makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream",e);
        }
        // Extract relevant fields from the JSON response and create an {@link books} object
        List<Book> books = extractFeatureFromJson(jsonResponse);

        return books;
    }


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */

    //Separate method to generate the Arraylist of Book objects.
    public static List<Book> extractFeatureFromJson(String bookJSON) {

        //If the JSON string is empty or null, then return early
        if(TextUtils.isEmpty(bookJSON)){
            return null;
        }

        //Create arrayList to add books too
        List<Book> books=new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            //Create JSON Object from JSON Response string.
            JSONObject baseJsonResponse=new JSONObject(bookJSON);

            //Extract the JSONArray with key names "features"
            //features contains the list of books
            JSONArray bookArray=baseJsonResponse.getJSONArray("items");


            //loop iterates through each book listed in the features JSON array,
            //we take out whatever data we're looking for, create a new book object with our data,
            //and then add it to the book arraylist on our screen.
            for (int i=0;i<bookArray.length();i++){
                JSONObject bookObject = bookArray.getJSONObject(i);
                JSONObject items = bookObject.getJSONObject("volumeInfo");

                //All values can be displayed as strings because they are only being displayed to the screen.
                String title=items.getString("title");
                // Extract the value for the key called "url"
                //String url = items.getString("url");

                //created new book
                Book thisBook=new Book(title);

                //adds new book to the list of books
                books.add(thisBook);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        Log.v(url.toString(),"Url");
        /** Sample JSON response for a google books query */
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
            Log.e(LOG_TAG, "Problem retrieving the Google Books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */

    private static String readFromStream(InputStream inputStream)throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream !=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
             BufferedReader reader= new BufferedReader(inputStreamReader);
             String line = reader.readLine();
             while (line !=null){
                 output.append(line);
                 line=reader.readLine();
             }
        }
        return output.toString();
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }



}