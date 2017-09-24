package graduation.whatziroom.search;


import java.util.List;

import graduation.whatziroom.Data.SearchData;

public interface OnFinishSearchListener {
	public void onSuccess(List<SearchData> itemList);
	public void onFail();
}
