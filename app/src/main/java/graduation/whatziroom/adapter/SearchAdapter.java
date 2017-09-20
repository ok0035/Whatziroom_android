package graduation.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import graduation.whatziroom.Data.SearchData;
import graduation.whatziroom.R;

/**
 * Created by mapl0 on 2017-09-20.
 */

public class SearchAdapter extends ArrayAdapter {

    LayoutInflater inf = null;
    Context mContext = null;
    ArrayList<SearchData> ScheduleList = null;

    public SearchAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<SearchData> list) {
        super(context, R.layout.search_list_item, list);
        mContext = context;
        ScheduleList = list;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.search_list_item, null);
        }

//        ScheduleData data = ScheduleList.get(position);
//
//        TextView name = row.findViewById(R.id.tvScheduleName);
//        TextView place = row.findViewById(R.id.tvSchedulePlace);
//        TextView date = row.findViewById(R.id.tvScheduleTime);
//        TextView dDay = row.findViewById(R.id.tvScheduleDday);
//
//        name.setText(data.getScheduleName());
//        place.setText(data.getSchedulePlace());
//        date.setText(data.getScheduleDate());
//        dDay.setText(data.getScheduleDday());


        return row;
    }
}
