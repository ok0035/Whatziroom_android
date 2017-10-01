package graduation.whatziroom.activity.room;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import graduation.whatziroom.Data.MapData;
import graduation.whatziroom.R;
import graduation.whatziroom.activity.base.BasicMethod;
import graduation.whatziroom.activity.main.MainViewPager;
import graduation.whatziroom.dialog.ScheduleNameDialog;
import graduation.whatziroom.search.OnFinishSearchListener;
import graduation.whatziroom.search.Searcher;
import graduation.whatziroom.util.GPSTracer;

public class SearchPlaceActivity extends FragmentActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, BasicMethod {

    private static final String LOG_TAG = "Sea";

    private MapView mMapView;
    private HashMap<Integer, MapData> mTagItemMap = new HashMap<Integer, MapData>();
    ProgressDialog mProgressDialog;
    private ListView lvSearchList;

    public static Activity searchActivity;
    private MapData searchData, selectedData;
    private android.widget.LinearLayout llSlideMain;
    private ImageView ivSearchResult;
    private android.widget.LinearLayout llSlideSub;
    private com.sothree.slidinguppanel.SlidingUpPanelLayout slidingLayout;
    private EditText edSearchQuery;
    private TextView btnSearch;
    private TextView tvSearchResultTitle;
    private TextView tvSearchResultAddress;
    private TextView tvSearchResultPhone;
    private LinearLayout llBtnSelectPlace;
    private WebView wbSearchResult;
    private ScrollView scWebView;

    private TextView tvBtnSearchCancel;
    private TextView tvBtnSearchSelect;

    private int userPKey, roomPKey;
    private String myScheduleDate;

    public SearchPlaceActivity() {
        super();

        searchActivity = this;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);

        setValues();
        bindView();
        setUpEvents();

    }

    @Override
    public void onBackPressed() {

        if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        else if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN && mMapView.getVisibility() == View.VISIBLE)
            mMapView.setVisibility(View.INVISIBLE);
        else if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN && mMapView.getVisibility() == View.INVISIBLE)
            mMapView.setVisibility(View.VISIBLE);

    }

    @Override
    public void setUpEvents() {

        mMapView.setDaumMapApiKey(getResources().getString(R.string.APIKEY));
        mMapView.setMapViewEventListener(this);
        mMapView.setPOIItemEventListener(this);
//        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        edSearchQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() { // 검색버튼 클릭 이벤트 리스너
            @Override
            public void onClick(View v) {

//                slidingLayout.setPanelHeight(BaseActivity.convertDPtoPX(0));
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

                String query = edSearchQuery.getText().toString();
                if (query == null || query.length() == 0) {
                    showToast("검색어를 입력하세요.");
                    return;
                }
                hideSoftKeyboard(); // 키보드 숨김
//                MapPoint.GeoCoordinate geoCoordinate = mMapView.getMapCenterPoint().getMapPointGeoCoord();
                double latitude = GPSTracer.latitude;
                double longitude = GPSTracer.longitude;
//		        int radius = 10000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
//		        int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
                String apikey = getResources().getString(R.string.APIKEY);

                Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
                searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, apikey, new OnFinishSearchListener() {
                    @Override
                    public void onSuccess(final List<MapData> itemList) {
                        mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                        showResult(itemList); // 검색 결과 보여줌

                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                mMapView.setVisibility(View.INVISIBLE);

                                searchData = new MapData();

                                for (int i = 0; i < itemList.size(); i++) {

                                    MapData data = itemList.get(i);
                                    searchData.addItem(data.getImageUrl(), data.getTitle(), data.getAddress(), data.getNewAddress(), data.getZipcode(), data.getPhone(),
                                            data.getCategory(), data.getLongitude(), data.getLatitude(), data.getDistance(), data.getDirection(), data.getId(), data.getPlaceUrl(), data.getAddressBCode());
                                }

                                lvSearchList.setAdapter(searchData.getAdapter());
//                                lvSearchList.deferNotifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onFail() {
//						showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
                    }
                });
            }
        });


        lvSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MapData data = searchData.getSearchList().get(i);
                Log.d("MarkerPoint", data.getTitle());


