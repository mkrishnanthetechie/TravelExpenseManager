package com.krishnan.travelexpensemanager;

import com.krishnan.travelexpensemanager.util.Utility;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @author krishm90
 * 
 * This is a Static Action tool bar Fragment. 
 * On clicking of respective actions , respective fragments will be loaded dynamically
 *
 */
public class ActionToolbarFragment extends Fragment implements OnClickListener {

	onActionButtonClickListener actionListner = null;
	Button add_Report = null;
	Button delete_Report = null;
	Button view_Report = null;

	String actionName = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		if (activity instanceof onActionButtonClickListener){
			actionListner = (onActionButtonClickListener) activity;	
		}
		else
			throw new ClassCastException("Activity class "
					+ activity.getClass()
					+ "should implement onActionButtonClickListener interface");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_action_toolbar,
				container, false);
		registerButtonActions(view);
		return view;
	}

	private void registerButtonActions(View view) {
		// TODO Auto-generated method stub
		try {
			add_Report = (Button) view.findViewById(R.id.add_report);
			delete_Report = (Button) view.findViewById(R.id.delete_report);
			view_Report = (Button) view.findViewById(R.id.view_report);

			add_Report.setOnClickListener(this);
			delete_Report.setOnClickListener(this);
			view_Report.setOnClickListener(this);
		}

		catch (Exception exp) {
			String errorLoc = "In registerButtonActions() of" + exp.getClass();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public interface onActionButtonClickListener {
		public void onActionPerformed(String actionName);
	}

	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		if (v != null){
			actionName = String.valueOf(((Button) v).getText());
			Utility.out("Action button clicked :" + actionName);
			actionListner.onActionPerformed(actionName);
		}
		else
			throw new NullPointerException("Error initializing at onClick() at" + ActionToolbarFragment.class);
	}
	
	@Override
	public void onDetach ()
	{
		super.onDetach();
		actionListner = null;
		actionName = null;
	}
}
