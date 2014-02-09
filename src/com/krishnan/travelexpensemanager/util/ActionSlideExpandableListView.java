package com.krishnan.travelexpensemanager.util;

import com.krishnan.travelexpensemanager.ViewIndividualExpenseFragment;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * A more specific expandable listview in which the expandable area consist of
 * some buttons which are context actions for the item itself.
 * 
 * It handles event binding for those buttons and allow for adding a listener
 * that will be invoked if one of those buttons are pressed.
 * 
 * @author tjerk
 * @date 6/26/12 7:01 PM
 */
public class ActionSlideExpandableListView extends SlideExpandableListView {
	private ViewIndividualExpenseFragment instance;
	private int[] viewIds = null;
	private SparseArray<String> individual_Expense_Per_Date = null;

	public ActionSlideExpandableListView(Context context) {
		super(context);
	}

	public ActionSlideExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ActionSlideExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setItemActionListener(ViewIndividualExpenseFragment instance,
			int count, int... viewIds) {
		this.instance = instance;
		this.individual_Expense_Per_Date = new SparseArray<String>(count);
		this.viewIds = viewIds;
	}

	/**
	 * Interface for callback to be invoked whenever an action is clicked in the
	 * expandle area of the list item.
	 */
	public interface OnActionClickListener {
		/**
		 * Called when an action item is clicked.
		 * 
		 * @param itemView
		 *            the view of the list item
		 * @param clickedView
		 *            the view clicked
		 * @param position
		 *            the position in the listview
		 */
		public void onClick(View itemView, View clickedView, int position);
	}

	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(new WrapperListAdapterImpl(adapter) {

			@Override
			public View getView(final int position, View view,
					ViewGroup viewGroup) {
				final View listView = wrapped
						.getView(position, view, viewGroup);
				// add the action listeners
				/*
				 * if(buttonIds != null && listView!=null) { for(int id :
				 * buttonIds) { View buttonView = listView.findViewById(id);
				 * if(buttonView!=null) {
				 * buttonView.findViewById(id).setOnClickListener(new
				 * OnClickListener() {
				 * 
				 * @Override public void onClick(View view) { if(listener!=null)
				 * { listener.onClick(listView, view, position); } } }); } } }
				 */
				Utility.out("List view clicked.. @postion " + position);
				Utility.out(wrapped.getItem(position));
				if (viewIds != null && viewIds.length > 0) {
					TextView tv = (TextView) listView.findViewById(viewIds[0]);

					if (individual_Expense_Per_Date.indexOfKey(position) < 0) {
						String dynamic_Text = instance
								.getIndividualExpenseForDate(wrapped.getItem(
										position).toString());

						if (dynamic_Text != null)
							individual_Expense_Per_Date.put(position,
									dynamic_Text);
						else
							individual_Expense_Per_Date.put(position,
									"Error in computation");
						tv.setText(individual_Expense_Per_Date.get(position));
					}

					else {
						tv.setText(individual_Expense_Per_Date.get(position));
					}

					Button expand_Collapse = ((Button) listView
							.findViewById(viewIds[1]));
					if (expand_Collapse.getText().toString()
							.equalsIgnoreCase("+"))
						expand_Collapse.setText("-");
					else
						expand_Collapse.setText("+");
				}
				return listView;
			}
		});
	}

	@Override
	public void enableExpandOnItemClick() {
		// TODO Auto-generated method stub
		super.enableExpandOnItemClick();
	}

}
