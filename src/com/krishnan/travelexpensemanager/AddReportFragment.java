package com.krishnan.travelexpensemanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Fragment to key in trip details / report details
 *
 */
public class AddReportFragment extends Fragment implements
		View.OnClickListener, OnEditorActionListener {

	EditText trip_Name = null;
	Button start_Date = null;
	Button end_Date = null;
	EditText no_Of_Shares = null;
	Button next, reset_Report;

	static SimpleDateFormat sdf = null;
	ExpenseHostActivity hostActivity;
	private boolean isSameReport = false;

	Resources res;

	int no_Of_Persons;
	private Bundle args_Bundle;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
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
		View view = inflater.inflate(R.layout.fragment_add_report, container,
				false);
		hostActivity.setCustomActionBarTitle("Add Report");
		registerElements(view);
		return view;
	}

	private void registerElements(View view) {
		// TODO Auto-generated method stub
		try {
			trip_Name = (EditText) view.findViewById(R.id.trip_name);
			start_Date = (Button) view.findViewById(R.id.start_date);
			end_Date = (Button) view.findViewById(R.id.end_date);
			no_Of_Shares = (EditText) view.findViewById(R.id.no_of_persons);
			next = (Button) view.findViewById(R.id.add_person_next);
			reset_Report = (Button) view.findViewById(R.id.reset_report);

			trip_Name.setOnClickListener(this);
			trip_Name.setOnEditorActionListener(this);
			start_Date.setOnClickListener(this);
			end_Date.setOnClickListener(this);
			no_Of_Shares.setOnClickListener(this);
			no_Of_Shares.setOnEditorActionListener(this);
			next.setOnClickListener(this);
			reset_Report.setOnClickListener(this);
			// no_Of_Shares.addTextChangedListener(person_count_watcher);

		} catch (Exception exp) {
			String errorLoc = "In registerElements() of" + exp.getClass();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		res = hostActivity.getApplicationContext().getResources();

		try {

			switch (v.getId()) {
			case R.id.start_date:
				showDatePickerDialog("start_picker", R.id.start_date);
				start_Date.setEnabled(false);
				end_Date.requestFocus();
				break;

			case R.id.end_date:
				showDatePickerDialog("end_picker", R.id.end_date);
				end_Date.setEnabled(false);
				no_Of_Shares.requestFocus();
				break;

			case R.id.no_of_persons:
				no_Of_Shares.setImeActionLabel("Proceed",
						KeyEvent.KEYCODE_ENTER);
				break;

			case R.id.add_person_next:
				if (validateInputs()) {
					String tripName = trip_Name.getText().toString();
					isSameReport = (isSameReport) ? true : false;
					if (isSameReport
							&& args_Bundle.getString("start_date") != null) {
						isSameReport = (start_Date.getText().toString()
								.equalsIgnoreCase(args_Bundle
										.getString("start_date"))) ? true
								: false;
					}

					isSameReport = (isSameReport) ? true : false;
					if (isSameReport
							&& args_Bundle.getString("trip_name") != null) {
						isSameReport = (tripName.equalsIgnoreCase(args_Bundle
								.getString("trip_name"))) ? true : false;
					}

					if (!isSameReport) {

						args_Bundle.putString("trip_name", trip_Name.getText()
								.toString().trim());
						args_Bundle.putString("start_date", start_Date
								.getText().toString());

						no_Of_Persons = Integer.valueOf(no_Of_Shares.getText()
								.toString().trim());

						args_Bundle.putInt("person_count", no_Of_Persons);

						boolean isDuplicateReport = hostActivity
								.isReportAlreadyExists(args_Bundle);

						if (!isDuplicateReport) {
							isSameReport = false;
							hostActivity.showPersonFragment(args_Bundle);
						}

						else {
							// duplicate_report
							Toast.makeText(getActivity(),
									res.getString(R.string.duplicate_report),
									Toast.LENGTH_LONG).show();
							isSameReport = true;
						}
					} else {
						Toast.makeText(getActivity(),
								res.getString(R.string.duplicate_report),
								Toast.LENGTH_LONG).show();
					}
				}

				else {
					Toast.makeText(getActivity(),
							res.getString(R.string.add_report_error),
							Toast.LENGTH_LONG).show();
				}
				break;

			case R.id.reset_report:
				trip_Name.setText("");
				start_Date.setEnabled(true);
				start_Date.setText(res.getString(R.string.date_button_text));
				if (!end_Date.isEnabled())
					end_Date.setEnabled(true);
				end_Date.setText(res.getString(R.string.date_button_text));
				no_Of_Shares.setText("");
				no_Of_Shares.setEnabled(true);
				break;

			default:
				break;
			}
		}

		catch (Exception exp) {
			String errorLoc = "In onClick() of AddReportFragment class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
	}

	private boolean validateInputs() throws java.text.ParseException {
		// TODO Auto-generated method stub
		boolean returnValue = true;

		if (trip_Name != null && trip_Name.getText().toString().length() == 0) {
			Toast.makeText(getActivity(),
					res.getString(R.string.trip_name_empty), Toast.LENGTH_SHORT)
					.show();
			returnValue = false;
			trip_Name.setEnabled(true);
		}

		if (start_Date != null
				&& start_Date
						.getText()
						.toString()
						.equalsIgnoreCase(
								res.getString(R.string.date_button_text))) {
			Toast.makeText(getActivity(),
					res.getString(R.string.start_date_empty),
					Toast.LENGTH_SHORT).show();
			returnValue = false;
			start_Date.setEnabled(true);
		}

		else {
			if (end_Date != null
					&& !(end_Date.getText().toString().equalsIgnoreCase(res
							.getString(R.string.date_button_text)))) {
				Date beginDate = sdf.parse(start_Date.getText().toString());
				Date finishDate = sdf.parse(end_Date.getText().toString());

				if (beginDate.after(finishDate)) {
					returnValue = false;
					Toast.makeText(getActivity(),
							res.getString(R.string.end_date_before_start_date),
							Toast.LENGTH_SHORT).show();
					end_Date.setEnabled(true);
				}

				else {
					args_Bundle.putString("end_date", end_Date.getText()
							.toString());
				}
			}
		}

		String person_text = no_Of_Shares.getText().toString();

		if (no_Of_Shares != null && !(person_text.trim().length() == 0)) {
			if (Integer.valueOf(person_text) <= 0) {
				Toast.makeText(getActivity(),
						res.getString(R.string.person_count_error),
						Toast.LENGTH_SHORT).show();
				returnValue = false;
				no_Of_Shares.setEnabled(true);
				no_Of_Shares.requestFocus();
			}
		} else {
			Toast.makeText(getActivity(),
					res.getString(R.string.person_count_empty),
					Toast.LENGTH_SHORT).show();
			returnValue = false;
			no_Of_Shares.setEnabled(true);
			no_Of_Shares.requestFocus();
		}

		return returnValue;
	}

	@Override
	public boolean onEditorAction(TextView tv, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub

		try {
			if (tv.getId() == R.id.trip_name) {

				Utility.out("Event occurred in trip name: " + "-" + actionId);

				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					Utility.out("Next key pressed " + actionId);
					String tripName = trip_Name.getText().toString();

					if (args_Bundle.getString("trip_name") != null) {
						isSameReport = (tripName.equalsIgnoreCase(args_Bundle
								.getString("trip_name"))) ? true : false;
					}

					Utility.out("Trip Name value " + tripName);
					trip_Name.setEnabled(false);
					trip_Name.clearFocus();
					start_Date.requestFocus();
				}
			}

			else if (tv.getId() == R.id.no_of_persons) {

				if (actionId == EditorInfo.IME_ACTION_DONE) {
					no_Of_Shares.setEnabled(false);
					no_Of_Shares.clearFocus();
				}
			}
		}

		catch (Exception exp) {
			String errorLoc = "In onEditorAction() of" + exp.getClass();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

		return true;
	}

	public void showDatePickerDialog(String tagName, int sourceId) {
		DialogFragment start_Fragment = new DatePickerDialogFragment();
		Bundle args = new Bundle();
		args.putInt(tagName, sourceId);
		hostActivity.setBundle(args);
		start_Fragment.show(getActivity().getSupportFragmentManager(), tagName);

	}

	public static class DatePickerDialogFragment extends DialogFragment
			implements DatePickerDialog.OnDateSetListener {

		static String date_String = null;
		Bundle myBundle;
		ExpenseHostActivity myActivity;

		public void onCreate(Bundle savedInstance) {
			super.onCreate(savedInstance);
			myActivity = (ExpenseHostActivity) getActivity();
			myBundle = myActivity.getBundle();
		}

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			// TODO Auto-generated method stub
			date_String = sdf.format(new GregorianCalendar(year, month, day)
					.getTime());
			Utility.out(date_String);

			if (myBundle.getInt("start_picker") != 0) {
				myActivity.setButtonData(myBundle.getInt("start_picker"),
						date_String);

			}

			else if (myBundle.getInt("end_picker") != 0) {

				myActivity.setButtonData(myBundle.getInt("end_picker"),
						date_String);
			}
		}
	}

	public interface onNextClickListener {
		public void showPersonFragment(Bundle args);
	}

	static {
		final String datePattern = "yyyy-MM-dd";
		sdf = new SimpleDateFormat(datePattern, Locale.ENGLISH);
	}

}
