package com.krishnan.travelexpensemanager.model;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.krishnan.travelexpensemanager.R;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Custom adapter to get shared expense details from the members
 *
 */
@SuppressWarnings("rawtypes")
public class CustomSharedExpenseAdapter extends ArrayAdapter implements
		OnItemSelectedListener {

	private Activity mContext;
	private List<Person> mList;
	private ArrayAdapter<Person> spinnerAdapter;
	private int shared_Count;
	private List<Person> selectedPersonList;
	// private Set<Person> selectedPersonList;
	private SparseArray<Float> amount_List_Map;

	@SuppressWarnings("unchecked")
	public CustomSharedExpenseAdapter(Activity context, List<Integer> list,
			List<Person> personList) {
		super(context, R.layout.shared_expense_row, list);
		this.mContext = context;
		this.mList = personList;
		this.shared_Count = list.size();
		spinnerAdapter = new ArrayAdapter<Person>(mContext,
				R.layout.spinner_row, mList);
		selectedPersonList = new ArrayList<Person>(shared_Count);
		// selectedPersonList = new HashSet<Person>(shared_Count);
		amount_List_Map = new SparseArray<Float>(shared_Count);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shared_Count;
	}

	@Override
	public Person getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	public Person getPersonAtPosition(int index) {
		return (selectedPersonList.size() > index) ? selectedPersonList
				.get(index) : null;

	}

	public int getSelectedPersonCount() {

		int count = 0;
		for (Person p : selectedPersonList) {
			count = (p != null) ? count + 1 : count;
		}
		return count;

	}

	public int getExpenseAmountCount() {
		return amount_List_Map.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SharedViewHolder holder = null;
		Resources res = null;

		try {
			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				res = mContext.getApplicationContext().getResources();

				convertView = inflater.inflate(R.layout.shared_expense_row,
						parent, false);
				holder = new SharedViewHolder();

				holder.person_Name = (Spinner) convertView
						.findViewById(R.id.person_name);
				holder.person_Amount = (EditText) convertView
						.findViewById(R.id.shared_expense_amount);

				convertView.setTag(holder);
				convertView.setTag(R.id.person_name, position);
				convertView.setTag(R.id.shared_expense_amount, position);
			}

			else {
				res = mContext.getApplicationContext().getResources();
				holder = (SharedViewHolder) convertView.getTag();
			}

			holder.person_Name.setTag(position);
			holder.person_Amount.setTag(position);

			final EditText et = holder.person_Amount;

			holder.person_Name.setPrompt(String.format(
					res.getString(R.string.spinner_prompt), position + 1));
			holder.person_Name.setAdapter(spinnerAdapter);
			holder.person_Name.setSelection(position % mList.size());

			/*
			 * if (selectedPersonList.size() < shared_Count)
			 * selectedPersonList.add(null);
			 */

			holder.person_Name.setOnItemSelectedListener(this);
			holder.person_Amount.setHint(String.format(
					res.getString(R.string.shared_expense_amount_hint_text),
					position + 1));

			holder.person_Amount.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if (s != null && s.toString().trim().length() > 0) {
						/*
						 * int position = (Integer) currentView
						 * .getTag(R.id.shared_expense_amount);
						 */

						int position = (Integer) et.getTag();

						amount_List_Map.put(position,
								Float.valueOf(s.toString()));

						/*
						 * Utility.out("Amount list value @" + position + " = "
						 * + amount_List_Map.valueAt(position));
						 */
					}
				}
			});

			if (position < amount_List_Map.size()) {
				String amount_Text = amount_List_Map.get(position).toString();
				holder.person_Amount.setText(amount_Text);
			} else {
				holder.person_Amount.setText("");
			}

		} catch (Exception exp) {
			String errorLoc = "In getView() of CustomSharedExpenseAdapter class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}

		return convertView;

	}

	private static class SharedViewHolder {
		protected Spinner person_Name;
		protected EditText person_Amount;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Resources res = mContext.getApplicationContext().getResources();

		Spinner selectedSpinner = (Spinner) parent;

		Utility.out("selected spinner id" + (Integer) selectedSpinner.getTag());

		int spinnerIndex = (Integer) selectedSpinner.getTag();

		Person p = (Person) parent.getItemAtPosition(position);

		/*
		 * if (!selectedPersonList.contains(p)) { //
		 * selectedPersonList.set((Integer) selectedSpinner.getTag(), p); //
		 * selectedPersonList.add(p); selectedPersonList.set((Integer)
		 * selectedSpinner.getTag(), p);
		 * 
		 * } else {
		 * 
		 * Toast.makeText(mContext,
		 * res.getString(R.string.duplicate_person_name_error),
		 * Toast.LENGTH_LONG).show(); selectedPersonList.set((Integer)
		 * selectedSpinner.getTag(), null); selectedSpinner.setFocusable(true);
		 * selectedSpinner.setFocusableInTouchMode(true);
		 * selectedSpinner.requestFocus();
		 * 
		 * }
		 */

		try {
			@SuppressWarnings("unused")
			Person last_Item = selectedPersonList.get(spinnerIndex);
			int selectedPersonIndex = selectedPersonList.indexOf(p);
			int selectedPersonLastIndex = selectedPersonList.lastIndexOf(p);

			if (selectedPersonIndex == -1 && selectedPersonLastIndex == -1) {
				selectedPersonList.set(spinnerIndex, p);
			} else {
				selectedPersonList.set(spinnerIndex, null);
				Toast.makeText(
						mContext,
						String.format(
								res.getString(R.string.duplicate_person_name_error),
								spinnerIndex + 1), Toast.LENGTH_LONG).show();
				selectedSpinner.setFocusable(true);
				selectedSpinner.setFocusableInTouchMode(true);
				selectedSpinner.requestFocus();
			}

		} catch (IndexOutOfBoundsException iob) {
			if (!selectedPersonList.contains(p)) {
				selectedPersonList.add(spinnerIndex, p);
			}
		}

		Utility.out("Values of Person names list " + selectedPersonList);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public Float getExpenseAmountAtPosition(int key) {
		// TODO Auto-generated method stub
		return (key < amount_List_Map.size()) ? amount_List_Map.get(key) : null;
	}

}
