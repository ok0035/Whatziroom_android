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
import com.bumptech.glide.request.RequestOptions;

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
    ArrayList<SearchData> searchList = null;

    public SearchAdapter(@NonNull Context context, ArrayList<SearchData> list) {
        super(context, R.layout.search_list_item, list);
        mContext = context;
        searchList = list;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.search_list_item, null);
        }

        SearchData data = searchList.get(position);

        TextView tvTitle = row.findViewById(R.id.tvSearchTitle);
        TextView tvAddress = row.findViewById(R.id.tvSearchAddress);
        TextView tvPhone = row.findViewById(R.id.tvSearchPhone);
        ImageView ivSearch = row.findViewById(R.id.ivSearch);

        tvTitle.setText(data.getTitle());
        tvAddress.setText(data.getAddress());
        tvPhone.setText(data.getPhone());


        if(!data.getImageUrl().equals("")) {
            System.out.println("이미지가 없는데 이미지가 나오면 안되지!!!");

            Log.d("Title", data.getTitle());
            Log.d("Address", data.getAddress());
            Log.d("Phone", data.getPhone());
            System.out.println("ImageURL : " + data.getImageUrl());
            Glide.with(SearchPlaceActivity.searchContext).load(data.getImageUrl()).apply(RequestOptions.circleCropTransform()).into(ivSearch);
        } else {

            //이미지뷰에 데이터가 남아있어서 의도하지 않은 이미지가 뿌려지는 것을 막아줌
            Glide.with(SearchPlaceActivity.searchContext).clear(ivSearch);

        }


        return row;
    }
}
