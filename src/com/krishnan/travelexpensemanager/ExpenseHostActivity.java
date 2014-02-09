package com.krishnan.travelexpensemanager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.krishnan.travelexpensemanager.ActionToolbarFragment.onActionButtonClickListener;
import com.krishnan.travelexpensemanager.ActionTypeFragment.onActionTypeSelectListener;
import com.krishnan.travelexpensemanager.AddExpenseFragment.onSaveExpenseActionListener;
import com.krishnan.travelexpensemanager.AddExpenseFragment.onSharedExpenseActionListener;
import com.krishnan.travelexpensemanager.AddPersonNamesList.onAddExpenseClickListener;
import com.krishnan.travelexpensemanager.AddReportFragment.onNextClickListener;
import com.krishnan.travelexpensemanager.SelectReportFragment.onAddExpenseFromReportListener;
import com.krishnan.travelexpensemanager.ViewExpenseFragment.onExpenseActionListener;
import com.krishnan.travelexpensemanager.ViewReportFragment.onReportSelectedFromViewListener;
import com.krishnan.travelexpensemanager.model.DbHelperModel;
import com.krishnan.travelexpensemanager.model.Expense;
import com.krishnan.travelexpensemanager.model.Person;
import com.krishnan.travelexpensemanager.model.Report;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * 
 * Main (Host) Activity which manages the life cycle of the entire application.
 * Based on the action selected in Action tool bar fragment , this activity hosts
 * the dynamic fragments
 *
 */
