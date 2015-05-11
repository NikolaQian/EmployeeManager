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

public class TowPersonsFragment extends Fragment {
	private ArrayList<Employee> listItem;
	
	public TowPersonsFragment(){
	}
	
	public TowPersonsFragment(ArrayList<Employee> listItem){
		this.listItem = listItem;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.towpersons_fragment, container, false);
		
		Employee employee1 = listItem.get(0);
		Employee employee2 = listItem.get(1);
		
		ImageView photoImageView1 = (ImageView)view.findViewById(R.id.photo_twopersons1);
		ImageView photoImageView2 = (ImageView)view.findViewById(R.id.photo_twopersons2);
		
		TextView nameTextView1 = (TextView)view.findViewById(R.id.name_twopersons1);
		TextView nameTextView2 = (TextView)view.findViewById(R.id.name_twopersons2);
		TextView departmentTextView1 = (TextView)view.findViewById(R.id.department_twopersons1);
		TextView departmentTextView2 = (TextView)view.findViewById(R.id.department_twopersons2);
		TextView positionTextView1 = (TextView)view.findViewById(R.id.position_twopersons1);
		TextView positionTextView2 = (TextView)view.findViewById(R.id.position_twopersons2);
		
		FramentHelper.displayFragment(employee1, photoImageView1, nameTextView1, departmentTextView1, positionTextView1);
		FramentHelper.displayFragment(employee2, photoImageView2, nameTextView2, departmentTextView2, positionTextView2);
		
		return view;
	}
	
}
