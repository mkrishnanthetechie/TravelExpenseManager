package com.krishnan.travelexpensemanager;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.krishnan.travelexpensemanager.model.Expense;
import com.krishnan.travelexpensemanager.model.Report;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Fragment to view the expenses spent so far in the selected report
 *
 */
public class ViewExpenseFragment extends Fragment implements OnClickListener {

	private TableLayout expense_Table;
	private Button individual_Expense_Action;
	private LinearLayout total_expense_amount_spent_layout;
	private Bundle args_Bundle;
	private ExpenseHostActivity hostActivity;
	private SparseArray<Expense> final_Expense_Array = null;
	private TableRow selectedRow = null;

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
		if (this.getArguments() != null)
			args_Bundle = this.getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_view_expense, container,
				false);
		Report report = (Report) args_Bundle.getSerializable("selected_report");
		String title = String.format("%s-%s", report.getTrip_Name(),
				report.getStart_Date());
		hostActivity.setCustomActionBarTitle(title);
		bindElements(view);
		populateExpenseTableData();
		return view;
	}

	private void populateExpenseTableData() {
		// TODO Auto-generated method stub

		try {
			final_Expense_Array = hostActivity
					.populateExpenseDetailsForReport(args_Bundle);
			constructExpenseTable(final_Expense_Array);
		}

		catch (Exception exp) {
			String errorLoc = "In populateExpenseTable() of ViewExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}
	}

	private void constructExpenseTable(SparseArray<Expense> final_Expense_Array) {
		// TODO Auto-generated method stub

		try {

			TextView valueView = (TextView) total_expense_amount_spent_layout
					.findViewById(R.id.total_expense_spent);

			Resources res = hostActivity.getApplicationContext().getResources();

			if (final_Expense_Array.size() < 1) {
				TextView text_Total_Expense = (TextView) total_expense_amount_spent_layout
						.findViewById(R.id.text_total_expense_of_trip);
				total_expense_amount_spent_layout.removeView(valueView);
				expense_Table.setVisibility(View.INVISIBLE);
				LinearLayout.LayoutParams text_Total_Expense_Params = new LinearLayout.LayoutParams(
						0, LayoutParams.WRAP_CONTENT, 1.0f);
				text_Total_Expense.setLayoutParams(text_Total_Expense_Params);
				text_Total_Expense.setText(res
						.getString(R.string.no_expenses_found));
				text_Total_Expense.setTextColor(Color.parseColor("#FF0000"));
			}

			else {

				Expense expense = null;

				Float total_Expense_Amount = 0f;

				for (int i = 1; i < final_Expense_Array.size() * 2; i++) {
					if ((i - 1) % 2 == 0) {

						if (((i - 1) / 2) < final_Expense_Array.size())
							expense = final_Expense_Array.get((i / 2) + 1);

						TableRow row = new TableRow(hostActivity);

						TableRow.LayoutParams row_Params = new TableRow.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);

						row.setLayoutParams(row_Params);

						row.setTag(i);

						row.setBackgroundColor(Color.parseColor("#77CCDD"));

						row.setGravity(Gravity.CENTER);

						registerForContextMenu(row);

						TextView si_No = new TextView(hostActivity);

						si_No.setLayoutParams(row_Params);

						si_No.setText(String.valueOf(final_Expense_Array
								.indexOfValue(expense) + 1));

						si_No.setBackgroundColor(Color.parseColor("#0000FF"));

						si_No.setGravity(Gravity.CENTER);

						TextView expense_Name = new TextView(hostActivity);

						expense_Name.setLayoutParams(row_Params);

						expense_Name.setText(expense.getExpense_Name());

						expense_Name.setBackgroundColor(Color
								.parseColor("#0000FF"));

						expense_Name.setGravity(Gravity.CENTER);

						TextView expense_Date = new TextView(hostActivity);

						expense_Date.setLayoutParams(row_Params);

						expense_Date.setText(expense.getExpense_Date());

						expense_Date.setBackgroundColor(Color
								.parseColor("#0000FF"));

						expense_Date.setGravity(Gravity.CENTER);

						TextView amount = new TextView(hostActivity);

						amount.setLayoutParams(row_Params);

						amount.setText(expense.getExpense_Amount().toString());

						amount.setBackgroundColor(Color.parseColor("#0000FF"));

						amount.setGravity(Gravity.CENTER);

						TextView comments = new TextView(hostActivity);

						comments.setLayoutParams(row_Params);

						comments.setText(expense.getExpense_Comments());

						comments.setBackgroundColor(Color.parseColor("#0000FF"));

						comments.setGravity(Gravity.CENTER);

						TextView expense_Type = new TextView(hostActivity);

						expense_Type.setLayoutParams(row_Params);

						expense_Type.setText(expense.getExpense_Type());

						expense_Type.setBackgroundColor(Color
								.parseColor("#0000FF"));

						expense_Type.setGravity(Gravity.CENTER);

						TextView given_By = new TextView(hostActivity);

						given_By.setLayoutParams(row_Params);

						given_By.setText(expense.getPerson().getPersonName());

						given_By.setBackgroundColor(Color.parseColor("#0000FF"));

						given_By.setGravity(Gravity.CENTER);

						TextView sharing_Count = new TextView(hostActivity);

						sharing_Count.setLayoutParams(row_Params);

						if (expense.getExpense_Type().equalsIgnoreCase(
								"individual"))
							sharing_Count.setText(String.valueOf(0));
						else
							sharing_Count.setText(String.valueOf(expense
									.getSharing_Count()));

						sharing_Count.setBackgroundColor(Color
								.parseColor("#0000FF"));

						sharing_Count.setGravity(Gravity.CENTER);

						// #0000FF
						row.addView(si_No, 0);
						row.addView(expense_Name, 1);
						row.addView(expense_Date, 2);
						row.addView(amount, 3);
						row.addView(comments, 4);
						row.addView(expense_Type, 5);
						row.addView(given_By, 6);
						row.addView(sharing_Count, 7);

						if (expense_Table.findViewWithTag(i) != null) {
							View remove_View = expense_Table.findViewWithTag(i);
							expense_Table.removeView(remove_View);
						}

						total_Expense_Amount += expense.getExpense_Amount();

						expense_Table.addView(row, i + 2);
					}

					else if ((i - 1) % 2 != 0) {
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

						if (expense_Table.findViewWithTag(i) != null) {
							View remove_View = expense_Table.findViewWithTag(i);
							expense_Table.removeView(remove_View);
						}

						expense_Table.addView(separator_Row, i + 2);
					}
				}
				valueView.setText(total_Expense_Amount.toString());
				individual_Expense_Action.setEnabled(true);
				args_Bundle.putString("total_amount",
						total_Expense_Amount.toString());
			}

		} catch (Exception exp) {
			String errorLoc = "In constructExpenseTable() of ViewExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

	}

	private void bindElements(View view) {
		// TODO Auto-generated method stub

		try {
			expense_Table = (TableLayout) view
					.findViewById(R.id.view_expense_table);
			individual_Expense_Action = (Button) view
					.findViewById(R.id.view_individual_expense_action);
			total_expense_amount_spent_layout = (LinearLayout) view
					.findViewById(R.id.total_expense_amount_spent_layout);
			individual_Expense_Action.setOnClickListener(this);
			individual_Expense_Action.setEnabled(false);
		}

		catch (Exception exp) {
			String errorLoc = "In bindElements() of ViewExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.view_individual_expense_action:
			Utility.out("Individual Action clicked...");
			hostActivity.onIndividualExpenseClicked(args_Bundle);
			break;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Utility.out("Selected Menu : " + item.getTitle());
		Utility.out("Selected table row is " + selectedRow.getTag());
		int key = ((Integer) selectedRow.getTag()) / 2;
		Expense selected_Expense = final_Expense_Array.get(final_Expense_Array
				.keyAt(key));

		if (selected_Expense != null) {
			Utility.out("Selected Row contents "
					+ String.format("%s-%s-%f",
							selected_Expense.getExpense_Name(),
							selected_Expense.getExpense_Date(),
							selected_Expense.getExpense_Amount()));
			args_Bundle.putString("selected_menu", item.getTitle().toString());
			args_Bundle.putSerializable("selected_expense", selected_Expense);
			Toast.makeText(
					hostActivity,
					item.getTitle() + " functinality will be implemented later",
					Toast.LENGTH_LONG).show();
			hostActivity.onContextMenuItemSelected(args_Bundle);
		} else {
			Toast.makeText(hostActivity,
					"There is an error occurred.. Please try after some time",
					Toast.LENGTH_LONG).show();
			Utility.out("Selected Expense is set to Null in View Expense Fragment class");
			hostActivity.finish();
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		setSelectedRow(v);
		MenuInflater inflater = hostActivity.getMenuInflater();
		inflater.inflate(R.menu.expense_action_menu, menu);
	}

	private void setSelectedRow(View v) {
		// TODO Auto-generated method stub
		selectedRow = (TableRow) v;
		selectedRow.setBackgroundColor(Color.parseColor("#00FFFF"));
	}

	interface onExpenseActionListener {
		public void onContextMenuItemSelected(Bundle args);

		public void onIndividualExpenseClicked(Bundle args);
	}
}
