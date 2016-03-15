package com.truspot.android.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.truspot.android.R;
import com.truspot.android.activities.PlayVideoActivity;
import com.truspot.android.enums.SocialMediaEnum;
import com.truspot.android.interfaces.IGotPicasso;
import com.truspot.android.picasso.RoundedTransformation;
import com.truspot.android.utils.GoogleUtil;
import com.truspot.android.utils.LogUtil;
import com.truspot.android.utils.Util;
import com.truspot.backend.api.model.SocialMediaItem;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SocialItemFragment extends Fragment {

    // constants
    public static final String BASIC_TAG = SocialItemFragment.class.getName();

    private static final String BUNDLE_SOCIAL_MEDIA_ITEM = "social_media_item";

    // variables
    private SocialMediaItem mItem;
    private Picasso mPicasso;
    private SocialMediaEnum mSocialMediaEnum;
    private String mImageUrl;

    // UI
    @Bind(R.id.tv_fragment_social_item)
    TextView tv;
    @Bind(R.id.iv_fragment_social_item)
    ImageView iv;
    @Bind(R.id.iv_fragment_social_item_play)
    ImageView ivPlay;
    @Bind(R.id.iv_fragment_social_item_avatar)
    ImageView ivAvatar;
    @Bind(R.id.tv_fragment_social_item_user_name)
    TextView tvUsername;

    // get instance methods
    public static SocialItemFragment getInstance(SocialMediaItem socialMediaItem) throws IOException {
        SocialItemFragment fragment = new SocialItemFragment();

        Bundle args = new Bundle();
        args.putString(BUNDLE_SOCIAL_MEDIA_ITEM, GoogleUtil.objectToJsonString(socialMediaItem));

        fragment.setArguments(args);

        return fragment;
    }

    // methods
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_item, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initArgs();
        initVariables();
        initListeners();
        loadUsernameDetails();
        loadMediaItem();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mPicasso = ((IGotPicasso) activity).getPicasso();
        } catch(ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " must implement " + IGotPicasso.class.getName());
        }
    }

    private void initArgs() {
        try {
            mItem = GoogleUtil.jsonToObject(SocialMediaItem.class,
                    getArguments().getString(BUNDLE_SOCIAL_MEDIA_ITEM));

            LogUtil.log(BASIC_TAG, mItem.toPrettyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVariables() {
        if (Util.isStringNotNull(mItem.getVideoUrl())) {
            mSocialMediaEnum = SocialMediaEnum.VIDEO;
        } else if (Util.isStringNotNull(mItem.getPhotoUrl())) {
            mSocialMediaEnum = SocialMediaEnum.PHOTO;
        } else {
            mSocialMediaEnum = SocialMediaEnum.TEXT;
        }
    }

    private void initListeners() {
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PlayVideoActivity.getIntent(getActivity(),
                        Util.getYoutubeIdFromUrl(mItem.getVideoUrl())));
            }
        });
    }

    private void loadUsernameDetails() {
        tvUsername.setText(Util.isStringNotNull(mItem.getUsername()) ? mItem.getUsername() : "");

        mPicasso.load(mItem.getAvatarUrl())
                .placeholder(R.drawable.default_avatar)
                .noFade()
                .resize(
                        Util.convertDpiToPixels(getActivity(), 50),
                        Util.convertDpiToPixels(getActivity(), 50))
                .centerCrop()
                .transform(new RoundedTransformation(
                        Util.convertDpiToPixels(getActivity(), 50) / 2,
                        0))
                .config(Bitmap.Config.RGB_565)
                .into(ivAvatar);
    }

    private void loadMediaItem() {
        ivPlay.setVisibility(mSocialMediaEnum == SocialMediaEnum.VIDEO ? View.VISIBLE : View.GONE);
        tv.setVisibility(Util.isStringNotNull(mItem.getText()) ? View.VISIBLE : View.GONE);
        mImageUrl = mSocialMediaEnum == SocialMediaEnum.VIDEO ? String.format("http://img.youtube.com/vi/%s/0.jpg",
                Util.getYoutubeIdFromUrl(mItem.getVideoUrl())) : mItem.getPhotoUrl();

        if (mSocialMediaEnum == SocialMediaEnum.VIDEO ||
                mSocialMediaEnum == SocialMediaEnum.PHOTO) {

            mImageUrl = mSocialMediaEnum == SocialMediaEnum.VIDEO ?
                    String.format("http://img.youtube.com/vi/%s/0.jpg",
                            Util.getYoutubeIdFromUrl(mItem.getVideoUrl())) :
                    mItem.getPhotoUrl();

            mPicasso
                    .load(mImageUrl)
                    .fit()
                    .centerCrop()
                    .config(Bitmap.Config.RGB_565)
                    .into(iv);
        }

        tv.setText(Util.isStringNotNull(mItem.getText()) ? mItem.getText() : "");
    }
}
