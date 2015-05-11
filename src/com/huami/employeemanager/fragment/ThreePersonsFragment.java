package com.huami.employeemanager.fragment;

import java.util.ArrayList;

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

public class ThreePersonsFragment extends Fragment {
	private ArrayList<Employee> listItem;
	
	public ThreePersonsFragment(){
	}
	
	public ThreePersonsFragment(ArrayList<Employee> listItem){
		this.listItem = listItem;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.threepersons_fragment, container, false);
		
		Employee employee1 = listItem.get(0);
		Employee employee2 = listItem.get(1);
		Employee employee3 = listItem.get(2);
		
		ImageView photoImageView1 = (ImageView)view.findViewById(R.id.photo_threepersons1);
		ImageView photoImageView2 = (ImageView)view.findViewById(R.id.photo_threepersons2);
		ImageView photoImageView3 = (ImageView)view.findViewById(R.id.photo_threepersons3);
		
		TextView nameTextView1 = (TextView)view.findViewById(R.id.name_threepersons1);
		TextView nameTextView2 = (TextView)view.findViewById(R.id.name_threepersons2);
		TextView nameTextView3 = (TextView)view.findViewById(R.id.name_threepersons3);
		TextView departmentTextView1 = (TextView)view.findViewById(R.id.department_threepersons1);
		TextView departmentTextView2 = (TextView)view.findViewById(R.id.department_threepersons2);
		TextView departmentTextView3 = (TextView)view.findViewById(R.id.department_threepersons3);
		TextView positionTextView1 = (TextView)view.findViewById(R.id.position_threepersons1);
		TextView positionTextView2 = (TextView)view.findViewById(R.id.position_threepersons2);
		TextView positionTextView3 = (TextView)view.findViewById(R.id.position_threepersons3);
		
		FramentHelper.displayFragment(employee1, photoImageView1, nameTextView1, departmentTextView1, positionTextView1);
		FramentHelper.displayFragment(employee2, photoImageView2, nameTextView2, departmentTextView2, positionTextView2);
		FramentHelper.displayFragment(employee3, photoImageView3, nameTextView3, departmentTextView3, positionTextView3);
		
		return view;
	}
}
