package com.huami.employeemanager.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import com.huami.employeemanager.common.StringUtil;
import com.huami.employeemanager.database.Employee;
import com.huami.employeemanager.image.ImageModify;

public class FramentHelper {
	public static void displayFragment(Employee employee, ImageView photoImageView, TextView nameTextView,
										TextView departmentTextView, TextView positionTextView){
		photoImageView.setImageBitmap(ImageModify.toRoundCorner(employee.getPhoto(), 10));
		
		if(employee.getName().length() == 2){
			StringBuilder builder = new StringBuilder(employee.getName());
			builder.insert(1, "  ");
			nameTextView.setText(builder.toString());
		}else {
			nameTextView.setText(employee.getName());
		}
		
		departmentTextView.setText(employee.getDepartment());
		
		if(!StringUtil.isEmpty(employee.getPosition())){
			positionTextView.setText(" " + employee.getPosition());
		}
	}
}
