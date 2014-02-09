package com.krishnan.travelexpensemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * 
 * Launcher Activity. Static UI
 *
 */
public class ExpenseLauncher extends Activity implements OnClickListener {

	private Button next = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializeElements();
	}

	private void initializeElements() {
		// TODO Auto-generated method stub
		try {
			next = (Button) findViewById(R.id.next);
			next.setOnClickListener(this);
		} catch (Exception exp) {
			Utility.out(exp.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent next_Intent = null;
		try {
			if (v != null) {
				switch (v.getId()) {
				case R.id.next:

					next_Intent = new Intent(Utility.returnActionName(
							getApplicationContext(), 0));

					startActivity(next_Intent);
					break;
				}
			}
		}

		catch (Exception exp) {
			String errorLoc = "In onClick() of" + exp.getClass();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(), exp.getCause());
			Utility.out(customMsg);
		}
	}

}
