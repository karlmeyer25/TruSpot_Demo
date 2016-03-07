package com.truspot.android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.truspot.android.R;
import com.truspot.android.interfaces.VenueClickListener;
import com.truspot.android.picasso.RoundedTransformation;
import com.truspot.android.utils.Util;
import com.truspot.backend.api.model.VenueFull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VenuesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    // variables
    private Context mContext;
    private Picasso mPicasso;
    private VenueClickListener mCallback;
    private List<VenueFull> mData;

    // constructor
    public VenuesAdapter(Context context, Picasso picasso, VenueClickListener callback) {
        this.mContext = context;
        this.mPicasso = picasso;
        this.mCallback = callback;

        mData = new ArrayList<>();
    }

    // methods
    public void addData(VenueFull[] data, boolean notify) {
        addData(Arrays.asList(data), notify);
    }

    public void addData(List<VenueFull> data, boolean notify) {
        int startCount = mData.size();

        mData.addAll(data);

        if (notify) {
            try {
                notifyItemRangeInserted(startCount, data.size());
            } catch (IndexOutOfBoundsException e) {
                notifyDataSetChanged();
                e.printStackTrace();
            }
        }
    }

    public void clearData(boolean notify) {
        mData.clear();

        if (notify) {
            notifyDataSetChanged();
        }
    }

    public List<VenueFull> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public VenueFull getItem(int position) {
        return mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VenuesHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_venue, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VenuesHolder viewHolder = (VenuesHolder) holder;
        VenueFull item = mData.get(position);

        mPicasso
                .load(R.drawable.ic_default_venue)
                .noFade()
                .resize(
                        Util.convertDpiToPixels(mContext, 80),
                        Util.convertDpiToPixels(mContext, 80))
                .centerCrop()
                .transform(new RoundedTransformation(
                        Util.convertDpiToPixels(mContext, 80) / 2,
                        0))
                .config(Bitmap.Config.RGB_565)
                .into(viewHolder.iv);

        viewHolder.tvName.setText(item.getVenue().getName());
        viewHolder.tvDescription.setText(item.getVenue().getDescription() != null
                ? item.getVenue().getDescription() : "");
    }

    // inner classes
    public class VenuesHolder
            extends
                RecyclerView.ViewHolder
            implements
                View.OnClickListener {

        // UI variables
        @Bind(R.id.iv_item_venue)
        ImageView iv;
        @Bind(R.id.tv_item_venue_name)
        TextView tvName;
        @Bind(R.id.tv_item_venue_description)
        TextView tvDescription;

        // constructor
        public VenuesHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mCallback != null) {
                mCallback.onVenueClick(mData.get(getAdapterPosition()));
            }
        }
    }
}
