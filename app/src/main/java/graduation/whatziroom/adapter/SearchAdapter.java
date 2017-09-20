package graduation.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import graduation.whatziroom.Data.SearchData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.room.SearchPlaceActivity;

/**
 * Created by mapl0 on 2017-09-20.
 */

public class SearchAdapter extends ArrayAdapter {

    LayoutInflater inf = null;
    Context mContext = null;
    ArrayList<SearchData> SearchList = null;

    public SearchAdapter(@NonNull Context context, ArrayList<SearchData> list) {
        super(context, R.layout.search_list_item, list);
        mContext = context;
        SearchList = list;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.search_list_item, null);
        }

        SearchData data = SearchList.get(position);

        TextView tvTitle = row.findViewById(R.id.tvSearchTitle);
        TextView tvAddress = row.findViewById(R.id.tvSearchAddress);
        TextView tvPhone = row.findViewById(R.id.tvSearchPhone);
        ImageView ivSearch = row.findViewById(R.id.ivSearch);

        tvTitle.setText(data.getTitle());
        tvAddress.setText(data.getAddress());
        tvPhone.setText(data.getPhone());
        Log.d("imageURL", data.getImageUrl() + "..................");
        System.out.println("IMAGEURL   " + data.getImageUrl());


        if(!data.getImageUrl().equals(""))
            Glide.with(SearchPlaceActivity.searchContext).load(data.getImageUrl()).into(ivSearch);

        return row;
    }
}
