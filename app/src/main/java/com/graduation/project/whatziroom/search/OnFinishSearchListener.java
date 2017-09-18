package com.graduation.project.whatziroom.search;

import com.graduation.project.whatziroom.Data.MapData;

import java.util.List;

public interface OnFinishSearchListener {
	public void onSuccess(List<MapData> itemList);
	public void onFail();
}
