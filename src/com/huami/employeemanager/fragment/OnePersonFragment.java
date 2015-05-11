package com.huami.employeemanager.fragment;

import com.huami.employeemanager.activity.R;
import com.huami.employeemanager.database.Employee;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class OnePersonFragment extends Fragment {
	private Employee employee;
	
	public OnePersonFragment(){
	}
	
	public OnePersonFragment(Employee employee){
		this.employee = employee;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.oneperson_fragment, container, false);
		
		ImageView photoImageView = (ImageView)view.findViewById(R.id.photo_oneperson);
		TextView nameTextView = (TextView)view.findViewById(R.id.name_oneperson);
		TextView departmentTextView = (TextView)view.findViewById(R.id.department_oneperson);
		TextView positionTextView = (TextView)view.findViewById(R.id.position_oneperson);
		
		FramentHelper.displayFragment(employee, photoImageView, nameTextView, departmentTextView, positionTextView);
		
		return view;
	}
	
}
