package com.truspot.android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.truspot.android.R;
import com.truspot.android.constants.Constants;
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

    // constants
    private static final int ITEM_DEFAULT = 0;
    private static final int ITEM_API_VENUE = 1;

    // variables
    private Context mContext;
    private Picasso mPicasso;
    private VenueClickListener mCallback;
    private List<VenueFull> mData;
    private boolean mIsApiList;
    private int mPosition;

    // constructor
    public VenuesAdapter(Context context,
                         Picasso picasso,
                         VenueClickListener callback,
                         boolean isApiList) {
        this.mContext = context;
        this.mPicasso = picasso;
        this.mCallback = callback;
        this.mIsApiList = isApiList;

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

    public void removeItem(int position, boolean notify) {
        mData.remove(position);

        if (notify) {
            try {
                notifyItemRemoved(position);
            } catch (IndexOutOfBoundsException e) {
                notifyDataSetChanged();
                e.printStackTrace();
            }
        }
    }

    public List<VenueFull> getData() {
        return mData;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mIsApiList ? ITEM_API_VENUE : ITEM_DEFAULT;
    }

    public VenueFull getItem(int position) {
        return mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_DEFAULT) {
            return new VenuesHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_venue, parent, false));
        } else {
            return new ApiHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_api, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        VenueFull item = mData.get(position);

        switch (viewType) {
            case ITEM_DEFAULT: {
                VenuesHolder viewHolder = (VenuesHolder) holder;

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


                break;
            }

            case ITEM_API_VENUE: {
                ApiHolder viewHolder = (ApiHolder) holder;

                /*
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        setPosition(position);
                        return false;
                    }
                });
                */

                viewHolder.tv.setText(item.getVenue().getName());
            }
        }
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

    public class ApiHolder
            extends
                RecyclerView.ViewHolder
            implements
                View.OnCreateContextMenuListener,
                View.OnClickListener {

        // UI variables
        @Bind(R.id.tv_item_api)
        TextView tv;

        // constructor
        public ApiHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, Constants.MENU_EDIT, Menu.NONE, mContext.getString(R.string.menu_edit));
            menu.add(Menu.NONE, Constants.MENU_DELETE, Menu.NONE, mContext.getString(R.string.menu_delete));
        }

        @Override
        public void onClick(View v) {
            setPosition(getAdapterPosition());

            if (mContext instanceof AppCompatActivity) {
                ((AppCompatActivity) mContext).openContextMenu(v);
            }
        }
    }
}
