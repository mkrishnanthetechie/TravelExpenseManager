package com.krishnan.travelexpensemanager.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseArray;

import com.krishnan.travelexpensemanager.R;
import com.krishnan.travelexpensemanager.util.Utility;

/**
 * @author krishm90
 * Data model class which has methods to interact with data base.
 * 
 */

@SuppressLint("DefaultLocale")
public class DbHelperModel {

	Context mContext;
	DbHelper dbHelper;

	/**
	 * constructor for Model helper class
	 * 
	 */
	public DbHelperModel(Context context) {
		this.mContext = context;
		dbHelper = new DbHelper(mContext);
	}

	/**
	 * @param data
	 * @throws ParseException
	 *             Persists the report details in db
	 */
	@SuppressWarnings("unchecked")
	public void insertInitialReportEntries(Bundle data) throws ParseException {

		SQLiteDatabase database = null;
		ArrayList<Long> rowIds;

		try {
			database = dbHelper.openDataBase();
			rowIds = new ArrayList<Long>();

			ContentValues report_details = new ContentValues();
			ContentValues person_Report_Details = new ContentValues();

			report_details.put("report_name", data.getString("trip_name"));

			String start_date = data.getString("start_date");

			report_details.put("start_date", start_date);

			if (data.containsKey("end_date"))
				report_details.put("end_date", data.getString("end_date"));

			report_details.put("person_count", data.getInt("person_count"));

			Long report_id = dbHelper.insertTableData(database,
					ExpenseManagerModel.TABLE_REPORT_DETAILS, report_details);

			Utility.out("Row inserted into "
					+ ExpenseManagerModel.TABLE_REPORT_DETAILS + " --> "
					+ report_id);

			rowIds.clear();
			if (data.containsKey("person_list")) {
				List<Person> person_List = (List<Person>) data
						.getSerializable("person_list");

				for (Person p : person_List) {
					person_Report_Details.put("report_id", report_id);
					person_Report_Details.put("person_name", p.toString());
					Long personReportId = dbHelper.insertTableData(database,
							ExpenseManagerModel.TABLE_PERSON_REPORT_DETAILS,
							person_Report_Details);
					rowIds.add(personReportId);
				}
			}

			Utility.out("Person table: " + rowIds);
		}

		catch (SQLException exp) {
			String errorLoc = "In insertInitialReportEntries() of"
					+ exp.getClass().getName();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

		finally {
			if (database != null)
				database.close();
		}
	}

	/**
	 * @param args
	 * @return
	 * @throws Exception
	 *             Returns specific/all the report object
	 * 
	 */
	public ArrayList<Report> getReportDetails(String... args) throws Exception {

		SQLiteDatabase database = null;
		ArrayList<Report> report_List = null;
		Cursor c = null;
		Resources res = null;

		try {
			database = dbHelper.openDataBase();
			res = mContext.getApplicationContext().getResources();

			String[] columns = new String[] { "report_name", "start_date",
					"end_date", "person_count", "report_id" };

			if (args != null && args.length > 0) {
				String selection = "report_name = ? AND start_date = ?";

				String selectionArgs[] = new String[] { args[0], args[1] };

				c = dbHelper.getCursorFromTableNameWithSelection(database,
						ExpenseManagerModel.TABLE_REPORT_DETAILS, columns,
						selection, selectionArgs);
			}

			else {
				c = dbHelper.getCursorFromTableName(database,
						ExpenseManagerModel.TABLE_REPORT_DETAILS, columns);
			}

			Utility.out("Rows present in "
					+ ExpenseManagerModel.TABLE_REPORT_DETAILS + "is "
					+ c.getCount());

			if (c != null && c.getCount() > 0) {
				report_List = new ArrayList<Report>(c.getCount());
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					int col1_index = c.getColumnIndex("report_name");
					int col2_index = c.getColumnIndex("start_date");
					int col3_index = c.getColumnIndex("end_date");
					int col4_index = c.getColumnIndex("person_count");
					int col5_index = c.getColumnIndex("report_id");

					Report report = new Report(c.getString(col1_index),
							c.getString(col2_index), c.getString(col3_index),
							c.getInt(col4_index));
					report.setReport_Id(c.getInt(col5_index));
					report_List.add(report);
				}
			} else {
				throw new Exception(res.getString(R.string.no_rows_found));
			}
		}

		catch (SQLException sqlExp) {
			String errorLoc = "In getReportDetails() of"
					+ sqlExp.getClass().getName();
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					sqlExp.getLocalizedMessage(), sqlExp.toString(),
					sqlExp.getStackTrace());
			Utility.out(customMsg);
		}

