package com.krishnan.travelexpensemanager.model;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.krishnan.travelexpensemanager.R;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Custom array adapter to load the day wise expense list
 *
 */
public class CustomDayWiseIndividualExpense extends ArrayAdapter<String>
		implements OnClickListener {

	private Activity mContext;
	private List<String> list;
	private TextView expense_Text;

	public CustomDayWiseIndividualExpense(Activity context,
			List<String> expense_dates) {
		super(context, R.layout.expandable_listview_item, expense_dates);
		this.mContext = context;
		this.list = expense_dates;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		try {
			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();
				convertView = inflater.inflate(
						R.layout.expandable_listview_item, parent, false);
				holder = new ViewHolder();
				convertView.setTag(holder);

				holder.dayView = (TextView) convertView
						.findViewById(R.id.distinct_expense_date);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.dayView.setText(list.get(position));

		} catch (Exception exp) {

			String errorLoc = "In getView() of CustomSharedExpenseAdapter class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
		return convertView;
	}

	private static class ViewHolder {
		protected TextView dayView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Button expand_Button = (Button) v;
		int selectedPosition = (Integer) expand_Button.getTag();

		Utility.out("Selected position: " + selectedPosition);
		expense_Text.setText(list.get(selectedPosition));
	}
}
