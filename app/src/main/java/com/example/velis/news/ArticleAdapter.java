package com.example.velis.news;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/** Adapter for the list of articles */
public class ArticleAdapter extends ArrayAdapter<Article> {

/**
 * This is our own custom constructor (it doesn't mirror a superclass constructor).
 * The context is used to inflate the layout file, and the list is the data we want
 * to populate into the lists.
 *
 * @param context  The current context. Used to inflate the layout file.
 * @param detail<Article> A List of Detail objects to display in a list
**/
 ArticleAdapter(Activity context, ArrayList<Article> detail ){
    // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
    // the second argument is used when the ArrayAdapter is populating a single TextView.
    // because the adapter is not
    // going to use this second argument, so it can be any value. Here, we used 0.
    super(context, 0, detail);
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        // Get the {@link Article} object located at this position in the list
        Article currentArticle = getItem(position);
        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = convertView.findViewById(R.id.name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        try{
            assert currentArticle != null;
            nameTextView.setText(currentArticle.getWebTitle());
        TextView dateTextView =  convertView.findViewById(R.id.date);
        // Get the date from the current Article object and
        // set this text on the name TextView
        dateTextView.setText(currentArticle.getDate());}
            catch(NullPointerException e){
                Log.v("getWebTitle ", " exception" + e);
        }
        TextView sectionTextView = convertView.findViewById(R.id.section);
        // Get the section name from the current Article object and
        // set this text on the name TextView
        sectionTextView.setText(currentArticle.getSection());
        // Get the author
        TextView authorTextView=convertView.findViewById(R.id.author);
        //getting the resource author
        String my=ArticleAdapter.this.getContext().getResources().getString(R.string.author)+currentArticle.getAuthor();
        authorTextView.setText(my);
        // Return the whole list item layout
        // so that it can be shown in the ListView
         return convertView;
    }


}
