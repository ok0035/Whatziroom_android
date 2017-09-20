package graduation.whatziroom.search;


import java.util.List;

import graduation.whatziroom.Data.MapData;

public interface OnFinishSearchListener {
	public void onSuccess(List<MapData> itemList);
	public void onFail();
}
