package com.krishnan.travelexpensemanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.krishnan.travelexpensemanager.model.CustomPersonListAdapter;
import com.krishnan.travelexpensemanager.model.Person;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Fragment to get person list related to a trip / report
 *
 */
public class AddPersonNamesList extends Fragment implements OnClickListener {

	ExpenseHostActivity hostActivity = null;

	Button add_expense, reset;

	Resources res;
	Bundle args_Bundle;
	CustomPersonListAdapter mAdapter;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		hostActivity = (ExpenseHostActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		List<Person> data = showPersonNames();
		// mAdapter = new ArrayAdapter<LinearLayout>(getActivity(),
		// android.R.layout.simple_list_item_1, data);
		View view = inflater.inflate(R.layout.fragment_person_names, container,
				false);
		ListView myView = (ListView) view.findViewById(R.id.person_list);
		myView.setItemsCanFocus(true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, data.size());
		myView.setLayoutParams(params);
		mAdapter = new CustomPersonListAdapter(
				(ExpenseHostActivity) getActivity(), data);
		myView.setAdapter(mAdapter);
		initializeElements(view);
		return view;
	}

	private void initializeElements(View view) {
		// TODO Auto-generated method stub
		try {
			add_expense = (Button) view.findViewById(R.id.add_expense);
			// reset = (Button) view.findViewById(R.id.reset);
			add_expense.setOnClickListener(this);
			// reset.setOnClickListener(this);
		}

		catch (Exception exp) {
			String errorLoc = "In initializeElements() of AddPersonNamesList class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
			Utility.out(exp);
		}
	}

	private List<Person> showPersonNames() {
		// TODO Auto-generated method stub

		// Random rand = new Random();
		try {
			List<Person> persons = new ArrayList<Person>();

			if (hostActivity != null) {

				int no_Of_Persons = args_Bundle.getInt("person_count");
				res = hostActivity.getApplicationContext().getResources();

				persons.clear();

				for (int i = 1; i <= no_Of_Persons; i++) {
					// String name_hint = String.format(
					// res.getString(R.string.edittext_format), i);
					persons.add(null);

				}
			}

			return persons;
		} catch (Exception exp) {
			String errorLoc = "In showPersonNames() of" + exp.getClass();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}

		return null;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		hostActivity.setCustomActionBarTitle("Add Persons");
		res = hostActivity.getApplicationContext().getResources();

		if (!(hostActivity instanceof onAddExpenseClickListener)) {
			throw new ClassCastException(ExpenseHostActivity.class
					+ "should implement " + onAddExpenseClickListener.class);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub

		List<Person> final_Person_List;

		switch (v.getId()) {
		case R.id.add_expense:
			final_Person_List = new ArrayList<Person>();
			int initial_Size = mAdapter.getCount();
			final_Person_List.clear();
			for (int i = 0; i < initial_Size; i++) {
				if (mAdapter.getItem(i) != null) {
					if (!(mAdapter.getItem(i).getPersonName()
							.equalsIgnoreCase(String.format(
									res.getString(R.string.edittext_format),
									i + 1)))) {
						final_Person_List.add(mAdapter.getItem(i));
					}
				}

				else {
					continue;
				}
			}

			if (final_Person_List.size() == initial_Size) {
				// Enter code to proceed further

				args_Bundle.putSerializable("person_list",
						(Serializable) final_Person_List);
				hostActivity.onAddExpenseClicked(args_Bundle);
			}

			else {
				Toast.makeText(getActivity(),
						res.getString(R.string.person_name_empty),
						Toast.LENGTH_SHORT).show();
				Toast.makeText(
						getActivity(),
						String.format(
								res.getString(R.string.missing_person_count),
								(initial_Size - final_Person_List.size())),
						Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.reset:
			// mAdapter.resetValues();
			break;
		}
	}

	public interface onAddExpenseClickListener {
		public void onAddExpenseClicked(Bundle args);
	}

}
