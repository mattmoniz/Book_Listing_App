package com.example.android.booklistingapp;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/*
 * {@link BookFInderAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
 * based on a data source, which is a list of {@link Book} objects.
 * */
public class BookFinderAdapter extends ArrayAdapter<Book> {
    private static final String LOG_TAG = BookFinderAdapter.class.getSimpleName();

    private static final String LOCATION_SEPARATOR = " of ";
    String primaryLocation;
    String locationOffset;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param books A List of book objects to display in a list
     */
    public BookFinderAdapter(Activity context, ArrayList<Book> books) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, books);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Book} object located at this position in the list
        Book currentBook = getItem(position);

        /*// Find the TextView in the list_item.xml layout with the ID version_name
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.Bk_Author);
        // Get the name from the current Books Author and set this text on the name TextView
        authorTextView.setText(currentBook.getAuthor());*/

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.Bk_Title);
        // Get the name from the current Book object and set this text on the name TextView
        titleTextView.setText(currentBook.getTitle());

        // Return the whole list item layout (containing 2 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }

}

