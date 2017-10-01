package graduation.whatziroom.search;


import java.util.List;

import graduation.whatziroom.Data.MapData;

public interface OnFinishSearchListener {
	void onSuccess(List<MapData> itemList);
	void onFail();
}