		finally {
			Utility.out("Finally block executed from getReportDetails method");
			if (c != null)
				c.close();
			if (database != null)
				database.close();
		}

		return report_List;
	}

	/**
	 * @param args
	 * @return whether the keyed in report details exists already
	 * @throws Exception
	 */
	public boolean isReportExistsAlready(String... args) throws Exception {
		SQLiteDatabase database = null;
		Cursor c = null;

		try {
			database = dbHelper.openDataBase();

			String[] columns = new String[] { "report_id", "report_name",
					"start_date", };

			String selection = "report_name = ? AND start_date = ?";

			String selectionArgs[] = new String[] { args[0], args[1] };

			c = dbHelper.getCursorFromTableNameWithSelection(database,
					ExpenseManagerModel.TABLE_REPORT_DETAILS, columns,
					selection, selectionArgs);

			Utility.out("Rows present in "
					+ ExpenseManagerModel.TABLE_REPORT_DETAILS + "is "
					+ c.getCount());
		}

		catch (Exception sqlExp) {
			String errorLoc = "In isReportExistsAlready() of DbHelperModel class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					sqlExp.getLocalizedMessage(), sqlExp.toString(),
					sqlExp.getStackTrace());
			Utility.out(customMsg);
		}

		finally {
			Utility.out("Finally block executed from getReportDetails method");
			if (c != null)
				c.close();
			if (database != null)
				database.close();
		}

		return (c.getCount() > 0) ? true : false;

	}

	/**
	 * @param report
	 * @return the members
	 * @throws Exception
	 */
	public ArrayList<Person> getMembersOfSelectedReport(Report report)
			throws Exception {
		// TODO Auto-generated method stub

		SQLiteDatabase database = null;
		ArrayList<Person> person_List = null;
		Cursor c = null;

		try {
			database = dbHelper.openDataBase();
			String[] columns = new String[] { "person_name" };

			String selection = "report_id = ?";

			String selectionArgs[] = new String[] { String.valueOf(report
					.getReport_Id()) };

			c = dbHelper.getCursorFromTableNameWithSelection(database,
					ExpenseManagerModel.TABLE_PERSON_REPORT_DETAILS, columns,
					selection, selectionArgs);

			Utility.out("Rows present in "
					+ ExpenseManagerModel.TABLE_PERSON_REPORT_DETAILS
					+ " for trip Name " + "{" + report.getTrip_Name() + ","
					+ report.getStart_Date() + "} is " + c.getCount());

			if (c != null && c.getCount() > 0) {
				person_List = new ArrayList<Person>(c.getCount());
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					int col1_index = c.getColumnIndex("person_name");
					Person p = new Person(c.getString(col1_index));
					person_List.add(p);
				}
			}

		}

		catch (Exception exp) {
			throw new Exception(exp.toString());
		}

		finally {
			if (c != null)
				c.close();
			if (database != null)
				database.close();
		}

		return person_List;
	}

	/**
	 * @param args
	 * @return rowIds of expenses persisted
	 */
	@SuppressLint("DefaultLocale")
	@SuppressWarnings("unchecked")
	public String insertExpenseDetails(Bundle args) {

		SQLiteDatabase database = null;
		ContentValues expense_Details, shared_Expense_Details;
		Resources res;

		long id = 0L;
		List<Long> id_List = null;

		Expense basic_expense = (Expense) args.getSerializable("expense");

		try {
			database = dbHelper.openDataBase();
			res = mContext.getResources();

			expense_Details = new ContentValues();

			shared_Expense_Details = new ContentValues();

			expense_Details.put("report_id", basic_expense.getReport_Name()
					.getReport_Id());
			expense_Details
					.put("expense_name", basic_expense.getExpense_Name());

			String time = basic_expense.getExpense_Time();

			String formatted_time = time.split(" ")[1].equalsIgnoreCase("AM")
					|| (Integer.parseInt(time.split(" ")[0].split(":")[0])) > 11 ? time
					.split(" ")[0] : String.format("%d:%s",
					(Integer.parseInt(time.split(" ")[0].split(":")[0])) + 12,
					time.split(" ")[0].split(":")[1]);

			String formatted_date_String = String.format("%s %s",
					basic_expense.getExpense_Date(), formatted_time);

			Utility.out("Formatted date string is : " + formatted_date_String);

			expense_Details.put("expense_date", formatted_date_String);

			expense_Details.put("expense_amount",
					basic_expense.getExpense_Amount());
			expense_Details.put("expense_comments",
					basic_expense.getExpense_Comments());

			if (basic_expense.getExpense_Type().equalsIgnoreCase(
					res.getString(R.string.individual_text).trim())) {

				Utility.out("Individual expense is being inserted");

				Expense individual_Expense = (Expense) args
						.getSerializable("individual");

				expense_Details.put("expense_by", individual_Expense
						.getPerson().getPersonName());

				expense_Details.put("isExpenseShared", false);

				id = dbHelper.insertTableData(database,
						ExpenseManagerModel.TABLE_EXPENSE_DETAILS,
						expense_Details);

				Utility.out("Successfully inserted data into expense table --> "
						+ id);
			}

			else if (basic_expense.getExpense_Type().equalsIgnoreCase(
					res.getString(R.string.shared_text).trim())) {

				Utility.out("Shared expense is being inserted");

				id_List = new ArrayList<Long>();

				expense_Details.put("isExpenseShared", true);

				List<Expense> shared_Expense_List = (List<Expense>) args
						.getSerializable("shared_expense_list");

				id_List.clear();

				expense_Details.put("expense_by", "-");
				expense_Details
						.put("sharing_count", shared_Expense_List.size());

				id = dbHelper.insertTableData(database,
						ExpenseManagerModel.TABLE_EXPENSE_DETAILS,
						expense_Details);

				Utility.out("Successfully inserted data into expense table --> "
						+ id);

				for (Expense each_Shared_Expense : shared_Expense_List) {
					expense_Details.put("expense_by", each_Shared_Expense
							.getPerson().getPersonName());

					shared_Expense_Details.put("_id", (int) id);
					shared_Expense_Details.put("expense_name",
							basic_expense.getExpense_Name());
					shared_Expense_Details.put("expense_by",
							each_Shared_Expense.getPerson().getPersonName());
					shared_Expense_Details.put("expense_amount",
							each_Shared_Expense.getExpense_Amount());

					long i = dbHelper.insertTableData(database,
							ExpenseManagerModel.TABLE_SHARED_EXPENSE_DETAILS,
							shared_Expense_Details);
					id_List.add(i);
				}
			}

		}

		catch (Exception exp) {
			String errorLoc = "In insertExpenseDetails() of DbHelperModel class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

		finally {
			if (database != null)
				database.close();
		}

		return (basic_expense.getExpense_Type().equalsIgnoreCase("individual") ? String
				.valueOf(id) : id_List.toString());
	}

	/**
	 * @param report
	 * @param database
	 * @return the cursor contains expenses related to selected report
	 * @throws Exception
	 */
	public Cursor getExpenseCursorFromReport(Report report,
			SQLiteDatabase database) throws Exception {
		// TODO Auto-generated method stub

		Cursor c = null;

		try {

			String[] columns = new String[] { "_id", "expense_name",
					"expense_date", "expense_amount", "expense_by",
					"expense_comments", "isExpenseShared", "sharing_count" };

			String selection = "report_id = ?";

			String selectionArgs[] = new String[] { String.valueOf(report
					.getReport_Id()) };
			String orderBy = "expense_date DESC";

			c = dbHelper.getCursorFromTableNameWithSelectionSorted(database,
					ExpenseManagerModel.TABLE_EXPENSE_DETAILS, columns,
					selection, selectionArgs, orderBy);
		}

		catch (Exception exp) {
			throw new Exception(exp.toString());
		}

		return c;

	}

	/**
	 * @param args
	 * @return the constructed expense objects related to selected report
	 */
	public SparseArray<Expense> populateExpenseDetailsForReport(Bundle args) {

		Cursor shared_Cursor = null;
		Cursor expense_Cursor = null;
		SQLiteDatabase database = null;

		SparseArray<Expense> expense_List = null;

		try {
			Report report = (Report) args.getSerializable("selected_report");
			database = dbHelper.openDataBase();
			expense_Cursor = this.getExpenseCursorFromReport(report, database);

			Utility.out("Rows present in "
					+ ExpenseManagerModel.TABLE_EXPENSE_DETAILS
					+ " for trip Name " + report.getTrip_Name() + " is "
					+ expense_Cursor.getCount());

			expense_List = new SparseArray<Expense>(expense_Cursor.getCount());

			int i = 1;

			if (expense_Cursor != null && expense_Cursor.getCount() > 0) {
				for (expense_Cursor.moveToFirst(); !expense_Cursor
						.isAfterLast(); expense_Cursor.moveToNext()) {

					int col1_index = expense_Cursor.getColumnIndex("_id");
					int col2_index = expense_Cursor
							.getColumnIndex("expense_name");
					int col3_index = expense_Cursor
							.getColumnIndex("expense_date");
					int col4_index = expense_Cursor
							.getColumnIndex("expense_amount");
					int col5_index = expense_Cursor
							.getColumnIndex("expense_comments");
					int col6_index = expense_Cursor
							.getColumnIndex("isExpenseShared");
					int col7_index = expense_Cursor
							.getColumnIndex("sharing_count");
					int col8_index = expense_Cursor
							.getColumnIndex("expense_by");

					Utility.out("IsShared Value: "
							+ expense_Cursor.getString(col6_index));

					String date = expense_Cursor.getString(col3_index);

					int index = date.indexOf(" ");

					String wrapped_DateString = String
							.format("%s\n%s", date.substring(0, index),
									date.substring(index + 1));

					String comments = expense_Cursor.getString(col5_index);

					int length = comments.length();

					StringBuffer comments_wrapper = new StringBuffer();

					if (length > 10) {
						for (int j = 0; length > 10; j++) {
							comments_wrapper.append(String.format("%s\n",
									comments.substring(j * 10,
											((j + 1) * 10))));
							length = length - 10;
						}
						comments_wrapper.append(String.format("%s\n", comments
								.substring((comments.length() / 10) * 10)));
					}

					else {
						comments_wrapper.append(String.format("%s", comments));
					}

					if (expense_Cursor.getInt(col6_index) == 1) {
						shared_Cursor = this.getSharedExpenseCursor(database,
								expense_Cursor.getString(col1_index),
								expense_Cursor.getString(col2_index));
						if (shared_Cursor != null
								&& shared_Cursor.getCount() > 0) {

							StringBuffer buf = new StringBuffer();

							for (shared_Cursor.moveToFirst(); !shared_Cursor
									.isAfterLast(); shared_Cursor.moveToNext()) {

								buf.append(String.format(
										"%s-%s;\n",
										shared_Cursor.getString(shared_Cursor
												.getColumnIndex("expense_by")),
										shared_Cursor.getString(shared_Cursor
												.getColumnIndex("expense_amount"))));

							}

							Expense expense = new Expense.ExpenseBuilder()
									.name(expense_Cursor.getString(col2_index))
									.date(wrapped_DateString)
									.type("shared")
									.comments(comments_wrapper.toString())
									.amount(Float.valueOf(expense_Cursor
											.getString(col4_index)))
									.sharing(expense_Cursor.getInt(col7_index))
									.person(new Person(buf.toString())).build();

							expense_List.put(i, expense);
							buf = null;
						}

						if (shared_Cursor != null)
							shared_Cursor.close();
					}

					else {
						Expense expense = new Expense.ExpenseBuilder()
								.name(expense_Cursor.getString(col2_index))
								.date(wrapped_DateString)
								.type("single")
								.comments(comments_wrapper.toString())
								.amount(Float.valueOf(expense_Cursor
										.getString(col4_index)))
								.person(new Person(expense_Cursor
										.getString(col8_index))).build();

						expense_List.put(i, expense);
					}

					++i;
				}
			}
		}

		catch (Exception exp) {
			String errorLoc = "In populateExpenseDetailsForReport() of DbHelperModel class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString(),
					exp.getStackTrace());
			Utility.out(customMsg);
		}

		finally {
			Utility.out("Finally block executed from populateExpenseDetailsForReport method");
			if (expense_Cursor != null)
				expense_Cursor.close();

			if (shared_Cursor != null)
				shared_Cursor.close();

			if (database != null)
				database.close();
		}

		return expense_List;
	}

	/**
	 * @param database
	 * @param args
	 * @return shared expense cursor for the shared expense types
	 * @throws Exception
	 */
	public Cursor getSharedExpenseCursor(SQLiteDatabase database,
			String... args) throws Exception {
		Cursor c = null;

		try {

			String[] columns = new String[] { "_id", "expense_name",
					"expense_by", "expense_amount" };

			String selection = "_id = ? AND expense_name = ?";

			String selectionArgs[] = new String[] { args[0], args[1] };

			c = dbHelper.getCursorFromTableNameWithSelection(database,
					ExpenseManagerModel.TABLE_SHARED_EXPENSE_DETAILS, columns,
					selection, selectionArgs);

			Utility.out("Rows present in "
					+ ExpenseManagerModel.TABLE_SHARED_EXPENSE_DETAILS
					+ " for shared expense " + args[0] + "--" + args[1]
					+ " is " + c.getCount());
		}

		catch (Exception exp) {
			throw new Exception(exp.toString());
		}

		return c;
	}

	/**
	 * @param database
	 * @param args
	 * @return the cursor for shared expense types contains the participating
	 *         members total expense for the selected day
	 * @throws Exception
	 */
	public Cursor getDayWiseSharedExpense(SQLiteDatabase database,
			String... args) throws Exception {

		try {
			String raw_Query = "select s.expense_by as \"expense_by\", SUM(s.expense_amount) as \"sum\" "
					+ " from report_expense_details i JOIN report_shared_expense_details s on i._id = s._id "
					+ "where i.report_id = ? AND date(i.expense_date) = ? "
					+ "group by s.expense_by having i.isExpenseShared = 1 AND i.sharing_count > 0";

			Utility.out("Query before execution is : " + raw_Query);

			return dbHelper.getCursorFromRawQuery(database, raw_Query, args);

		} catch (Exception exp) {
			throw new Exception(exp.toString());
		}
	}

	/**
	 * @param database
	 * @param args
	 * @return the cursor for individual expense types contains the
	 *         participating members total expense for the selected day
	 * @throws Exception
	 */
	public Cursor getDayWiseIndividualExpense(SQLiteDatabase database,
			String... args) throws Exception {

		try {
			String raw_Query = "select expense_by as \"expense_by\", SUM(expense_amount) as \"sum\" from report_expense_details "
					+ " where report_id = ? AND date(expense_date) = ? AND isExpenseShared = 0 AND sharing_count = 0 "
					+ " group by expense_by";

			Utility.out("Query before execution is : " + raw_Query);

			return dbHelper.getCursorFromRawQuery(database, raw_Query, args);
		}

		catch (Exception exp) {
			throw new Exception(exp.toString());
		}
	}

	/**
	 * @param report
	 * @return the distinct expense dates
	 */
	public List<String> getDistinctExpenseDatesFromTable(Report report) {

		SQLiteDatabase database = null;
		List<String> distinct_Expense_Dates = null;
		Cursor c = null;

		try {

			String rawQuery = "select distinct(date(expense_date)) as \"distinct_expense_dates\" from report_expense_details "
					+ " where report_id = ? ";

			Utility.out("Actual query: " + rawQuery);
			database = dbHelper.openDataBase();
			c = dbHelper.getCursorFromRawQuery(database, rawQuery,
					String.valueOf(report.getReport_Id()));

			Utility.out("Distinct expense dates count is " + c.getCount());

			if (c.getCount() > 0) {
				distinct_Expense_Dates = new ArrayList<String>(c.getCount());
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					int col1 = c.getColumnIndex("distinct_expense_dates");
					distinct_Expense_Dates.add(c.getString(col1));
				}
			}
		}

		catch (Exception exp) {
			String errorLoc = "In getDistinctExpenseDatesFromTable() of DbHelperModel class";
			String customMsg = String.format("%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString());
			Utility.out(customMsg);
		}

		finally {
			if (c != null)
				c.close();
			if (database != null)
				database.close();
		}
		return distinct_Expense_Dates;
	}

	/**
	 * @param database
	 * @param report
	 * @return the cursor for shared expense types contains the participating
	 *         members total expense so far in this trip
	 * @throws Exception
	 */
	public Cursor getTotalWiseFromSharedExpense(SQLiteDatabase database,
			Report report) throws Exception {

		try {
			String raw_Query = "select s.expense_by as \"expense_by\" ,SUM(s.expense_amount) as \"sum\" "
					+ "from report_expense_details i JOIN report_shared_expense_details s on i._id = s._id "
					+ "where i.report_id = ? "
					+ "group by s.expense_by having i.isExpenseShared = 1 AND i.sharing_count > 0";

			Utility.out("Query before execution is : " + raw_Query);

			return dbHelper.getCursorFromRawQuery(database, raw_Query,
					String.valueOf(report.getReport_Id()));
		}

		catch (Exception exp) {
			throw new Exception(exp.toString());
		}

	}

	/**
	 * @param database
	 * @param report
	 * @return the cursor for individual expense types contains the
	 *         participating members total expense
	 * @throws Exception
	 */
	public Cursor getTotalWiseFromIndividualExpense(SQLiteDatabase database,
			Report report) throws Exception {

		try {
			String raw_Query = "select expense_by as \"expense_by\", SUM(expense_amount) as \"sum\" "
					+ "from report_expense_details where report_id = ? AND isExpenseShared = 0 AND sharing_count = 0 "
					+ "group by expense_by";

			Utility.out("Query before execution is : " + raw_Query);

			return dbHelper.getCursorFromRawQuery(database, raw_Query,
					String.valueOf(report.getReport_Id()));
		}

		catch (Exception exp) {
			throw new Exception(exp.toString());
		}

	}

	/**
	 * @param report
	 * @return the participating members total expense done so far in this trip
	 */
	public List<Expense> getMembersTotalExpense(Report report) {

		Cursor shared_Expense_Cursor = null;
		Cursor individual_Expense_Cursor = null;
		SQLiteDatabase database = null;

		Map<String, Float> total_Individual_Expense = null;

		ArrayList<Expense> final_Individual_Expense_List = null;
		try {
			database = dbHelper.openDataBase();
			individual_Expense_Cursor = this.getTotalWiseFromIndividualExpense(
					database, report);
			shared_Expense_Cursor = this.getTotalWiseFromSharedExpense(
					database, report);

			total_Individual_Expense = this.getPersonExpenseMapping(
					shared_Expense_Cursor, individual_Expense_Cursor,
					report.getPerson_Count());

			final_Individual_Expense_List = new ArrayList<Expense>(
					report.getPerson_Count());

			for (String key : total_Individual_Expense.keySet()) {
				Expense expense = new Expense.ExpenseBuilder()
						.person(new Person(key))
						.amount(total_Individual_Expense.get(key)).build();
				final_Individual_Expense_List.add(expense);
			}

		} catch (Exception exp) {
			String errorLoc = "In getMembersTotalExpense() of DbHelperModel class";
			String customMsg = String.format("%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString());
			Utility.out(customMsg);
		}

		finally {
			total_Individual_Expense = null;
			if (individual_Expense_Cursor != null)
				individual_Expense_Cursor.close();
			if (shared_Expense_Cursor != null)
				shared_Expense_Cursor.close();
			if (database != null) {
				database.close();
				Utility.out("DB connection closed from getMembersTotalExpense method");
			}
		}

		return final_Individual_Expense_List;
	}

	/**
	 * @param report
	 * @param date
	 * @return the participating members total expense done for the selected
	 *         date
	 */
	public String getMembersExpenseForDate(Report report, String date) {

		Cursor shared_Expense_Cursor = null;
		Cursor individual_Expense_Cursor = null;
		SQLiteDatabase database = null;

		Map<String, Float> individual_Expense_For_Date = null;
		try {
			database = dbHelper.openDataBase();
			individual_Expense_Cursor = this.getDayWiseIndividualExpense(
					database, String.valueOf(report.getReport_Id()), date);
			shared_Expense_Cursor = this.getDayWiseSharedExpense(database,
					String.valueOf(report.getReport_Id()), date);

			individual_Expense_For_Date = this.getPersonExpenseMapping(
					shared_Expense_Cursor, individual_Expense_Cursor,
					report.getPerson_Count());

			StringBuffer individual_Expense_Text_For_Date = new StringBuffer();
			for (String key : individual_Expense_For_Date.keySet()) {
				individual_Expense_Text_For_Date.append(String.format(
						"%s-%s;\n", key, individual_Expense_For_Date.get(key)));
			}

			return individual_Expense_Text_For_Date.toString();

		} catch (Exception exp) {
			String errorLoc = "In getMembersExpenseForDate() of DbHelperModel class";
			String customMsg = String.format("%s\n%s\n%s", errorLoc,
					exp.getLocalizedMessage(), exp.toString());
			Utility.out(customMsg);
		}

		finally {
			individual_Expense_For_Date = null;
			if (individual_Expense_Cursor != null)
				individual_Expense_Cursor.close();
			if (shared_Expense_Cursor != null)
				shared_Expense_Cursor.close();
			if (database != null) {
				database.close();
				Utility.out("DB connection closed from getMembersExpenseForDate method");
			}
		}

		return null;
	}

	/**
	 * @param shared_Expense_Cursor
	 * @param individual_Expense_Cursor
	 * @param mapSize
	 * @return the mapping between member to amount spent
	 * @throws NullPointerException
	 */
	private Map<String, Float> getPersonExpenseMapping(
			Cursor shared_Expense_Cursor, Cursor individual_Expense_Cursor,
			int mapSize) throws NullPointerException {

		HashMap<String, Float> individual_Expense_Map = new HashMap<String, Float>(
				mapSize);

		if (individual_Expense_Cursor != null) {

			Utility.out("Individual Expense count is "
					+ individual_Expense_Cursor.getCount());

			if (individual_Expense_Cursor.getCount() > 0) {

				for (individual_Expense_Cursor.moveToFirst(); !individual_Expense_Cursor
						.isAfterLast(); individual_Expense_Cursor.moveToNext()) {

					int col1 = individual_Expense_Cursor
							.getColumnIndex("expense_by");
					int col2 = individual_Expense_Cursor.getColumnIndex("sum");

					String expense_By = individual_Expense_Cursor
							.getString(col1);
					Float sum_Amount = Float.valueOf(individual_Expense_Cursor
							.getString(col2));
					individual_Expense_Map.put(expense_By, sum_Amount);
				}
			}
		}

		else {
			throw new NullPointerException(
					"There is an error while querying individual expenses");
		}

		if (shared_Expense_Cursor != null) {

			Utility.out("Shared Expense count is "
					+ shared_Expense_Cursor.getCount());

			if (shared_Expense_Cursor.getCount() > 0) {
				for (shared_Expense_Cursor.moveToFirst(); !shared_Expense_Cursor
						.isAfterLast(); shared_Expense_Cursor.moveToNext()) {

					int col1 = shared_Expense_Cursor
							.getColumnIndex("expense_by");
					int col2 = shared_Expense_Cursor.getColumnIndex("sum");

					String expense_By = shared_Expense_Cursor.getString(col1);
					Float sum_Amount = Float.valueOf(shared_Expense_Cursor
							.getString(col2));

					if (individual_Expense_Map.containsKey(expense_By)) {
						individual_Expense_Map.put(expense_By,
								individual_Expense_Map.get(expense_By)
										+ sum_Amount);
					} else {
						individual_Expense_Map.put(expense_By, sum_Amount);
					}
				}
			}
		}

		else {
			throw new NullPointerException(
					"There is an error while querying shared expenses");
		}

		return individual_Expense_Map;
	}

	/**
	 * Destroys the objects when Host activity got destroyed
	 */
	public void destroyObjects() {
		// TODO Auto-generated method stub
		this.mContext = null;
		dbHelper = null;
	}

	/**
	 * @param report
	 * @return outcome of deletion of report selected
	 */
	public boolean deleteSelectedReport(Report report) {
		// TODO Auto-generated method stub

		SQLiteDatabase database = null;
		Cursor expense_Cursor = null;
		List<Integer> expense_Ids = null;
		boolean returnValue = false;

		try {
			database = dbHelper.openDataBase();
			expense_Cursor = this.getExpenseCursorFromReport(report, database);

			if (expense_Cursor != null && expense_Cursor.getCount() > 0) {
				expense_Ids = new ArrayList<Integer>(expense_Cursor.getCount());
				for (expense_Cursor.moveToFirst(); !expense_Cursor
						.isAfterLast(); expense_Cursor.moveToNext()) {
					int col1 = expense_Cursor.getColumnIndex("_id");
					int expense_Id = expense_Cursor.getInt(col1);
					expense_Ids.add(expense_Id);
				}
			}

			if (expense_Ids != null && expense_Ids.size() > 0) {
				this.deleteExpenseFromSelectedReport(expense_Ids,
						ExpenseManagerModel.TABLE_SHARED_EXPENSE_DETAILS,
						database);
				this.deleteExpenseFromSelectedReport(expense_Ids,
						ExpenseManagerModel.TABLE_EXPENSE_DETAILS, database);
			}
			this.deleteSelectedReport(report,
					ExpenseManagerModel.TABLE_PERSON_REPORT_DETAILS, database);
			this.deleteSelectedReport(report,
					ExpenseManagerModel.TABLE_REPORT_DETAILS, database);
			returnValue = true;

		} catch (Exception sqlExp) {
			String errorLoc = "In deleteSelectedReport() of DbHelperModel class";
			String customMsg = String.format("%s\n%s\n%s\n%s", errorLoc,
					sqlExp.getLocalizedMessage(), sqlExp.toString(),
					sqlExp.getStackTrace());
			Utility.out(customMsg);
			returnValue = false;
		}

		finally {
			Utility.out("Finally block executed from deleteSelectedReport method");
			expense_Ids = null;
			if (expense_Cursor != null)
				expense_Cursor.close();
			if (database != null)
				database.close();
		}
		return returnValue;
	}

	/**
	 * @param report
	 * @param tableName
	 * @param database
	 * @throws Exception
	 * Deletes the row from the report details & person report details
	 */
	private void deleteSelectedReport(Report report, String tableName,
			SQLiteDatabase database) throws Exception {
		// TODO Auto-generated method stub
		String whereClause = "report_id = ?";
		dbHelper.deleteTableRowsWithSelection(database, tableName, whereClause,
				String.valueOf(report.getReport_Id()));
	}

	/**
	 * @param expense_Ids
	 * @param tableName
	 * @param database
	 * @throws Exception
	 * Deletes the associated expenses
	 */
	private void deleteExpenseFromSelectedReport(List<Integer> expense_Ids,
			String tableName, SQLiteDatabase database) throws Exception {
		// TODO Auto-generated method stub

		String whereClause = "_id = ?";

		for (Integer id : expense_Ids) {
			dbHelper.deleteTableRowsWithSelection(database, tableName,
					whereClause, String.valueOf(id));
		}
	}
}
