package com.krishnan.travelexpensemanager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.krishnan.travelexpensemanager.model.Report;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Fragment to show the reports added so far to add expense / delete any unwanted reports
 *
 */
public class SelectReportFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener {

	private Bundle args_Bundle;
	private Resources res;
	private ExpenseHostActivity hostActivity;
	private TableLayout selected_Report;

	private Spinner report_list;
	private TextView no_report_found;
	private TextView dynamicTripNameText;
	private TextView dynamicStartDateText;
	private TextView dynamicEndDateText;
	private TextView dynamicMembersCountText;

	private Button go;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (getActivity() != null) {
			hostActivity = (ExpenseHostActivity) getActivity();
			res = hostActivity.getApplicationContext().getResources();
		}
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

		View view = null;

		try {
			super.onCreateView(inflater, container, savedInstanceState);
			view = inflater.inflate(R.layout.fragment_select_report, container,
					false);

			selected_Report = (TableLayout) view
					.findViewById(R.id.selected_report);

			if (args_Bundle != null) {
				String action = args_Bundle.getString("action");
				String title = action.equalsIgnoreCase(res
						.getString(R.string.add_report)) ? "Select Report"
						: "Delete Report";
				hostActivity.setCustomActionBarTitle(title);
			}

			registerActions(view);
			ArrayList<Report> report_List = getReportList(hostActivity
					.getApplicationContext());

			if (report_List != null) {
				ArrayAdapter<Report> adapter = new ArrayAdapter<Report>(
						hostActivity, R.layout.spinner_row, report_List);
				report_list.setAdapter(adapter);
			}
		}

		catch (Exception exp) {
			String errorLoc = "In onCreateView() of SelectReportFragment class";

			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
			if (args_Bundle.getString("action").equalsIgnoreCase(
					res.getString(R.string.delete_report))) {
				no_report_found.setText(res
						.getString(R.string.delete_no_reports_added));
			}

			no_report_found.setVisibility(View.VISIBLE);
			selected_Report.setVisibility(View.INVISIBLE);
			view.findViewById(R.id.select_report_layout).setVisibility(
					View.INVISIBLE);
		}

		return view;
	}

	public ArrayList<Report> getReportList(Context context) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<Report> report_List = null;

		try {
			report_List = hostActivity.getReportDetails();
		}

		catch (Exception exp) {
			throw new Exception(exp.toString());
		}

		return report_List;
	}

	private void registerActions(View view) {
		// TODO Auto-generated method stub
		try {
			report_list = (Spinner) view.findViewById(R.id.select_report);
			no_report_found = (TextView) view
					.findViewById(R.id.no_reports_found_error);

			dynamicTripNameText = (TextView) view
					.findViewById(R.id.dynamic_trip_name_text);
			dynamicStartDateText = (TextView) view
					.findViewById(R.id.dynamic_start_date_text);
			dynamicEndDateText = (TextView) view
					.findViewById(R.id.dynamic_end_date_text);
			dynamicMembersCountText = (TextView) view
					.findViewById(R.id.dynamic_members_count_text);

			report_list.setOnItemSelectedListener(this);
			go = (Button) view.findViewById(R.id.select_report_action);
			go.setOnClickListener(this);
			if (args_Bundle.getString("action").equalsIgnoreCase(
					res.getString(R.string.delete_report))) {
				go.setText("Delete");
			}
			go.setEnabled(false);
		} catch (Exception exp) {
			String errorLoc = "In registerActions() of"
					+ exp.getClass().getName();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.select_report_action) {
			if (args_Bundle.getString("action").equalsIgnoreCase(
					res.getString(R.string.add_report))) {
				hostActivity.onAddExpenseFromReportClicked(args_Bundle);
			}

			else if (args_Bundle.getString("action").equalsIgnoreCase(
					res.getString(R.string.delete_report))) {
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
						hostActivity);
				alertBuilder
						.setCancelable(false)
						.setTitle(
								res.getString(R.string.action_alert_dialog_title))
						.setMessage(
								res.getString(R.string.delete_report_confirmation))
						.setPositiveButton(
								res.getString(R.string.alert_positive_button_text),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										hostActivity
												.onDeleteReportClicked(args_Bundle);
									}
								})
						.setNegativeButton(
								res.getString(R.string.alert_negative_button_text),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
				AlertDialog dialog = alertBuilder.create();
				dialog.show();
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		Utility.out("Selected position:-> " + position);
		Report report = (Report) report_list.getItemAtPosition(position);
		Utility.out(report.getTrip_Name() + "->" + report.getStart_Date()
				+ "->" + report.getPerson_Count());
		args_Bundle.putSerializable("report", report);
		showSelectedReportDetail(report);
	}

	private void showSelectedReportDetail(Report report) {
		// TODO Auto-generated method stub
		try {
			dynamicTripNameText.setText(report.getTrip_Name());
			dynamicStartDateText.setText(report.getStart_Date());
			dynamicEndDateText.setText(report.getEnd_Date());
			dynamicMembersCountText.setText(" " + report.getPerson_Count());
			go.setEnabled(true);
		}

		catch (Exception exp) {
			String errorLoc = "In showSelectedReportDetail() of SelectReportFragment class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public interface onAddExpenseFromReportListener {
		public void onAddExpenseFromReportClicked(Bundle args);
	}

	public interface onDeleteReportListener {
		public void onDeleteReportClicked(Bundle args);
	}

}
