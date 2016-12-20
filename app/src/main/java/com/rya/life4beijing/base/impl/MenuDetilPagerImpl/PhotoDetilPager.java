package com.rya.life4beijing.base.impl.MenuDetilPagerImpl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rya.life4beijing.R;
import com.rya.life4beijing.Utils.ConstantsValue;
import com.rya.life4beijing.Utils.HttpUtil;
import com.rya.life4beijing.Utils.StreamUtil;
import com.rya.life4beijing.base.BaseMenuDetilPager;
import com.rya.life4beijing.bean.PhotosBean;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rya32 on 广东石油化工学院.
 * Version 1.0
 */

public class PhotoDetilPager extends BaseMenuDetilPager {
    @BindView(R.id.lv_photo)
    ListView lvPhoto;
    @BindView(R.id.gv_photo)
    GridView gvPhoto;
    private PhotosBean photosBean;
    private boolean isListView = true;

    public PhotoDetilPager(Activity activity, View view) {
        super(activity);

        final ImageButton imgPhotos = (ImageButton) view.findViewById(R.id.imgPhotos);
        imgPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListView) {
                    imgPhotos.setImageDrawable(getmActivity().getResources().getDrawable(R.drawable.icon_pic_list_type));
                    gvPhoto.setVisibility(View.VISIBLE);
                    lvPhoto.setVisibility(View.GONE);
                    isListView = false;
                } else {
                    imgPhotos.setImageDrawable(getmActivity().getResources().getDrawable(R.drawable.icon_pic_grid_type));
                    gvPhoto.setVisibility(View.GONE);
                    lvPhoto.setVisibility(View.VISIBLE);
                    isListView = true;
                }
            }
        });
    }

    @Override
    public View initView() {
        View view = View.inflate(getmActivity(), R.layout.photo_menu_pager, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        getDataFromServer();
    }

    private void getDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream photosData = HttpUtil.getData(ConstantsValue.PHOTOS_URI);
                    String str_photosData = StreamUtil.streamToString(photosData);
                    StreamUtil.writeFileToCache(getmActivity(), str_photosData, "photos_data");

                    getDataFromJson(str_photosData);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getDataFromJson(String photosDataStr) {
        Gson gson = new Gson();
        photosBean = gson.fromJson(photosDataStr, PhotosBean.class);

        getmActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lvPhoto.setAdapter(new PhotosAdapter());
                gvPhoto.setAdapter(new PhotosAdapter());
            }
        });
    }

    private class PhotosAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return photosBean.getData().getNews().size();
        }

        @Override
        public PhotosBean.DataBean.NewsBean getItem(int position) {
            return photosBean.getData().getNews().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = View.inflate(getmActivity(), R.layout.photo_menu_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Picasso.with(getmActivity())
                    .load(photosBean.getData().getNews().get(position).getListimage())
                    .placeholder(R.drawable.topnews_item_default)
                    .error(R.drawable.topnews_item_default)
                    .into(viewHolder.ivPhotos);

            viewHolder.tvPhotos.setText(photosBean.getData().getNews().get(position).getTitle());

            return convertView;
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_photos)
        ImageView ivPhotos;
        @BindView(R.id.tv_photos)
        TextView tvPhotos;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
