package com.truspot.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.truspot.android.R;
import com.truspot.android.constants.Constants;
import com.truspot.backend.api.model.SocialMediaItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SocialMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // variables
    private Context mContext;
    private List<SocialMediaItem> mData;
    private int mPosition;

    // constructor
    public SocialMediaAdapter(Context context) {
        this.mContext = context;

        mData = new ArrayList<>();
    }

    // methods
    public void addData(SocialMediaItem[] data, boolean notify) {
        addData(Arrays.asList(data), notify);
    }

    public void addData(List<SocialMediaItem> data, boolean notify) {
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

    public List<SocialMediaItem> getData() {
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

    public SocialMediaItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApiHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_api, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ApiHolder viewHolder = (ApiHolder) holder;
        SocialMediaItem item = mData.get(position);

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }
        });

        viewHolder.tv.setText(String.valueOf(item.getId()));
    }


    // inner classes
    public class ApiHolder
            extends
                RecyclerView.ViewHolder
            implements
                View.OnCreateContextMenuListener {

        // UI variables
        @Bind(R.id.tv_item_api)
        TextView tv;

        // constructor
        public ApiHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, Constants.MENU_EDIT, Menu.NONE, mContext.getString(R.string.menu_edit));
            menu.add(Menu.NONE, Constants.MENU_DELETE, Menu.NONE, mContext.getString(R.string.menu_delete));
        }
    }
}