//                onPOIItemSelected(mMapView, mMapView.getPOIItems()[i]);

//                mMapView.getPOIItems()[i].setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
//                mMapView.getPOIItems()[i].setCustomSelectedImageResourceId(R.drawable.map_pin_red);

                Glide.with(SearchPlaceActivity.this).load(data.getImageUrl()).apply(RequestOptions.circleCropTransform()).into(ivSearchResult);
                tvSearchResultTitle.setText(data.getTitle());
                tvSearchResultAddress.setText(data.getAddress());
                tvSearchResultPhone.setText(data.getPhone());

                wbSearchResult.setWebViewClient(new WebViewClient());
                WebSettings webSettings = wbSearchResult.getSettings();
                webSettings.setJavaScriptEnabled(true);
                wbSearchResult.loadUrl(data.getPlaceUrl());

                mMapView.setVisibility(View.VISIBLE);
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mMapView.selectPOIItem(mMapView.getPOIItems()[i], true);
                mMapView.setMapCenterPoint(mMapView.getPOIItems()[i].getMapPoint(), true);

                selectedData = data;

            }
        });

        scWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

//                Log.d("getAction", motionEvent.getAction() + "");
//                Log.d("ACTIONUP", motionEvent.ACTION_UP + "");
//                Log.d("ACTIONDOWN", motionEvent.ACTION_DOWN + "");
//                Log.d("ACTIONMOVE", motionEvent.ACTION_MOVE + "");
//                Log.d("ACTIONCANCEL", motionEvent.ACTION_CANCEL + "");


                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                    slidingLayout.setTouchEnabled(false);
                else slidingLayout.setTouchEnabled(true);

                return false;

            }
        });


        tvBtnSearchCancel.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View view) {

                finish();

            }

        });

        tvBtnSearchSelect.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View view) {

                ScheduleNameDialog dialog = new ScheduleNameDialog(SearchPlaceActivity.this, userPKey, roomPKey, myScheduleDate, selectedData);
                dialog.show();

            }

        });

    }

    @Override
    public void setValues() {

        Intent intent = getIntent();

        userPKey = MainViewPager.getUserPKey();
        roomPKey = intent.getIntExtra("roomPKey", 0);
        Log.d("SearchRoomPKey", roomPKey + "");
        myScheduleDate = intent.getStringExtra("date");
        Log.d("mySchedule", myScheduleDate);

    }


    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.search_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            if (poiItem == null) return null;
            MapData item = mTagItemMap.get(poiItem.getTag());
            if (item == null) return null;
            ImageView imageViewBadge = mCalloutBalloon.findViewById(R.id.badge);
            TextView textViewTitle = mCalloutBalloon.findViewById(R.id.title);
            textViewTitle.setText(item.getTitle());
            TextView textViewDesc = mCalloutBalloon.findViewById(R.id.desc);
            textViewDesc.setText(item.getAddress());
            imageViewBadge.setImageDrawable(createDrawableFromUrl(item.getImageUrl()));
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }

    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edSearchQuery.getWindowToken(), 0);
    }

    public void onMapViewInitialized(MapView mapView) {
        Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");

        GPSTracer gps = new GPSTracer();
        gps.getLocation();


        final Timer timer = new Timer();
        mProgressDialog = ProgressDialog.show(SearchPlaceActivity.this, "",
                "잠시만 기다려 주세요.", true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing() && GPSTracer.getIsInit()) {

                    mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(GPSTracer.latitude, GPSTracer.longitude), 2, true);

                    mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

                    Searcher searcher = new Searcher();
                    String query = edSearchQuery.getText().toString();

                    MapPoint.GeoCoordinate geoCoordinate = mMapView.getMapCenterPoint().getMapPointGeoCoord();
                    double latitude = GPSTracer.latitude;
                    double longitude = GPSTracer.longitude;


//                    int radius = 0; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
//                    int page = 3; // 페이지 번호 (1 ~ 3). 한페이지에 15개
                    String apikey = getResources().getString(R.string.APIKEY);

                    searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, apikey, new OnFinishSearchListener() {
                        @Override
                        public void onSuccess(final List<MapData> itemList) {
                            showResult(itemList);
                        }

                        @Override
                        public void onFail() {
//							showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
                        }
                    });

