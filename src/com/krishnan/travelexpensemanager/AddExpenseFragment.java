package com.krishnan.travelexpensemanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.krishnan.travelexpensemanager.model.Expense;
import com.krishnan.travelexpensemanager.model.Person;
import com.krishnan.travelexpensemanager.model.Report;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Fragment to get expense details and saves into persistent storage
 *
 */
public class AddExpenseFragment extends Fragment implements OnClickListener,
		OnEditorActionListener, OnCheckedChangeListener, OnItemSelectedListener {

	private static SimpleDateFormat sdf;
	private static Calendar cal;
	private Bundle args_Bundle;
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
		if (this.getArguments() != null)
			args_Bundle = this.getArguments();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_add_expense, container,
				false);
		setCustomActionBarTitle();
		registerElements(view);
		return view;
	}

	private void setCustomActionBarTitle() {
		// TODO Auto-generated method stub
		Report report = (Report) args_Bundle.getSerializable("report");
		String title = String.format("%s-%s", report.getTrip_Name(),
				report.getStart_Date());
		hostActivity.setCustomActionBarTitle(title);
	}

	private void registerElements(View view) {
		// TODO Auto-generated method stub

		TextView report_Name = (TextView) view.findViewById(R.id.report_name);
		Report report = (Report) args_Bundle.getSerializable("report");
		report_Name.setText(report.getTrip_Name());

		expense_Name = (EditText) view.findViewById(R.id.expense_name);
		expense_Amount = (EditText) view.findViewById(R.id.expense_amount);
		expense_Comments = (EditText) view.findViewById(R.id.expense_comments);

		expense_Name.setOnEditorActionListener(this);
		expense_Amount.setOnEditorActionListener(this);
		expense_Comments.setOnEditorActionListener(this);

		expense_Date = (Button) view.findViewById(R.id.expense_date);
		expense_Time = (Button) view.findViewById(R.id.expense_time);

		expense_Date.setOnClickListener(this);
		expense_Time.setOnClickListener(this);

		expense_Type = (RadioGroup) view.findViewById(R.id.expense_by_group);
		expense_Type.setOnCheckedChangeListener(this);

		expense_Type_Action_Layout = (LinearLayout) view
				.findViewById(R.id.expense_type_action_layout);
		shared_Expense_Table = (TableLayout) view
				.findViewById(R.id.shared_expense_table);

		individual_Expense = (Spinner) view.findViewById(R.id.select_person);
		individual_Expense.setOnItemSelectedListener(this);

		save_Expense = (Button) view.findViewById(R.id.save_expense);
		save_Expense.setOnClickListener(this);
		save_Expense.setEnabled(false);
	}

	private Button expense_Date, expense_Time, save_Expense,
			initiate_Sharing_Expense;
	private EditText expense_Name, expense_Amount, expense_Comments,
			sharing_Count;
	private RadioGroup expense_Type = null;
	private LinearLayout expense_Type_Action_Layout;
	private TableLayout shared_Expense_Table;
	private Spinner individual_Expense;

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		try {
			if (v.getId() == R.id.expense_amount) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					Utility.out("Amount entered is: "
							+ expense_Amount.getText().toString());
					expense_Comments.requestFocus();
				}
			}
		}

		catch (Exception exp) {
			String errorLoc = "In onEditorAction() of AddExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Resources res = hostActivity.getApplicationContext().getResources();
		switch (v.getId()) {
		case R.id.expense_date:
			showPickerDialog("expense_date", R.id.expense_date);
			expense_Time.requestFocus();
			break;

		case R.id.expense_time:
			showPickerDialog("expense_time", R.id.expense_time);
			break;

		case R.id.save_expense:

			try {
				if (validateFieldValues()) {

					Report report = (Report) args_Bundle
							.getSerializable("report");

					String expense_Description = expense_Name.getText()
							.toString();
					String date = expense_Date.getText().toString();
					String time = expense_Time.getText().toString();

					Float amount = Float.valueOf(expense_Amount.getText()
							.toString());
					String type = hostActivity.onExpenseTypeChanged(
							expense_Type.getCheckedRadioButtonId()).trim();

					String comments = (expense_Comments != null && expense_Comments
							.getText().toString().trim().length() > 0) ? expense_Comments
							.getText().toString().trim()
							: "-";

					Expense expense = new Expense.ExpenseBuilder()
							.report(report).name(expense_Description)
							.date(date).time(time).amount(amount).type(type)
							.comments(comments).build();

					args_Bundle.putSerializable("expense", expense);

					hostActivity.onSaveExpenseClicked(args_Bundle);

				}
			}

			catch (Exception exp) {
				String errorLoc = "In onClick() of AddExpenseFragment class";
				String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
						exp.getLocalizedMessage(), exp.toString(),
						exp.getStackTrace());
				Utility.out(customMsg);
			}

			break;
		case 1001:
			Report report = (Report) args_Bundle.getSerializable("report");
			String sharing_Members_Count = sharing_Count.getText().toString();
			String expense_amount = expense_Amount.getText().toString();

			if (sharing_Members_Count != null
					&& sharing_Members_Count.trim().length() > 0) {
				if (Integer.valueOf(sharing_Members_Count) <= report
						.getPerson_Count()) {
					if (expense_amount != null
							&& expense_amount.trim().length() > 0) {

						if (Integer.valueOf(sharing_Members_Count) > 1) {

							args_Bundle.putInt("sharing_count",
									Integer.valueOf(sharing_Members_Count));
							args_Bundle.putFloat("expense_amount",
									Float.valueOf(expense_amount));

							args_Bundle.putInt("table_layout_id",
									shared_Expense_Table.getId());

							hostActivity.onSharedExpenseClicked(args_Bundle);

						} else {
							Toast.makeText(
									hostActivity,
									res.getString(R.string.sharing_count_less_than_one),
									Toast.LENGTH_LONG).show();
							RadioButton individual = (RadioButton) expense_Type
									.findViewById(R.id.individual);
							((RadioButton) expense_Type
									.findViewById(R.id.shared))
									.setEnabled(false);
							TextView tv = (TextView) expense_Type_Action_Layout
									.findViewById(R.id.expense_type_action_text);
							tv.setText(res.getString(R.string.text_select_name));

							View spinner_View = expense_Type_Action_Layout
									.findViewWithTag(res
											.getString(R.string.individual_spinner_tag));

							View sharing_Count_View = expense_Type_Action_Layout
									.findViewWithTag(res
											.getString(R.string.sharing_count_tag));
							View initiate_Sharing_Action_View = expense_Type_Action_Layout
									.findViewWithTag(res
											.getString(R.string.initiate_sharing_button_tag));

							if (sharing_Count_View != null
									&& initiate_Sharing_Action_View != null) {
								expense_Type_Action_Layout
										.removeView(sharing_Count_View);
								expense_Type_Action_Layout
										.removeView(initiate_Sharing_Action_View);
							}

							if (spinner_View == null) {
								expense_Type_Action_Layout.addView(
										individual_Expense, 1);
							}
							individual.setChecked(true);
						}
					} else {
						Toast.makeText(hostActivity,
								res.getString(R.string.expense_amount_empty),
								Toast.LENGTH_LONG).show();
						expense_Amount.requestFocus();
					}

				} else {
					Toast.makeText(hostActivity,
							res.getString(R.string.sharing_count_error),
							Toast.LENGTH_LONG).show();
					sharing_Count.requestFocus();
				}
			}

			else {
				Toast.makeText(hostActivity,
						res.getString(R.string.sharing_count_empty),
						Toast.LENGTH_SHORT).show();
				sharing_Count.requestFocus();
			}

			break;
		}
	}

	public void showPickerDialog(String tagName, int sourceId) {
		DialogFragment start_Fragment = new PickerDialogFragment();
		Bundle args = new Bundle();
		args.putInt(tagName, sourceId);
		hostActivity.setBundle(args);
		start_Fragment.show(getActivity().getSupportFragmentManager(), tagName);
	}

	public static class PickerDialogFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener, OnTimeSetListener {

		static String date_String = null;
		static String time_String = null;
		Bundle myBundle;
		ExpenseHostActivity hostActivity;

		public void onCreate(Bundle savedInstance) {
			super.onCreate(savedInstance);
			hostActivity = (ExpenseHostActivity) getActivity();
			myBundle = hostActivity.getBundle();
		}

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();

			if ((myBundle.getInt("expense_date") != 0)) {
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				return new DatePickerDialog(getActivity(), this, year, month,
						day);
			} else {
				int hourOfDay = c.get(Calendar.HOUR);
				int minute = c.get(Calendar.MINUTE);
				return new TimePickerDialog(getActivity(), this, hourOfDay,
						minute, false); // Create a new instance of
										// TimePickerDialog and return it
			}
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			// TODO Auto-generated method stub

			date_String = sdf.format(new GregorianCalendar(year, month, day)
					.getTime());
			Utility.out(date_String);
			hostActivity.setButtonData(myBundle.getInt("expense_date"),
					date_String);
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			String meredian = ((hourOfDay < 12) && (minute <= 59)) ? "AM"
					: "PM";
			int HourFormat12 = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
			time_String = String.format("%s:%s %s", pad(HourFormat12),
					pad(minute), meredian);
			Utility.out(time_String);
			hostActivity.setButtonData(myBundle.getInt("expense_time"),
					time_String);
		}

		private static String pad(int c) {
			if (c >= 10)
				return String.valueOf(c);
			else
				return "0" + String.valueOf(c);
		}
	}

	static {
		final String datePattern = "yyyy-MM-dd";
		cal = Calendar.getInstance();
		sdf = new SimpleDateFormat(datePattern, Locale.ENGLISH);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		Resources res = hostActivity.getApplicationContext().getResources();
		String radio_Text = hostActivity.onExpenseTypeChanged(checkedId).trim();

		if (radio_Text.equalsIgnoreCase(res.getString(R.string.individual_text)
				.trim())) {
			RadioButton individual = (RadioButton) expense_Type
					.findViewById(R.id.individual);
			individual.setChecked(true);
			RadioButton other_Radio_Button = (RadioButton) expense_Type
					.findViewById(R.id.shared);
			other_Radio_Button.setEnabled(false);
			String expense_amount = expense_Amount.getText().toString();

			if (expense_amount != null && expense_amount.trim().length() > 0) {
				ArrayList<Person> members_List = getMembersOfSelectedReport();
				ArrayAdapter<Person> person_Adapter = new ArrayAdapter<Person>(
						hostActivity, R.layout.spinner_row, members_List);
				individual_Expense.setAdapter(person_Adapter);
				expense_Type_Action_Layout.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(hostActivity,
						res.getString(R.string.expense_amount_empty),
						Toast.LENGTH_LONG).show();
				expense_Amount.requestFocus();
				individual.setChecked(false);
			}

		}

		else if (radio_Text.equalsIgnoreCase(res
				.getString(R.string.shared_text).trim())) {

			RadioButton other_Radio_Button = (RadioButton) expense_Type
					.findViewById(R.id.individual);
			other_Radio_Button.setEnabled(false);
			View spinner_View = expense_Type_Action_Layout.findViewWithTag(res
					.getString(R.string.individual_spinner_tag));

			if (spinner_View != null)
				expense_Type_Action_Layout.removeView(spinner_View);

			TextView tv = (TextView) expense_Type_Action_Layout
					.findViewById(R.id.expense_type_action_text);
			tv.setText(res.getString(R.string.sharing_number_count_text));

			sharing_Count = new EditText(hostActivity);
			LinearLayout.LayoutParams editText_Params = new LinearLayout.LayoutParams(
					0, LayoutParams.WRAP_CONTENT, 0.5f);
			sharing_Count.setLayoutParams(editText_Params);
			sharing_Count.setKeyListener(new DigitsKeyListener(false, false));
			sharing_Count.setGravity(Gravity.CENTER_HORIZONTAL);
			sharing_Count.setHint(res
					.getString(R.string.no_of_persons_text_hint));
			sharing_Count.setTextColor(Color.parseColor("#000000"));
			sharing_Count.setTag(res.getString(R.string.sharing_count_tag));
			sharing_Count.setLines(1);

			InputFilter[] filter = new InputFilter[1];
			filter[0] = new InputFilter.LengthFilter(2);
			sharing_Count.setFilters(filter);

			expense_Type_Action_Layout.addView(sharing_Count, 1);

			initiate_Sharing_Expense = new Button(hostActivity);
			LinearLayout.LayoutParams button_Params = new LinearLayout.LayoutParams(
					0, LayoutParams.WRAP_CONTENT, 0.2f);
			initiate_Sharing_Expense.setLayoutParams(button_Params);
			int button_id = Integer.valueOf(res
					.getString(R.string.initiate_sharing_button_id));
			initiate_Sharing_Expense.setId(button_id);
			initiate_Sharing_Expense.setText(res
					.getString(R.string.initiate_sharing_button_text));
			initiate_Sharing_Expense.setTextColor(Color.parseColor("#000000"));
			initiate_Sharing_Expense.setTag(res
					.getString(R.string.initiate_sharing_button_tag));
			initiate_Sharing_Expense.setOnClickListener(this);
			expense_Type_Action_Layout.addView(initiate_Sharing_Expense, 2);
			expense_Type_Action_Layout.setVisibility(View.VISIBLE);
		}

	}

	private ArrayList<Person> getMembersOfSelectedReport() {
		// TODO Auto-generated method stub
		ArrayList<Person> person_List = null;

		try {
			Report report = (Report) args_Bundle.getSerializable("report");
			person_List = hostActivity.getMembersOfSelectedReport(report);
		}

		catch (Exception exp) {
			String errorLoc = "In getMembersOfSelectedReport() of AddExpenseFragment class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

		return person_List;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Utility.out("Selected position:-> " + position);
		Person p = (Person) individual_Expense.getItemAtPosition(position);
		Utility.out("Person name : --> " + p);
		createExpenseTableRow(p);
	}

	private void createExpenseTableRow(Person p) {
		// TODO Auto-generated method stub
		try {

			Resources res = hostActivity.getApplicationContext().getResources();

			shared_Expense_Table.removeAllViews();

			TableRow header_row = new TableRow(hostActivity);

			header_row.setGravity(Gravity.CENTER);

			TableRow.LayoutParams header_Params = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			header_Params.setMargins(2, 3, 2, 3);

			TextView headerCol1 = new TextView(hostActivity);

			headerCol1.setLayoutParams(header_Params);

			headerCol1.setText(res.getString(R.string.column_rownum_text));

			headerCol1.setTextAppearance(hostActivity,
					R.id.add_expense_table_column_style);

			headerCol1.setTextColor(Color.parseColor("#0000FF"));

			headerCol1.setGravity(Gravity.CENTER);

			TextView headerCol2 = new TextView(hostActivity);

			headerCol2.setLayoutParams(header_Params);

			headerCol2.setText(res.getString(R.string.column_person_name_text));

			headerCol2.setTextAppearance(hostActivity,
					R.id.add_expense_table_column_style);

			headerCol2.setTextColor(Color.parseColor("#0000FF"));

			headerCol2.setGravity(Gravity.CENTER);

			TextView headerCol3 = new TextView(hostActivity);

			headerCol3.setLayoutParams(header_Params);

			headerCol3.setText(res.getString(R.string.column_amount_text));

			headerCol3.setTextAppearance(hostActivity,
					R.id.add_expense_table_column_style);

			headerCol3.setTextColor(Color.parseColor("#0000FF"));

			headerCol3.setGravity(Gravity.CENTER);

			header_row.addView(headerCol1, 0);
			header_row.addView(headerCol2, 1);
			header_row.addView(headerCol3, 2);

			shared_Expense_Table.addView(header_row, 0);

			TableRow separator_Header_Row = new TableRow(hostActivity);

			TableRow.LayoutParams separator_Header_Row_Params = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, 2);

			separator_Header_Row.setLayoutParams(separator_Header_Row_Params);

			separator_Header_Row
					.setBackgroundColor(Color.parseColor("#FFFF00"));

			TextView header_separator_Line = new TextView(hostActivity);

			header_separator_Line.setLayoutParams(separator_Header_Row_Params);

			separator_Header_Row.addView(header_separator_Line, 0);

			shared_Expense_Table.addView(separator_Header_Row, 1);

			TableRow row = new TableRow(hostActivity);

			TableRow.LayoutParams row_Params = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			row.setLayoutParams(row_Params);

			row.setTag("dynamic_row");

			row.setBackgroundColor(Color.parseColor("#77CCDD"));

			row.setGravity(Gravity.CENTER);

			TextView si_No = new TextView(hostActivity);

			si_No.setLayoutParams(row_Params);

			si_No.setText("1");

			si_No.setBackgroundColor(Color.parseColor("#0000FF"));

			si_No.setGravity(Gravity.CENTER);

			TextView person_Name = new TextView(hostActivity);

			person_Name.setLayoutParams(row_Params);

			person_Name.setText(p.getPersonName());

			person_Name.setBackgroundColor(Color.parseColor("#0000FF"));

			person_Name.setGravity(Gravity.CENTER);

			TextView amount = new TextView(hostActivity);

			amount.setLayoutParams(row_Params);

			amount.setText(expense_Amount.getText().toString());

			amount.setBackgroundColor(Color.parseColor("#0000FF"));

			amount.setGravity(Gravity.CENTER);
			// #0000FF
			row.addView(si_No, 0);
			row.addView(person_Name, 1);
			row.addView(amount, 2);

			if (shared_Expense_Table.findViewWithTag("dynamic_row") != null) {
				View remove_View = shared_Expense_Table
						.findViewWithTag("dynamic_row");
				shared_Expense_Table.removeView(remove_View);
			}

			shared_Expense_Table.addView(row, 2);
			shared_Expense_Table.setVisibility(View.VISIBLE);

			Float individual_amount = Float.valueOf(expense_Amount.getText()
					.toString());

			Expense individual_Expense = new Expense.ExpenseBuilder().person(p)
					.amount(individual_amount).type("individual").build();

			args_Bundle.putSerializable("individual", individual_Expense);

			save_Expense.setEnabled(true);
		}

		catch (Exception exp) {
			String errorLoc = "In createExpenseTableRow() of AddExpenseFragment";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	interface onSharedExpenseActionListener {
		public void onSharedExpenseClicked(Bundle args);
	}

	interface onSaveExpenseActionListener {
		public void onSaveExpenseClicked(Bundle args);
	}

	public void afterSharedExpenseDialogDismissed(Bundle args) {

		Utility.out("Back to add expense");
		save_Expense.setEnabled(true);
	}

	public boolean validateFieldValues() throws ParseException {
		Resources res = hostActivity.getApplicationContext().getResources();

		boolean returnValue = true;

		if (!(expense_Name != null && expense_Name.getText().toString().trim()
				.length() > 0)) {
			returnValue = false;
			Toast.makeText(getActivity(),
					res.getString(R.string.expense_description_empty),
					Toast.LENGTH_LONG).show();
			expense_Name.requestFocus();
		}

		if (expense_Date != null
				&& expense_Date
						.getText()
						.toString()
						.equalsIgnoreCase(
								res.getString(R.string.date_button_text))) {
			returnValue = false;
			Toast.makeText(getActivity(),
					res.getString(R.string.expense_date_empty),
					Toast.LENGTH_LONG).show();
		}

		else {
			Report report = (Report) args_Bundle.getSerializable("report");
			Date expenseDate = sdf.parse(expense_Date.getText().toString());
			Date reportStartDate = sdf.parse(report.getStart_Date());
			Date today = cal.getTime();

			if (expenseDate.before(reportStartDate)) {
				returnValue = false;
				Toast.makeText(getActivity(),
						res.getString(R.string.expense_date_before_start_date),
						Toast.LENGTH_SHORT).show();
			}

			if (!report.getEnd_Date().equalsIgnoreCase("-")) {
				Date reportEndDate = sdf.parse(report.getEnd_Date());

				if (expenseDate.after(reportEndDate)) {
					returnValue = false;
					Toast.makeText(
							getActivity(),
							res.getString(R.string.expense_date_after_end_date),
							Toast.LENGTH_SHORT).show();
				}
			}

			if (expenseDate.after(today)) {
				Toast.makeText(getActivity(),
						res.getString(R.string.expense_after_today),
						Toast.LENGTH_SHORT).show();
				returnValue = false;
			}
		}

		if (expense_Time != null
				&& expense_Time
						.getText()
						.toString()
						.equalsIgnoreCase(
								res.getString(R.string.time_button_text))) {
			returnValue = false;
			Toast.makeText(getActivity(),
					res.getString(R.string.expense_time_empty),
					Toast.LENGTH_LONG).show();
		}

		if (!(expense_Amount != null && expense_Amount.getText().toString()
				.trim().length() > 0)) {
			returnValue = false;
			Toast.makeText(getActivity(),
					res.getString(R.string.expense_amount_empty),
					Toast.LENGTH_LONG).show();
			expense_Amount.requestFocus();
		}

		return returnValue;
	}
}
