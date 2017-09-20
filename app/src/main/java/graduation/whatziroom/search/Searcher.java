package graduation.whatziroom.search;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import graduation.whatziroom.Data.SearchData;

public class Searcher {
    // http://dna.daum.net/apis/local
	public static final String DAUM_MAPS_LOCAL_KEYWORD_SEARCH_API_FORMAT = "https://apis.daum.net/local/v1/search/keyword.json?query=%s&location=%f,%f&apikey=%s";
	public static final String DAUM_MAPS_LOCAL_CATEGORY_SEARCH_API_FORMAT = "https://apis.daum.net/local/v1/search/category.json?code=%s&location=%f,%f&apikey=%s";
	/** category codes
	MT1 대형마트
	CS2 편의점
	PS3 어린이집, 유치원
	SC4 학교
	AC5 학원
	PK6 주차장
	OL7 주유소, 충전소
	SW8 지하철역
	BK9 은행
	CT1 문화시설
	AG2 중개업소
	PO3 공공기관
	AT4 관광명소
	AD5 숙박
	FD6 음식점
	CE7 카페
	HP8 병원
	PM9 약국
	 */
	
	private static final String HEADER_NAME_X_APPID = "x-appid";
	private static final String HEADER_NAME_X_PLATFORM = "x-platform";
	private static final String HEADER_VALUE_X_PLATFORM_ANDROID = "android";
	
	OnFinishSearchListener onFinishSearchListener;
	SearchTask searchTask;
	String appId;
	
	private class SearchTask extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... urls) {
			String url = urls[0];
			Map<String, String> header = new HashMap<String, String>();
			header.put(HEADER_NAME_X_APPID, appId);
			header.put(HEADER_NAME_X_PLATFORM, HEADER_VALUE_X_PLATFORM_ANDROID);
			String json = fetchData(url, header);
			List<SearchData> itemList = parse(json);
			if (onFinishSearchListener != null) {
				if (itemList == null) {
					onFinishSearchListener.onFail();
				} else {
					onFinishSearchListener.onSuccess(itemList);
				}
			}
			return null;
		}
	}

	public void searchKeyword(Context applicationContext, String query, double latitude, double longitude, String apikey, OnFinishSearchListener onFinishSearchListener) {
    	this.onFinishSearchListener = onFinishSearchListener;
    	
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
		
		if (applicationContext != null) {
			appId = applicationContext.getPackageName();
		}
		String url = buildKeywordSearchApiUrlString(query, latitude, longitude, apikey);
		Log.d("URL", url);
		searchTask = new SearchTask();
		searchTask.execute(url);
    }
	
	public void searchCategory(Context applicationContext, String categoryCode, double latitude, double longitude, String apikey, OnFinishSearchListener onFinishSearchListener) {
		this.onFinishSearchListener = onFinishSearchListener;
		
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
		
		if (applicationContext != null) {
			appId = applicationContext.getPackageName();
		}
		String url = buildCategorySearchApiUrlString(categoryCode, latitude, longitude, apikey);
		searchTask = new SearchTask();
		searchTask.execute(url);
	}
    
	private String buildKeywordSearchApiUrlString(String query, double latitude, double longitude, String apikey) {
    	String encodedQuery = "";
		try {
			encodedQuery = URLEncoder.encode(query, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return String.format(DAUM_MAPS_LOCAL_KEYWORD_SEARCH_API_FORMAT, encodedQuery, latitude, longitude, apikey);
    }
	
	private String buildCategorySearchApiUrlString(String categoryCode, double latitude, double longitude, String apikey) {
		return String.format(DAUM_MAPS_LOCAL_CATEGORY_SEARCH_API_FORMAT, categoryCode, latitude, longitude, apikey);
	}
	
	private String fetchData(String urlString, Map<String, String> header) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(4000 /* milliseconds */);
			conn.setConnectTimeout(7000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			if (header != null) {
				for (String key : header.keySet()) {
					conn.addRequestProperty(key, header.get(key));
				}
			}
			conn.connect();
			InputStream is = conn.getInputStream();
			@SuppressWarnings("resource")
            Scanner s = new Scanner(is);
			s.useDelimiter("\\A");
			String data = s.hasNext() ? s.next() : "";
			is.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
	private List<SearchData> parse(String jsonString) {
		List<SearchData> itemList = new ArrayList<SearchData>();
		try {
			JSONObject reader = new JSONObject(jsonString);
			JSONObject channel = reader.getJSONObject("channel");
			JSONArray objects = channel.getJSONArray("item");
			for (int i = 0; i < objects.length(); i++) {
				JSONObject object = objects.getJSONObject(i);
				SearchData item = new SearchData();
				item.setTitle(object.getString("title"));
				item.setImageUrl(object.getString("imageUrl"));
				item.setAddress(object.getString("address"));
				item.setNewAddress(object.getString("newAddress"));
				item.setZipcode(object.getString("zipcode"));
				item.setPhone(object.getString("phone"));
				item.setCategory(object.getString("category"));
				item.setLatitude(object.getDouble("latitude"));
				item.setLongitude(object.getDouble("longitude"));
				item.setDistance(object.getString("distance"));
				item.setDirection(object.getString("direction"));
				item.setId(object.getString("id"));
				item.setPlaceUrl(object.getString("placeUrl"));
				item.setAddressBCode(object.getString("addressBCode"));
				itemList.add(item);

				Log.d("title", object.getString("title"));
				Log.d("address", object.getString("address"));
				Log.d("zipcode", object.getString("zipcode"));
				Log.d("phone", object.getString("phone"));
				Log.d("latitude", object.getString("latitude"));
				Log.d("longitude", object.getString("longitude"));
				Log.d("distance", object.getString("distance"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return itemList;
	}
	
	public void cancel() {
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
	}
}
