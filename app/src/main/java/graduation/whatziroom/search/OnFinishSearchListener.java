package graduation.whatziroom.search;


import java.util.List;

import graduation.whatziroom.Data.SearchData;

public interface OnFinishSearchListener {
	void onSuccess(List<SearchData> itemList);
	void onFail();
}
