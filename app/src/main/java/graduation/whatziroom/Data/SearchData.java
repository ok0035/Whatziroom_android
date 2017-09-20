package graduation.whatziroom.Data;

import java.util.ArrayList;

import graduation.whatziroom.activity.room.SearchPlaceActivity;
import graduation.whatziroom.adapter.SearchAdapter;

/**
 * Created by mapl0 on 2017-09-20.
 */

public class SearchData {

	private String imageUrl, title, address, newAddress, zipcode, phone, category, direction, id, placeUrl, addressBCode;
	private double longitude, latitude, distance;
	private SearchAdapter adapter;
	private ArrayList<SearchData> searchList;

	public SearchData() {
		searchList = new ArrayList<SearchData>();
		adapter = new SearchAdapter(SearchPlaceActivity.searchContext, searchList);
	}

	public SearchData(String imageUrl, String title, String address, String newAddress, String zipcode, String phone, String category,
					  double longitude, double latitude, double distance, String direction, String id, String placeUrl, String addressBCode) {

		setImageUrl(imageUrl);
		setTitle(title);
		setAddress(address);
		setNewAddress(newAddress);
		setZipcode(zipcode);
		setPhone(phone);
		setCategory(category);
		setLongitude(longitude);
		setLatitude(latitude);
		setDistance(distance+"");
		setDirection(direction);
		setId(id);
		setPlaceUrl(placeUrl);
		setAddressBCode(addressBCode);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlaceUrl() {
		return placeUrl;
	}

	public void setPlaceUrl(String placeUrl) {
		this.placeUrl = placeUrl;
	}

	public String getAddressBCode() {
		return addressBCode;
	}

	public void setAddressBCode(String addressBCode) {
		this.addressBCode = addressBCode;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNewAddress() {
		return newAddress;
	}

	public void setNewAddress(String newAddress) {
		this.newAddress = newAddress;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		if(distance.equals(""))
			this.distance = 0;
		else
			this.distance = Double.parseDouble(distance);
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public SearchAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(SearchAdapter adapter) {
		this.adapter = adapter;
	}

	public ArrayList<SearchData> getSearchList() {
		return searchList;
	}

	public void setSearchList(ArrayList<SearchData> searchList) {
		this.searchList = searchList;
	}

	public void addItem(String imageUrl, String title, String address, String newAddress, String zipcode, String phone,
						String category, double longitude, double latitude, double distance, String direction, String id, String placeUrl, String addressBCode) {

		searchList.add(new SearchData(imageUrl, title, address, newAddress, zipcode, phone, category, longitude, latitude, distance, direction, id, placeUrl, addressBCode));

	}
}