public class ExpenseHostActivity extends ActionBarActivity implements
		onActionButtonClickListener, onNextClickListener,
		onAddExpenseClickListener, onActionTypeSelectListener,
		onAddExpenseFromReportListener, onSharedExpenseActionListener,
		onSaveExpenseActionListener, onReportSelectedFromViewListener,
		onExpenseActionListener {

	ActionBar actionBar;
	private Bundle buttonBundle;
	DbHelperModel model = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout);
		model = new DbHelperModel(this.getApplicationContext());

		if (findViewById(R.id.expense_host_activity) != null) {
			// To avoid overlapping of fragments
			if (savedInstanceState != null)
				return;

			DefaultFragment defaultFragment = new DefaultFragment();
			defaultFragment.setArguments(getIntent().getExtras());
			FragmentTransaction fragmentTransaction = getFragmentTransaction();

			fragmentTransaction.add(R.id.expense_host_activity,
					new ActionToolbarFragment());

			fragmentTransaction.add(R.id.dynamic_fragment_container,
					defaultFragment);

			fragmentTransaction.commit();
		}
	}

	public FragmentTransaction getFragmentTransaction() {
		return getSupportFragmentManager().beginTransaction();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	@Override
	public void onActionPerformed(String actionName) {
		// TODO Auto-generated method stub
		Bundle args = new Bundle();
		args.putString("action", actionName);

		if (actionName.equalsIgnoreCase("Add")) {
			FragmentTransaction fragmentTransaction = getFragmentTransaction();
			if (fragmentTransaction != null) {
				ActionTypeFragment type_Fragment = new ActionTypeFragment();
				type_Fragment.setArguments(args);

				Fragment defaultFragment = getSupportFragmentManager()
						.findFragmentById(R.id.dynamic_fragment_container);

				if (defaultFragment != null)
					fragmentTransaction.replace(
							R.id.dynamic_fragment_container, type_Fragment);

				else
					fragmentTransaction.add(R.id.dynamic_fragment_container,
							type_Fragment);

				fragmentTransaction.commit();

			} else {
				throw new NullPointerException(
						"Fragment Manager/Transaction is null in "
								+ ExpenseHostActivity.class);
			}
		}

		else if (actionName.equalsIgnoreCase("Drop")) {
			SelectReportFragment selectReportFragment = new SelectReportFragment();
			replaceFragmentWithAnother(selectReportFragment, args,
					"delete_report");
		}

		else if (actionName.equalsIgnoreCase("View")) {
			ViewReportFragment view_Fragment = new ViewReportFragment();
			replaceFragmentWithAnother(view_Fragment, args, "view_report_list");
		}
	}

	public void setCustomActionBarTitle(String title) {
		actionBar = getSupportActionBar();

		if (actionBar != null)
			actionBar.setTitle(title);

	}

	public void setBundle(Bundle buttonBundle) {
		this.buttonBundle = buttonBundle;
	}

	public Bundle getBundle() {
		return this.buttonBundle;
	}

	public void setButtonData(int id, String text) {
		if (text != null) {
			Utility.out(id + " text value before button set" + text);
			((Button) findViewById(id)).setText(text);
		}

	}

	@Override
	public void showPersonFragment(Bundle args) {
		try {
			FragmentTransaction fragmentTransaction = getFragmentTransaction();
			AddPersonNamesList personNames = new AddPersonNamesList();
			personNames.setArguments(args);

			Fragment add_Report_fragment = getSupportFragmentManager()
					.findFragmentById(R.id.dynamic_fragment_container);

			if (add_Report_fragment != null)
				fragmentTransaction.replace(R.id.dynamic_fragment_container,
						personNames);

			else
				fragmentTransaction.add(R.id.dynamic_fragment_container,
						personNames);

			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

			fragmentTransaction.commit();
		} catch (Exception exp) {
			String errorLoc = "In showPersonFragment() of" + exp.getClass();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onAddExpenseClicked(Bundle args) {
		// TODO Auto-generated method stub
		try {
			model.insertInitialReportEntries(args);
			Report report = model.getReportDetails(args.getString("trip_name"),
					args.getString("start_date")).get(0);

			args.putSerializable("report", report);
			AddExpenseFragment add_ExpenseFragment = new AddExpenseFragment();
			replaceFragmentWithAnother(add_ExpenseFragment, args, "add_expense");
		} catch (SQLException exp) {
			String errorLoc = "In onAddExpenseClicked() of"
					+ exp.getClass().getName();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		} catch (ParseException pe) {
			String errorLoc = "In onAddExpenseClicked() of"
					+ pe.getClass().getName();
			String customMsg = String
					.format("%s\n%s\n%s\n%s", errorLoc,
							pe.getLocalizedMessage(), pe.toString(),
							pe.getStackTrace());
			Utility.out(customMsg);
		}

		catch (Exception exp) {
			String errorLoc = "In onAddExpenseClicked() of"
					+ exp.getClass().getName();
			String customMsg = String.format("%s\n%s\n%s\n", errorLoc,
					exp.getLocalizedMessage(), exp.toString());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onActionTypeSelected(Bundle args) {
		// TODO Auto-generated method stub
		String action = args.getString("action");
		int type_Id = args.getInt("type_id");

		String radio_Text = ((RadioButton) findViewById(type_Id)).getText()
				.toString();

		Resources res = this.getApplicationContext().getResources();

		if (action.equalsIgnoreCase(res.getString(R.string.add_report))) {

			if (radio_Text.equalsIgnoreCase(res
					.getString(R.string.report_radio_text))) {
				AddReportFragment add_Fragment = new AddReportFragment();
				replaceFragmentWithAnother(add_Fragment, args, "select_report");
			} else if (radio_Text.equalsIgnoreCase(res
					.getString(R.string.expense_radio_text))) {
				SelectReportFragment selectReportFragment = new SelectReportFragment();
				replaceFragmentWithAnother(selectReportFragment, args,
						"select_expense");
			}
		} else if (action.equalsIgnoreCase(res
				.getString(R.string.delete_report))) {
			if (radio_Text.equalsIgnoreCase(res
					.getString(R.string.report_radio_text))) {

			} else if (radio_Text.equalsIgnoreCase(res
					.getString(R.string.expense_radio_text))) {

			}
		}
	}

	public void replaceFragmentWithAnother(Fragment replace_Fragment,
			Bundle args, String tag) {
		FragmentTransaction fragmentTransaction = getFragmentTransaction();
		if (fragmentTransaction != null) {
			replace_Fragment.setArguments(args);

			fragmentTransaction.replace(R.id.dynamic_fragment_container,
					replace_Fragment, tag);

			fragmentTransaction.commit();

		} else {
			throw new NullPointerException(
					"Fragment Manager/Transaction is null in "
							+ ExpenseHostActivity.class.getName());
		}
	}

	@Override
	public void onAddExpenseFromReportClicked(Bundle args) {
		// TODO Auto-generated method stub
		AddExpenseFragment add_ExpenseFragment = new AddExpenseFragment();
		replaceFragmentWithAnother(add_ExpenseFragment, args, "add_expense");
	}

	public String onExpenseTypeChanged(int checkedId) {
		String radio_Text = ((RadioButton) findViewById(checkedId)).getText()
				.toString();
		Utility.out(radio_Text);
		return radio_Text;
	}

	@Override
	public void onSharedExpenseClicked(Bundle args) {
		// TODO Auto-generated method stub

		SharedExpenseFragment sharedExpenseFragment = new SharedExpenseFragment();
		sharedExpenseFragment.setArguments(args);
		sharedExpenseFragment.show(getSupportFragmentManager(),
				"shared_expense_dialog");
	}

	@SuppressWarnings("unchecked")
	public void onSharedExpenseDialogDismissed(Bundle args) {
		try {

			Report report = (Report) args.getSerializable("report");
			String title = String.format("%s-%s", report.getTrip_Name(),
					report.getStart_Date());
			this.setCustomActionBarTitle(title);

			TableLayout shared_Expense_Table = (TableLayout) findViewById(args
					.getInt("table_layout_id"));

			Expense expense = null;

			Resources res = this.getApplicationContext().getResources();

			List<Expense> shared_Expense_List = (List<Expense>) args
					.getSerializable("shared_expense_list");

			shared_Expense_Table.removeAllViews();

			TableRow header_row = new TableRow(this);

			header_row.setGravity(Gravity.CENTER);

			TableRow.LayoutParams header_Params = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			header_Params.setMargins(2, 3, 2, 3);

			TextView headerCol1 = new TextView(this);

			headerCol1.setLayoutParams(header_Params);

			headerCol1.setText(res.getString(R.string.column_rownum_text));

			headerCol1.setTextAppearance(this,
					R.id.add_expense_table_column_style);

			headerCol1.setTextColor(Color.parseColor("#0000FF"));

			headerCol1.setGravity(Gravity.CENTER);

			TextView headerCol2 = new TextView(this);

			headerCol2.setLayoutParams(header_Params);

			headerCol2.setText(res.getString(R.string.column_person_name_text));

			headerCol2.setTextAppearance(this,
					R.id.add_expense_table_column_style);

			headerCol2.setTextColor(Color.parseColor("#0000FF"));

			headerCol2.setGravity(Gravity.CENTER);

			TextView headerCol3 = new TextView(this);

			headerCol3.setLayoutParams(header_Params);

			headerCol3.setText(res.getString(R.string.column_amount_text));

			headerCol3.setTextAppearance(this,
					R.id.add_expense_table_column_style);

			headerCol3.setTextColor(Color.parseColor("#0000FF"));

			headerCol3.setGravity(Gravity.CENTER);

			header_row.addView(headerCol1, 0);

			header_row.addView(headerCol2, 1);

			header_row.addView(headerCol3, 2);

			shared_Expense_Table.addView(header_row, 0);

			TableRow separator_Header_Row = new TableRow(this);

			TableRow.LayoutParams separator_Header_Row_Params = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, 2);

			separator_Header_Row.setLayoutParams(separator_Header_Row_Params);

			separator_Header_Row
					.setBackgroundColor(Color.parseColor("#FFFF00"));

			TextView header_separator_Line = new TextView(this);

			header_separator_Line.setLayoutParams(separator_Header_Row_Params);

			separator_Header_Row.addView(header_separator_Line, 0);

			shared_Expense_Table.addView(separator_Header_Row, 1);

			for (int i = 0; i < shared_Expense_List.size() * 2; i++) {

				if (i % 2 == 0) {

					if ((i / 2) < shared_Expense_List.size())
						expense = shared_Expense_List.get((i / 2));

					TableRow row = new TableRow(this);

					TableRow.LayoutParams row_Params = new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);

					row.setLayoutParams(row_Params);

					row.setTag(i);

					row.setBackgroundColor(Color.parseColor("#77CCDD"));

					row.setGravity(Gravity.CENTER);

					TextView si_No = new TextView(this);

					si_No.setLayoutParams(row_Params);

					si_No.setText(String.valueOf(shared_Expense_List
							.indexOf(expense) + 1));

					si_No.setBackgroundColor(Color.parseColor("#0000FF"));

					si_No.setGravity(Gravity.CENTER);

					TextView person_Name = new TextView(this);

					person_Name.setLayoutParams(row_Params);

					person_Name.setText(expense.getPerson().getPersonName());

					person_Name.setBackgroundColor(Color.parseColor("#0000FF"));

					person_Name.setGravity(Gravity.CENTER);

					TextView amount = new TextView(this);

					amount.setLayoutParams(row_Params);

					amount.setText(expense.getExpense_Amount().toString());

					amount.setBackgroundColor(Color.parseColor("#0000FF"));

					amount.setGravity(Gravity.CENTER);
					// #0000FF
					row.addView(si_No, 0);
					row.addView(person_Name, 1);
					row.addView(amount, 2);

					if (shared_Expense_Table.findViewWithTag(i) != null) {
						View remove_View = shared_Expense_Table
								.findViewWithTag(i);
						shared_Expense_Table.removeView(remove_View);
					}

					shared_Expense_Table.addView(row, i + 2);
				}

				else if (i % 2 != 0) {
					TableRow separator_Row = new TableRow(this);

					TableRow.LayoutParams seperatow_Row_Params = new TableRow.LayoutParams(
							LayoutParams.MATCH_PARENT, 2);

					separator_Row.setLayoutParams(seperatow_Row_Params);

					separator_Row.setBackgroundColor(Color
							.parseColor("#FFFF00"));

					separator_Row.setTag(i);

					TextView separator_Line = new TextView(this);

					separator_Line.setLayoutParams(seperatow_Row_Params);

					separator_Row.addView(separator_Line, 0);

					if (shared_Expense_Table.findViewWithTag(i) != null) {
						View remove_View = shared_Expense_Table
								.findViewWithTag(i);
						shared_Expense_Table.removeView(remove_View);
					}

					shared_Expense_Table.addView(separator_Row, i + 2);
				}
			}
			shared_Expense_Table.setVisibility(View.VISIBLE);

			AddExpenseFragment add_Expense_Fragment = (AddExpenseFragment) getSupportFragmentManager()
					.findFragmentByTag("add_expense");
			add_Expense_Fragment.afterSharedExpenseDialogDismissed(args);

		} catch (Exception exp) {
			String errorLoc = "In onSharedExpenseDialogDismissed() of ExpenseHostActivity";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

	}

	@Override
	public void onSaveExpenseClicked(Bundle args) {
		// TODO Auto-generated method stub
		try {

			Utility.out("Save action received");

			Expense expense = (Expense) args.getSerializable("expense");

			Utility.out(expense.getExpense_Name() + "--"
					+ expense.getExpense_Date() + "--"
					+ expense.getExpense_Time());

			DbHelperModel dbHelperModel = new DbHelperModel(
					getApplicationContext());

			dbHelperModel.insertExpenseDetails(args);

			Toast.makeText(this, "Expense data saved. ", Toast.LENGTH_LONG)
					.show();

			Fragment defaultFragment = new DefaultFragment();

			replaceFragmentWithAnother(defaultFragment, args,
					"default_fragment");
		}

		catch (Exception exp) {
			String errorLoc = "In onSaveExpenseClicked() of ExpenseHostActivity";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onReportSelectedFromView(Bundle args) {
		// TODO Auto-generated method stub
		ViewExpenseFragment viewExpenseFragment = new ViewExpenseFragment();
		replaceFragmentWithAnother(viewExpenseFragment, args, "view_expense");
	}

	@Override
	public void onContextMenuItemSelected(Bundle args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIndividualExpenseClicked(Bundle args) {
		// TODO Auto-generated method stub
		Utility.out("View Individual expense button clicked..");
		ViewIndividualExpenseFragment individualExpenseFragment = new ViewIndividualExpenseFragment();
		individualExpenseFragment.setArguments(args);
		individualExpenseFragment.show(getSupportFragmentManager(),
				"individual_expense_dialog");
	}

	public boolean isReportAlreadyExists(Bundle args) throws Exception {
		// TODO Auto-generated method stub
		return model.isReportExistsAlready(args.getString("trip_name"),
				args.getString("start_date"));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		model.destroyObjects();
		model = null;
	}

	public ArrayList<Report> getReportDetails() throws Exception {
		// TODO Auto-generated method stub
		return model.getReportDetails();
	}

	public ArrayList<Person> getMembersOfSelectedReport(Report report)
			throws Exception {
		// TODO Auto-generated method stub
		return model.getMembersOfSelectedReport(report);
	}

	public SparseArray<Expense> populateExpenseDetailsForReport(Bundle args) {
		// TODO Auto-generated method stub
		return model.populateExpenseDetailsForReport(args);
	}

	public List<Expense> getMembersTotalExpense(Report report) {
		// TODO Auto-generated method stub
		return model.getMembersTotalExpense(report);
	}

	public List<String> getDistinctExpenseDatesFromTable(Report report) {
		// TODO Auto-generated method stub
		return model.getDistinctExpenseDatesFromTable(report);
	}

	public void onDeleteReportClicked(Bundle args) {
		// TODO Auto-generated method stub

		Resources res = this.getApplicationContext().getResources();
		try {
			Report report = (Report) args.getSerializable("report");
			boolean result = model.deleteSelectedReport(report);

			if (result) {
				Toast.makeText(this,
						res.getString(R.string.delete_report_success),
						Toast.LENGTH_LONG).show();
			}

			else {
				Toast.makeText(this,
						res.getString(R.string.delete_report_failure),
						Toast.LENGTH_LONG).show();
			}
			Fragment defaultFragment = new DefaultFragment();

			replaceFragmentWithAnother(defaultFragment, args,
					"default_fragment");
		} catch (Exception exp) {
			String errorLoc = "In onDeleteReportClicked() of ExpenseHostActivity";
			String customMsg = String.format("%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString());
			Utility.out(customMsg);
		}
	}

	public String getMembersExpenseForDate(Report report, String date) {
		// TODO Auto-generated method stub
		return model.getMembersExpenseForDate(report, date);
	}
}
