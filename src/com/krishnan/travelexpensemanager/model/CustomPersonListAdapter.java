package com.krishnan.travelexpensemanager.model;

import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.krishnan.travelexpensemanager.R;
import com.krishnan.travelexpensemanager.util.Utility;



/**
 * @author krishm90
 * Custom array adapter to feed in participating member details 
 * for a report
 *
 */
public class CustomPersonListAdapter extends ArrayAdapter<Person> {

	private Activity mContext;
	private List<Person> list;
	private int i = 0;

	public CustomPersonListAdapter(Activity context, List<Person> person) {
		super(context, R.layout.person_row, person);
		this.mContext = context;
		this.list = person;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		Resources res = null;

		try {
			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				res = mContext.getApplicationContext().getResources();

				convertView = inflater.inflate(R.layout.person_row, parent,
						false);
				holder = new ViewHolder();

				holder.person_Text = (TextView) convertView
						.findViewById(R.id.person_text);
				holder.person_Value = (EditText) convertView
						.findViewById(R.id.person_value);

				convertView.setTag(holder);
				/*convertView.setTag(R.id.person_text, holder.person_Text);
				convertView.setTag(R.id.person_value, holder.person_Value);*/
			}

			else {
				res = mContext.getApplicationContext().getResources();
				holder = (ViewHolder) convertView.getTag();
			}

			if (i < list.size()) {
				holder.person_Value.setId(i);
				Utility.out("Value of i " + i);
				i++;
			}

			holder.person_Text.setText(String.format(
					res.getString(R.string.textview_format), position + 1));

			String name_Text = (list.get(position) != null) ? list
					.get(position).getPersonName() : "";
			String name_hint = String.format(
					res.getString(R.string.edittext_format), position + 1);
			
			holder.person_Value.setHint(name_hint);
			holder.person_Value.setText(name_Text);
			/*
			 * holder.person_Value .setOnEditorActionListener(new
			 * OnEditorActionListener() {
			 * 
			 * @Override public boolean onEditorAction(TextView v, int actionId,
			 * KeyEvent event) { // TODO Auto-generated method stub
			 * 
			 * EditText et = (EditText) v;
			 * 
			 * updatePersonNames(et);
			 * 
			 * return true; }
			 * 
			 * public void updatePersonNames(EditText et) { String person_Text =
			 * et.getText().toString(); if (person_Text != null &&
			 * person_Text.length() > 0) { list.set(et.getId(), new
			 * Person(person_Text)); Utility.out(list.get(et.getId())); }
			 * 
			 * else { Toast.makeText( mContext, mContext.getApplicationContext()
			 * .getResources() .getString( R.string.person_name_empty),
			 * Toast.LENGTH_SHORT).show(); list.set(et.getId(), null); }
			 * et.clearFocus(); } });
			 */

			holder.person_Value
					.setOnFocusChangeListener(new OnFocusChangeListener() {

						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							// TODO Auto-generated method stub
							EditText et = (EditText) v;

							Utility.out("Edit text which is getting focused "
									+ et.getId() + "--" + hasFocus);

							if (!hasFocus)
								updatePersonNames(et);
							// et.requestFocus(View.FOCUS_DOWN);
						}

						public void updatePersonNames(EditText et) {
							try {
								String person_Text = et.getText().toString()
										.trim();

								Utility.out("Edit text which is getting updated "
										+ et.getId());

								if (person_Text != null
										&& person_Text.length() > 0) {
									list.set(et.getId(),
											new Person(person_Text));
								}

								else {
									list.set(et.getId(), null);
								}

								Utility.out("Current list items " + list);
							}

							catch (Exception exp) {
								String errorLoc = "In updatePersonNames() of CustomPersonListAdapter class";
								String customMsg = String.format(
										"%s\n%s\n%s\n%s", errorLoc,
										exp.getLocalizedMessage(),
										exp.toString(), exp.getCause());
								Utility.out(customMsg);
							}
						}
					});
		}

		catch (Exception exp) {
			String errorLoc = "In getView() of" + exp.getClass();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Person getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public void resetValues() {
		View view = mContext.getLayoutInflater().inflate(R.layout.person_row,
				null);

		for (int i = 0; i < getCount(); i++) {
			EditText et = (EditText) view.findViewById(i);
			et.setText("");
			et.setHint(list.get(i).getPersonName());
		}
	}

	private static class ViewHolder {
		protected TextView person_Text;
		protected EditText person_Value;
	}
}
