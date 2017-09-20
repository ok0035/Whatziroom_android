package graduation.whatziroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import graduation.whatziroom.Data.ScheduleData;
import graduation.whatziroom.R;

/**
 * Created by mapl0 on 2017-09-16.
 */

public class ScheduleAdapter extends ArrayAdapter {

    LayoutInflater inf = null;
    Context mContext = null;
    ArrayList<ScheduleData> ScheduleList = null;

    public ScheduleAdapter(Context context, ArrayList<ScheduleData> list) {
        super(context, R.layout.schedule_list_item, list);
        mContext = context;
        ScheduleList = list;
        inf = LayoutInflater.from(mContext);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.schedule_list_item, null);
        }

        ScheduleData data = ScheduleList.get(position);

        TextView name = row.findViewById(R.id.tvScheduleName);
        TextView place = row.findViewById(R.id.tvSchedulePlace);
        TextView date = row.findViewById(R.id.tvScheduleTime);
        TextView dDay = row.findViewById(R.id.tvScheduleDday);

        name.setText(data.getScheduleName());
        place.setText(data.getSchedulePlace());
        date.setText(data.getScheduleDate());
        dDay.setText(data.getScheduleDday());


        return row;
    }

}
