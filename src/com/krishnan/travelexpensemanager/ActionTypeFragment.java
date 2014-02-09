package com.krishnan.travelexpensemanager;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * 
 * Fragment to get Add report /expense action
 *
 */
public class ActionTypeFragment extends Fragment implements OnClickListener {

	RadioGroup type = null;	
	Button go = null;

	ExpenseHostActivity hostActivity;
	private Bundle args_Bundle = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (this.getArguments() != null)
			args_Bundle = this.getArguments();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		hostActivity = (ExpenseHostActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_action_type, container,
				false);

		if (args_Bundle != null) {
			String title = String.format("%s %s %s", "Select",
					args_Bundle.getString("action"), "action");
			((ExpenseHostActivity) getActivity())
					.setCustomActionBarTitle(title);
		}

		registerActions(view);
		return view;
	}

	private void registerActions(View view) {
		// TODO Auto-generated method stub
		try {
			type = (RadioGroup) view.findViewById(R.id.radio_layout);

			go = (Button) view.findViewById(R.id.go);

			go.setOnClickListener(this);

		} catch (Exception exp) {
			String errorLoc = "In registerActions() of"
					+ exp.getClass().getName();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Resources res = hostActivity.getApplicationContext().getResources();

		if (v.getId() == R.id.go) {

			if (type.getCheckedRadioButtonId() != -1) {
				args_Bundle.putInt("type_id", type.getCheckedRadioButtonId());
				hostActivity.onActionTypeSelected(args_Bundle);
			}

			else {
				Toast.makeText(getActivity(),
						res.getString(R.string.type_error), Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	public interface onActionTypeSelectListener {
		public void onActionTypeSelected(Bundle args);
	}

}
