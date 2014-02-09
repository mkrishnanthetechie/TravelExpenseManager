package com.krishnan.travelexpensemanager;

import java.io.Serializable;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

import com.krishnan.travelexpensemanager.model.CustomViewReportListAdapter;
import com.krishnan.travelexpensemanager.model.Report;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Fragment to select the report for viewing the expense related to it.
 *
 */
@SuppressLint("DefaultLocale")
public class ViewReportFragment extends ListFragment {

	private Bundle args_Bundle;
	private ExpenseHostActivity hostActivity;
	private ArrayAdapter<Report> custom_Report_Adapter;
	private LinearLayout view_Report_List_Layout;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (getActivity() != null)
			hostActivity = (ExpenseHostActivity) getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onActivityCreated(savedInstanceState);

			ArrayList<Report> report_List = hostActivity.getReportDetails();

			custom_Report_Adapter = new CustomViewReportListAdapter(
					hostActivity, report_List);
			this.setListAdapter(custom_Report_Adapter);
			TableLayout.LayoutParams params = new TableLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 0, report_List.size());

			getListView().setLayoutParams(params);
		} catch (Exception exp) {
			String errorLoc = "In onActivityCreated() of ViewExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
			view_Report_List_Layout.findViewById(
					R.id.view_no_reports_found_error).setVisibility(
					View.VISIBLE);
			view_Report_List_Layout
					.findViewById(R.id.view_action_select_report)
					.setVisibility(View.INVISIBLE);
			getListView().setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public ListView getListView() {
		// TODO Auto-generated method stub
		return super.getListView();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		ListView parent = l;

		Utility.out(parent.getItemAtPosition(position));

		Report report = (Report) parent.getItemAtPosition(position);

		Utility.out(String.format("%s --> %s --> %d", report.getTrip_Name(),
				report.getStart_Date(), report.getPerson_Count()));

		args_Bundle.putSerializable("selected_report", (Serializable) report);

		Toast.makeText(
				getActivity(),
				String.format("%s --> %s --> %d", report.getTrip_Name(),
						report.getStart_Date(), report.getPerson_Count()),
				Toast.LENGTH_SHORT).show();

		hostActivity.onReportSelectedFromView(args_Bundle);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (this.getArguments() != null)
			args_Bundle = this.getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_view_report, container,
				false);
		view_Report_List_Layout = (LinearLayout) view
				.findViewById(R.id.view_report_list_layout);
		return view;
	}

	@Override
	public void setListAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		super.setListAdapter(adapter);
	}

	interface onReportSelectedFromViewListener {
		public void onReportSelectedFromView(Bundle args);
	}

}
