package com.huami.employeemanager.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.huami.employeemanager.common.UIHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	//���ݿ���sd���е�·��
	public static final String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/EmployeeManager/database";
	//��Ƭ���·��
	public static final String PHOTO_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/EmployeeManager/photo";
	//���ݿ�����
	public static final String DB_NAME = "employees_manager.db";
	
	private SQLiteDatabase db;
	private Context context;
	
	public DBManager(Context context){
		File databaseDir = new File(DB_PATH);
		File photoDir = new File(PHOTO_PATH);
		
		if(!databaseDir.exists()){
			databaseDir.mkdirs();
		}
		
		if(!photoDir.exists()){
			photoDir.mkdirs();
		}
		
		this.context = context;
		this.db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS employees (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCGHAR, miID VARCHAR, " +
								"department VARCHAR, position VARCHAR, phoneNum VARCHAR, age INTEGER, photo VARCHAR DEFAULT('unknown.png'))");
		
	}
	
	
	
	//���ӵ���Ԫ��
	public void addOne(Employee e){
		db.execSQL("INSERT INTO employees VALUES(NULL,?,?,?,?,?,?,?)", new Object[]{e.getName(), e.getMiID(), e.getDepartment(), e.getPosition(),
																								e.getPhoneNum(), e.getAge(), e.getPhoto()});	
	}
	
	//���Ӷ��Ԫ��
	public void add(List<Employee> es){
		db.beginTransaction();
		
		for(Employee e : es){
			addOne(e);
		}
		
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	//ɾ��Ԫ��
	public int delete(String miID){
		return db.delete("employees", "miID = ?", new String[]{miID});		
	}
	
	//��������
	public void update(Employee e, Employee oldEmployee){
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		e.getPhoto().compress(Bitmap.CompressFormat.PNG, 100, os);
//		db.execSQL("UPDATE employees SET name=?,miID=?,department=?,phoneNum=?,age=?,photo=? WHERE miID=?", 
//										new Object[]{e.getName(), e.getMiID(), e.getDepartment(),
//										e.getPhoneNum(), e.getAge(), os.toByteArray(), oldMiID});
		
		ContentValues cv = new ContentValues();
		cv.put("name", e.getName());
		cv.put("miID", e.getMiID());
		cv.put("department", e.getDepartment());
		cv.put("position", e.getPosition());
		cv.put("phoneNum", e.getPhoneNum());
		cv.put("age", e.getAge());
		cv.put("photo", e.getPhoto());
		
		db.update("employees", cv, "miID=?", new String[]{oldEmployee.getMiID()});
	}
	
	//ͨ��miID��ѯ����
	public ArrayList<Employee> query(String miID){
		ArrayList<Employee> lst = new ArrayList<Employee>();
		Cursor c = db.rawQuery("SELECT * FROM employees WHERE miID=?", new String[]{String.valueOf(miID)});
		
		while(c.moveToNext()){
			Employee person = new Employee();
    		person.set_id(c.getInt(c.getColumnIndex("_id")));
    		person.setName(c.getString(c.getColumnIndex("name")));
    		person.setMiID(c.getString(c.getColumnIndex("miID")));
    		person.setDepartment(c.getString(c.getColumnIndex("department")));
    		person.setPosition(c.getString(c.getColumnIndex("position")));
    		person.setPhoneNum(c.getString(c.getColumnIndex("phoneNum")));
    		person.setAge(c.getInt(c.getColumnIndex("age")));
    		person.setPhoto(PHOTO_PATH + "/" + c.getString(c.getColumnIndex("photo")));
    		lst.add(person);
		}
		
		return lst;
	}
		
	//��ѯ��������
	public ArrayList<Employee> queryAll(){
		ArrayList<Employee> lst = new ArrayList<Employee>();
		Cursor c = db.rawQuery("SELECT * FROM employees", null);
		while(c.moveToNext()){
			Employee person = new Employee();
    		person.set_id(c.getInt(c.getColumnIndex("_id")));
    		person.setName(c.getString(c.getColumnIndex("name")));
    		person.setMiID(c.getString(c.getColumnIndex("miID")));
    		person.setDepartment(c.getString(c.getColumnIndex("department")));
    		person.setPosition(c.getString(c.getColumnIndex("position")));
    		person.setPhoneNum(c.getString(c.getColumnIndex("phoneNum")));
    		person.setAge(c.getInt(c.getColumnIndex("age")));
    		person.setPhoto(PHOTO_PATH + "/" + c.getString(c.getColumnIndex("photo"))); 
    		lst.add(person);
		}
		
		return lst;
	}
	
	public void close(){
		db.close();
	}
}