//					searcher.searchCategory(getApplicationContext(), query, latitude, longitude, apikey, new OnFinishSearchListener() {
//						@Override
//						public void onSuccess(final List<MapData> itemList) {
//							showResult(itemList);
//						}
//
//						@Override
//						public void onFail() {
////							showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
//						}
//					});


                    mProgressDialog.dismiss();
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 0, 2000);

    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SearchPlaceActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResult(List<MapData> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();

        for (int i = 0; i < itemList.size(); i++) {
            MapData item = itemList.get(i);

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.getTitle());
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.getLatitude(), item.getLongitude());
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
//            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
//            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);

            mMapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), item);

        }

        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        MapPOIItem[] poiItems = mMapView.getPOIItems();
        if (poiItems.length > 0) {
            mMapView.selectPOIItem(poiItems[0], false);
        }
    }

    private Drawable createDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object fetch(String address) throws IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

//        SearchPlaceData item = mTagItemMap.get(mapPOIItem.getTag());
//        StringBuilder sb = new StringBuilder();
//        sb.append("title=").append(item.getTitle()).append("\n");
//        sb.append("imageUrl=").append(item.getImageUrl()).append("\n");
//        sb.append("address=").append(item.getAddress()).append("\n");
//        sb.append("newAddress=").append(item.getNewAddress()).append("\n");
//        sb.append("zipcode=").append(item.getZipcode()).append("\n");
//        sb.append("phone=").append(item.getPhone()).append("\n");
//        sb.append("category=").append(item.getCategory()).append("\n");
//        sb.append("longitude=").append(item.getLongitude()).append("\n");
//        sb.append("latitude=").append(item.getLatitude()).append("\n");
//        sb.append("distance=").append(item.getDistance()).append("\n");
//        sb.append("direction=").append(item.getDirection()).append("\n");
//        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();

        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

    }

    @Override
    @Deprecated
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

        mapView.setMapCenterPoint(mapPOIItem.getMapPoint(), true);
        MapData data = searchData.getSearchList().get(mapPOIItem.getTag());
        Glide.with(this).load(data.getImageUrl()).apply(RequestOptions.circleCropTransform()).into(ivSearchResult);

        tvSearchResultTitle.setText(data.getTitle());
        tvSearchResultAddress.setText(data.getAddress());
        tvSearchResultPhone.setText(data.getPhone());

        wbSearchResult.setWebViewClient(new WebViewClient());
        WebSettings webSettings = wbSearchResult.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wbSearchResult.loadUrl(data.getPlaceUrl());

        selectedData = mTagItemMap.get(mapPOIItem.getTag());
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

//        Log.d("POIItem", mapPOIItem.getCustomSele);

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
    }

    @Override
    public void bindView() {

        this.llBtnSelectPlace = (LinearLayout) findViewById(R.id.llBtnSelectPlace);
        this.slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
        this.tvSearchResultPhone = (TextView) findViewById(R.id.tvSearchResultPhone);
        this.tvSearchResultAddress = (TextView) findViewById(R.id.tvSearchResultAddress);
        this.tvSearchResultTitle = (TextView) findViewById(R.id.tvSearchResultTitle);
        this.ivSearchResult = (ImageView) findViewById(R.id.ivSearchResult);
        this.llSlideMain = (LinearLayout) findViewById(R.id.llSlideMain);
        this.mMapView = (MapView) findViewById(R.id.mMapView);
        this.lvSearchList = (ListView) findViewById(R.id.lvSearchList);
        this.btnSearch = (TextView) findViewById(R.id.btnSearch);
        this.edSearchQuery = (EditText) findViewById(R.id.edSearchQuery);
        this.wbSearchResult = (WebView) findViewById(R.id.wbSearchResult);
        this.scWebView = (ScrollView) findViewById(R.id.scWebView);
        this.tvBtnSearchSelect = (TextView) findViewById(R.id.tvBtnSearchSelect);
        this.tvBtnSearchCancel = (TextView) findViewById(R.id.tvBtnSearchCancel);

    }

}
