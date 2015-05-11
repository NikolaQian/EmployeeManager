package com.huami.employeemanager.database;

import java.io.Serializable;
import android.graphics.Bitmap;

public class Employee implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	private String name;	
	private String miID;
	private String department;
	private String position;
	private String phoneNum;
	private int age;
	private Bitmap photo;
	
	public Employee(){		
		
	}
	
	public Employee(String name, String miID,String department, String phoneNum, int age, Bitmap photo){
		this.name = name;
		this.miID = miID;
		this.department = department;
		this.phoneNum = phoneNum;
		this.age = age;
		this.photo = photo;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getMiID() {
		return miID;
	}

	public void setMiID(String miID) {
		this.miID = miID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	
	
	
}
