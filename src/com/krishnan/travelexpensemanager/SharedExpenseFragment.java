package com.krishnan.travelexpensemanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.krishnan.travelexpensemanager.model.CustomSharedExpenseAdapter;
import com.krishnan.travelexpensemanager.model.Expense;
import com.krishnan.travelexpensemanager.model.Expense.ExpenseBuilder;
import com.krishnan.travelexpensemanager.model.Person;
import com.krishnan.travelexpensemanager.model.Report;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Dialog Fragment to get the person & amount details of a shared expense 
 *
 */
public class SharedExpenseFragment extends DialogFragment implements
		OnClickListener, OnDismissListener {

	@Override
	public void setCancelable(boolean cancelable) {
		// TODO Auto-generated method stub
		super.setCancelable(false);
	}

	@Override
	public boolean isCancelable() {
		// TODO Auto-generated method stub
		return false;
	}

	private ArrayList<Person> getMembersOfSelectedTrip() throws Exception {
		// TODO Auto-generated method stub

		ArrayList<Person> person_List = null;
		try {
			Report report = (Report) args_Bundle.getSerializable("report");
			person_List = hostActivity.getMembersOfSelectedReport(report);
		}

		catch (Exception exp) {
			throw new Exception(exp.toString());
		}
		return person_List;
	}

	private Bundle args_Bundle;
	private ExpenseHostActivity hostActivity;
	private Button submitAction;
	private Dialog customDialog = null;
	private Map<Person, Float> shared_Expense_Map;
	private CustomSharedExpenseAdapter mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		hostActivity.setCustomActionBarTitle("Add Shared Expense");
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
		View view = inflater.inflate(R.layout.fragment_shared_expense,
				container, false);
		try {

			Resources res = hostActivity.getApplicationContext().getResources();

			this.setCancelable(false);

			customDialog = this.getDialog();
			customDialog.requestWindowFeature(STYLE_NORMAL);
			customDialog.setContentView(R.layout.fragment_shared_expense);
			customDialog.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			customDialog.setTitle(res
					.getString(R.string.shared_expense_dialog_title));

			customDialog.setOnDismissListener(this);

			int sharing_Count = args_Bundle.getInt("sharing_count");
			List<Integer> countList = new ArrayList<Integer>(sharing_Count);
			shared_Expense_Map = new HashMap<Person, Float>(sharing_Count);

			for (int i = 0; i < sharing_Count; i++)
				countList.add(i);

			Utility.out("Size of count List " + countList.size());
			List<Person> person_List = getMembersOfSelectedTrip();

			mAdapter = new CustomSharedExpenseAdapter(hostActivity, countList,
					person_List);

			TextView expense_Amount = (TextView) view
					.findViewById(R.id.dialog_expense_amount);
			expense_Amount.setText(String.valueOf(args_Bundle
					.getFloat("expense_amount")));
			ListView myListView = (ListView) view
					.findViewById(R.id.shared_expense_list);
			myListView.setItemsCanFocus(true);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 0, countList.size());
			myListView.setLayoutParams(params);
			myListView.setAdapter(mAdapter);

			submitAction = (Button) view
					.findViewById(R.id.shared_expense_action);
			submitAction.setOnClickListener(this);
			customDialog.show();

		} catch (Exception exp) {
			String errorLoc = "In onCreateView() of SharedExpenseFragment";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.shared_expense_action:

			Resources res = hostActivity.getApplicationContext().getResources();

			int initial_Size = mAdapter.getCount();

			shared_Expense_Map.clear();

			Utility.out("Total rows in the List view is " + initial_Size);

			for (int i = 0; i < initial_Size; i++) {

				if (mAdapter.getPersonAtPosition(i) != null
						&& mAdapter.getExpenseAmountAtPosition(i) != null) {

					shared_Expense_Map.put(mAdapter.getPersonAtPosition(i),
							mAdapter.getExpenseAmountAtPosition(i));

				}

				else {
					continue;
				}
			}

			Utility.out("Values in the final map" + shared_Expense_Map);

			if (shared_Expense_Map.size() == initial_Size) {

				Float total_Expense_Amount = args_Bundle
						.getFloat("expense_amount");

				Float cumulative_Individual_Expenses = 0f;

				for (Person p : shared_Expense_Map.keySet())
					cumulative_Individual_Expenses += shared_Expense_Map.get(p);

				Utility.out("Cumulative total :"
						+ cumulative_Individual_Expenses);

				if (total_Expense_Amount
						.compareTo(cumulative_Individual_Expenses) == 0) {
					customDialog.dismiss();
				}

				else {

					Utility.out("Compare: "
							+ Float.compare(total_Expense_Amount,
									cumulative_Individual_Expenses));
					Utility.out(total_Expense_Amount
							.compareTo(cumulative_Individual_Expenses));

					Toast.makeText(
							hostActivity,
							String.format(
									res.getString(R.string.total_expense_not_match_with_cumulative_individual),
									(total_Expense_Amount - cumulative_Individual_Expenses)),
							Toast.LENGTH_SHORT).show();
				}
			}

			else {
				if (mAdapter.getSelectedPersonCount() < initial_Size) {
					Toast.makeText(
							hostActivity,
							String.format(
									res.getString(R.string.duplicate_person_count_format),
									(initial_Size - mAdapter
											.getSelectedPersonCount())),
							Toast.LENGTH_SHORT).show();
				}

				if (mAdapter.getExpenseAmountCount() < initial_Size) {
					Toast.makeText(
							hostActivity,
							String.format(
									res.getString(R.string.missing_expense_amount_count),
									(initial_Size - shared_Expense_Map.size())),
							Toast.LENGTH_SHORT).show();
				}
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub

		Toast.makeText(hostActivity, "Shared Expense dialog is dismissed",
				Toast.LENGTH_SHORT).show();

		List<Expense> shared_Expense_List = new ArrayList<Expense>(
				shared_Expense_Map.size());

		shared_Expense_List.clear();

		try {
			for (Person p : shared_Expense_Map.keySet()) {
				Expense expense = new ExpenseBuilder().person(p)
						.amount(shared_Expense_Map.get(p)).type("shared")
						.build();
				shared_Expense_List.add(expense);
			}

			args_Bundle.putSerializable("shared_expense_list",
					(Serializable) shared_Expense_List);

			hostActivity.onSharedExpenseDialogDismissed(args_Bundle);
		}

		catch (Exception exp) {
			String errorLoc = "In onDismiss() of SharedExpenseFragment";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

	}

}
