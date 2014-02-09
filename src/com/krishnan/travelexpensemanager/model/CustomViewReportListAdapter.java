package com.krishnan.travelexpensemanager.model;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.krishnan.travelexpensemanager.R;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Custom adapter to load the reports added
 *
 */
public class CustomViewReportListAdapter extends ArrayAdapter<Report> {

	private Activity mContext;
	private List<Report> list;

	public CustomViewReportListAdapter(Activity context,
			List<Report> report_List) {
		super(context, R.layout.view_report_list_row, report_List);
		this.mContext = context;
		this.list = report_List;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Report getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public int getPosition(Report item) {
		// TODO Auto-generated method stub
		return list.indexOf(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		try {
			if (convertView == null) {
				LayoutInflater inflater = mContext.getLayoutInflater();

				convertView = inflater.inflate(R.layout.view_report_list_row,
						parent, false);
				holder = new ViewHolder();

				holder.serial_Number = (TextView) convertView
						.findViewById(R.id.view_report_serial_number);

				holder.report_Name = (TextView) convertView
						.findViewById(R.id.view_report_name);

				holder.period = (TextView) convertView
						.findViewById(R.id.view_report_period);

				holder.members_Count = (TextView) convertView
						.findViewById(R.id.view_report_member_count);

				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			Report report = list.get(position);

			holder.serial_Number.setText(String.valueOf(position + 1));
			holder.report_Name.setText(report.getTrip_Name());

			String period_Text = (report.getEnd_Date().equalsIgnoreCase("-")) ? String
					.format("%s -> %s", report.getStart_Date(), "Nil") : String
					.format("%s -> %s", report.getStart_Date(),
							report.getEnd_Date());

			holder.period.setText(period_Text);
			holder.members_Count.setText(String.valueOf(report
					.getPerson_Count()));

		} catch (Exception exp) {
			String errorLoc = "In getView() of CustomViewReportListAdapter";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
		return convertView;
	}

	private static class ViewHolder {
		protected TextView serial_Number;
		protected TextView report_Name;
		protected TextView period;
		protected TextView members_Count;
	}
}
