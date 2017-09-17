package com.graduation.project.whatziroom.Data;

import android.support.v4.app.FragmentActivity;

import com.graduation.project.whatziroom.R;
import com.graduation.project.whatziroom.activity.SearchDemoActivity;


public class DemoListItem {
	public final int titleId;
	public final int descriptionId;
	public final Class<? extends FragmentActivity> activity;

	public DemoListItem(int titleId, int descriptionId,
                        Class<? extends FragmentActivity> activity) {
		this.titleId = titleId;
		this.descriptionId = descriptionId;
		this.activity = activity;
	}

	public static final DemoListItem[] DEMO_LIST_ITEMS = {
			new DemoListItem(R.string.search_demo_title,
					R.string.search_demo_desc, SearchDemoActivity.class)
	};
}
