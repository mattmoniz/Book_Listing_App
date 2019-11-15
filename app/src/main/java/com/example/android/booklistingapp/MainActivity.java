package com.example.android.booklistingapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    /** Adapter for the list of books */
    private BookFinderAdapter mAdapter;

    /**
     * URL for book data from the GoogleBooks
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.btn_submitAuthor);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String bookName;
                EditText nameInput = ((EditText) findViewById(R.id.search_field));
                bookName =nameInput.getText().toString();

                //String text = nameInput.getText();
                //String bookName1 = bookName.substring(0,bookName.indexOf("\n"));


                String GOOGLE_BOOKS_REQUEST_URL= "https://www.googleapis.com/books/v1/volumes?q=";
                String SearchURL=GOOGLE_BOOKS_REQUEST_URL+bookName;

                //Start the AsyncTask to fetch the book data
                BookFindAsyncTask task = new BookFindAsyncTask();
                task.execute(SearchURL);

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });


        // Find a reference to the {@link ListView} in the layout
        ListView BookListView = (ListView)findViewById(R.id.list);

        //create a new adapter that takes an empty list of books as input
        mAdapter = new BookFinderAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        BookListView.setAdapter(mAdapter);

        //Allowed user to click on list item t take them to page URL
        /*BookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the Current Book that was clicked on
                Book currentBook = mAdapter.getItem(position);


                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });*/
    }

    private class BookFindAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {

            //Dont perform the request if there are no URLs.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Book> result = QueryUtils.FetchBookData((urls[0]));
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> data) {
            //clear the adapter of previous data
            mAdapter.clear();

            //if there is a valid list of {@link Books}, then add them to the data set.
            //this will trigger the listview to update.

            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }


}