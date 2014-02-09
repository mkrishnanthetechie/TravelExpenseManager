package com.krishnan.travelexpensemanager;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.krishnan.travelexpensemanager.model.CustomDayWiseIndividualExpense;
import com.krishnan.travelexpensemanager.model.Expense;
import com.krishnan.travelexpensemanager.model.Report;
import com.krishnan.travelexpensemanager.util.ActionSlideExpandableListView;
import com.krishnan.travelexpensemanager.util.ActionSlideExpandableListView.OnActionClickListener;
import com.krishnan.travelexpensemanager.util.SlideExpandableListAdapter;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Dialog Fragment to view the individual expense report based on day wise / total wise
 *
 */
public class ViewIndividualExpenseFragment extends DialogFragment implements
		OnClickListener, OnItemClickListener, OnActionClickListener {

	private ExpenseHostActivity hostActivity;
	private Bundle args_Bundle;
	private Dialog customDialog = null;
	private TextView total_Members;
	private LinearLayout viewTypeLayout;
	private Button ok_Button;
	private CustomDayWiseIndividualExpense mAdapter;
	private RelativeLayout root_Layout;
	private Resources res;
	private List<String> distinctExpenseList;

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
		if (this.getArguments() != null)
			args_Bundle = this.getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.dialog_individual_expense_view,
				container, false);

		Resources res = hostActivity.getApplicationContext().getResources();

		try {
			Report report = (Report) args_Bundle
					.getSerializable("selected_report");
			customDialog = this.getDialog();
			customDialog.requestWindowFeature(STYLE_NORMAL);
			customDialog
					.setContentView(R.layout.dialog_individual_expense_view);
			customDialog.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			customDialog.setOnDismissListener(this);
			String dialog_Title = String.format(
					res.getString(R.string.individual_expense_dialog_title),
					args_Bundle.getString("total_amount"));
			customDialog.setTitle(dialog_Title);
			registerDialogElements(view);
			total_Members.setText(String.valueOf(report.getPerson_Count()));
			distinctExpenseList = getDistinctExpenseList();
			constructExpandableListView();
			customDialog.show();
		}

		catch (Exception exp) {
			String errorLoc = "In onCreateView() of ViewIndividualExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString());
			Utility.out(customMsg);
		}
		return view;
	}

	public void constructExpandableListView() {
		mAdapter = new CustomDayWiseIndividualExpense(hostActivity,
				distinctExpenseList);

		ActionSlideExpandableListView myListView = (ActionSlideExpandableListView) root_Layout
				.findViewById(R.id.day_wise_expense_list);
		myListView.setAdapter(new SlideExpandableListAdapter(mAdapter));
		myListView.setItemActionListener(this, mAdapter.getCount(),
				R.id.dynamic_individual_expense_text,
				R.id.expandable_toggle_button);
	}

	private List<String> getDistinctExpenseList() {
		// TODO Auto-generated method stub
		List<String> distinctExpenseList = null;
		try {

			Report report = (Report) args_Bundle
					.getSerializable("selected_report");
			distinctExpenseList = hostActivity
					.getDistinctExpenseDatesFromTable(report);

			if (distinctExpenseList == null) {
				throw new NullPointerException(
						res.getString(R.string.no_expenses_found));
			}
		}

		catch (NullPointerException npe) {
			String errorLoc = "Null value received from getDistinctExpenseDatesFromTable() of DbHelperModel class";
			String customMsg = String.format("%s\n%s\n%s", errorLoc,
					npe.getLocalizedMessage(), npe.toString());
			Utility.out(customMsg);
		}

		return distinctExpenseList;
	}

	private void registerDialogElements(View view) {
		// TODO Auto-generated method stub
		try {
			total_Members = (TextView) view
					.findViewById(R.id.total_member_count);
			viewTypeLayout = (LinearLayout) view
					.findViewById(R.id.view_type_layout);
			viewTypeLayout.getChildAt(0).setOnClickListener(this);
			root_Layout = (RelativeLayout) view
					.findViewById(R.id.view_individual_expense_dialog_root);
			ok_Button = (Button) view
					.findViewById(R.id.individual_expense_dialog_close);
			ok_Button.setOnClickListener(this);
			res = hostActivity.getApplicationContext().getResources();
		}

		catch (Exception exp) {
			String errorLoc = "In registerDialogElements() of ViewIndividualExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (customDialog.isShowing())
			customDialog.dismiss();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!customDialog.isShowing())
			customDialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Utility.out("View clicked .. " + v.getId());

		switch (v.getId()) {
		case R.id.view_type_button:
			ToggleButton toggle_View = (ToggleButton) viewTypeLayout
					.getChildAt(0);
			TextView viewTypeText = (TextView) viewTypeLayout.getChildAt(1);
			if (toggle_View.isChecked()) {
				viewTypeText.setText(res
						.getString(R.string.view_type_action_total_share_text));
				toggle_View.setTextColor(Color.parseColor("#00FF00"));
				hidePreviousView("listview");
				showSelectedView("table");
			} else {
				viewTypeText.setText(res
						.getString(R.string.view_type_action_default_text));
				toggle_View.setTextColor(Color.parseColor("#FF0000"));
				hidePreviousView("table");
				showSelectedView("listview");
			}
			break;

		case R.id.individual_expense_dialog_close:
			customDialog.dismiss();
			break;
		}
	}

	private void showSelectedView(String constructView) {
		// TODO Auto-generated method stub
		if (constructView.equalsIgnoreCase("listview")) {
			// constructExpandableListView();
			root_Layout.findViewById(R.id.day_wise_expense_list).setVisibility(
					View.VISIBLE);
		}

		else if (constructView.equalsIgnoreCase("table")) {

			if (root_Layout.findViewWithTag(res
					.getString(R.string.total_expense_table_tag)) != null) {
				constructTotalExpenseTable();
			}
			TableLayout totalExpenseTable = (TableLayout) root_Layout
					.findViewWithTag(res
							.getString(R.string.total_expense_table_rows_populated));
			totalExpenseTable.setVisibility(View.VISIBLE);
		}
	}

	private void constructTotalExpenseTable() {
		// TODO Auto-generated method stub

		TableLayout totalExpenseTable = (TableLayout) root_Layout
				.findViewWithTag(res
						.getString(R.string.total_expense_table_tag));
		try {
			Report report = (Report) args_Bundle
					.getSerializable("selected_report");
			List<Expense> total_Individual_Expenses = hostActivity
					.getMembersTotalExpense(report);

			Expense expense = null;

			Float totalExpenseSpent = Float.valueOf(args_Bundle
					.getString("total_amount"));

			for (int i = 0; i < total_Individual_Expenses.size() * 2; i++) {

				if (i % 2 == 0) {

					if ((i / 2) < total_Individual_Expenses.size())
						expense = total_Individual_Expenses.get((i / 2));

					TableRow row = new TableRow(hostActivity);

					TableRow.LayoutParams row_Params = new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);

					row.setLayoutParams(row_Params);

					row.setTag(i);

					row.setBackgroundColor(Color.parseColor("#77CCDD"));

					row.setGravity(Gravity.CENTER);

					TextView personName = new TextView(hostActivity);

					personName.setLayoutParams(row_Params);

					personName.setBackgroundColor(Color.parseColor("#0000FF"));

					personName.setText(expense.getPerson().getPersonName());

					personName.setGravity(Gravity.CENTER);

					TextView amountText = new TextView(hostActivity);

					amountText.setLayoutParams(row_Params);

					amountText.setBackgroundColor(Color.parseColor("#0000FF"));

					amountText.setText(expense.getExpense_Amount().toString());

					amountText.setGravity(Gravity.CENTER);

					String percentageValue = String.valueOf((expense
							.getExpense_Amount() / totalExpenseSpent) * 100).substring(0, 4)
							+ "%";

					TextView percentageText = new TextView(hostActivity);

					percentageText.setLayoutParams(row_Params);

					percentageText.setText(percentageValue);

					percentageText.setBackgroundColor(Color
							.parseColor("#0000FF"));

					percentageText.setGravity(Gravity.CENTER);

					// #0000FF
					row.addView(personName, 0);
					row.addView(amountText, 1);
					row.addView(percentageText, 2);

					if (totalExpenseTable.findViewWithTag(i) != null) {
						View remove_View = totalExpenseTable.findViewWithTag(i);
						totalExpenseTable.removeView(remove_View);
					}

					totalExpenseTable.addView(row, i + 2);
				}

				else if (i % 2 != 0) {
					TableRow separator_Row = new TableRow(hostActivity);

					TableRow.LayoutParams seperatow_Row_Params = new TableRow.LayoutParams(
							LayoutParams.MATCH_PARENT, 2);

					separator_Row.setLayoutParams(seperatow_Row_Params);

					separator_Row.setBackgroundColor(Color
							.parseColor("#FFFF00"));

					separator_Row.setTag(i);

					TextView separator_Line = new TextView(hostActivity);

					separator_Line.setLayoutParams(seperatow_Row_Params);

					separator_Row.addView(separator_Line, 0);

					if (totalExpenseTable.findViewWithTag(i) != null) {
						View remove_View = totalExpenseTable.findViewWithTag(i);
						totalExpenseTable.removeView(remove_View);
					}

					totalExpenseTable.addView(separator_Row, i + 2);
				}
			}
			totalExpenseTable.setVisibility(View.VISIBLE);
			totalExpenseTable.setTag(res
					.getString(R.string.total_expense_table_rows_populated));
			Utility.out("Total expense table populated with rows");

		} catch (Exception exp) {
			String errorLoc = "In constructTotalExpenseTable() of ViewIndividualExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString());
			Utility.out(customMsg);
		}

	}

	private void hidePreviousView(String removeView) {
		// TODO Auto-generated method stub
		if (removeView.equalsIgnoreCase("listview")) {
			View view = root_Layout.findViewWithTag(res
					.getString(R.string.expandable_listview_tag));
			// root_Layout.removeView(view);
			view.setVisibility(View.INVISIBLE);
		}

		else if (removeView.equalsIgnoreCase("table")) {
			View view = root_Layout
					.findViewById(R.id.total_individual_expense_layout);
			// root_Layout.removeView(view);
			view.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		Utility.out("View Individual dialog is dismissed");
		Toast.makeText(hostActivity, "Individual Expense dialog is dismissed",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Utility.out("Item click received...");
		Utility.out(parent.getItemAtPosition(position));
	}

	@Override
	public void onClick(View itemView, View clickedView, int position) {
		// TODO Auto-generated method stub
		Utility.out("Item view @" + itemView.getId() + "clicked.. @"
				+ clickedView.getId());
	}

	public String getIndividualExpenseForDate(String date) {

		Report report = (Report) args_Bundle.getSerializable("selected_report");

		try {
			String individual_Expense_Given_Date = hostActivity
					.getMembersExpenseForDate(report, date);
			if (individual_Expense_Given_Date != null)
				return individual_Expense_Given_Date;

			else
				throw new NullPointerException(
						"Value for computed individual expense is null from DbHelperModel class");
		}

		catch (Exception exp) {
			String errorLoc = "In getIndividualExpenseForDate() of ViewIndividualExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString());
			Utility.out(customMsg);
		}
		return null;
	}
}
