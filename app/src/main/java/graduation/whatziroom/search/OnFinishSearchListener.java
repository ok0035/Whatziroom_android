package graduation.whatziroom.search;


import java.util.List;

import graduation.whatziroom.Data.SearchPlaceData;

public interface OnFinishSearchListener {
	void onSuccess(List<SearchPlaceData> itemList);
	void onFail();
}
