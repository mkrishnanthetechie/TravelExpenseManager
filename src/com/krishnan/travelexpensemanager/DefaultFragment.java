package com.krishnan.travelexpensemanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author krishm90
 * 
 * Default fragment which get loaded initially 
 *
 */
public class DefaultFragment extends Fragment {

	private ExpenseHostActivity hostActivity;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (getActivity() != null)
			hostActivity = (ExpenseHostActivity) getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		hostActivity.setCustomActionBarTitle("Travel Expense Manager");
		return inflater.inflate(R.layout.fragment_default, container, false);
	}

}
