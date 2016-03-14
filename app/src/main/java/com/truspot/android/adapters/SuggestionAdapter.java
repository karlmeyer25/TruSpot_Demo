package com.truspot.android.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends ArrayAdapter<String> implements Filterable {

    // variables
    private List<String> items;
    private List<String> filteredItems;

    // constructor
    public SuggestionAdapter(Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.items = objects;
    }

    // methods
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<String> results = new ArrayList<>();

                if (filteredItems == null)
                    filteredItems = items;

                if (constraint != null) {
                    if (filteredItems != null && filteredItems.size() > 0) {
                        for (final String venue : filteredItems) {
                            if (venue.toLowerCase().contains(constraint.toString().toLowerCase()))
                                results.add(venue);
                        }
                    }

                    oReturn.values = results;
                    oReturn.count = results.size();
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items = (ArrayList<String>) results.values;

                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    public int getCount() {
        return items.size();
    }
}
