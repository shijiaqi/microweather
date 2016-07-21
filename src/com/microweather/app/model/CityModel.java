package com.microweather.app.model;

import java.util.List;

public class CityModel {

	public String city_id;
	public String name;
	public String en;
	public List<CountyModel> list;
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEn() {
		return en;
	}
	public void setEn(String en) {
		this.en = en;
	}
	public List<CountyModel> getList() {
		return list;
	}
	public void setList(List<CountyModel> list) {
		this.list = list;
	}
	
	
}
