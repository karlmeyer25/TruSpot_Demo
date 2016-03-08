package com.truspot.android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.truspot.android.R;
import com.truspot.android.picasso.RoundedTransformation;
import com.truspot.android.utils.Util;
import com.truspot.backend.api.model.VenueFull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ApiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // variables
    private Context mContext;
    private List<String> mData;

    // constructor
    public ApiAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mData = data;
    }

    // methods
    public List<String> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApiHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_api, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ApiHolder viewHolder = (ApiHolder) holder;
        String item = mData.get(position);

        viewHolder.tv.setText(item);
    }

    // inner classes
    public class ApiHolder
            extends
                RecyclerView.ViewHolder
            implements
                View.OnClickListener {

        // UI variables
        @Bind(R.id.tv_item_api)
        TextView tv;

        // constructor
        public ApiHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            // TODO
        }
    }
}
