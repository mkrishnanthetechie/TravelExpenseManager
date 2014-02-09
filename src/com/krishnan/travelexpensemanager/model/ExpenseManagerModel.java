/**
 * 
 */
package com.krishnan.travelexpensemanager.model;

/**
 * @author krishm90
 * Interface to define the tables names /schema
 * 
 */
public interface ExpenseManagerModel {

	public String TABLE_REPORT_DETAILS = "report_details";

	public String TABLE_PERSON_REPORT_DETAILS = "person_report_details";

	public String TABLE_EXPENSE_DETAILS = "report_expense_details";

	public String TABLE_SHARED_EXPENSE_DETAILS = "report_shared_expense_details";

	public String TABLE_REPORT_DETAILS_CREATE = String.format(
			"%s %s %s %s %s %s %s %s %s", "CREATE TABLE IF NOT EXISTS ",
			TABLE_REPORT_DETAILS, " ( ",
			"report_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,",
			"report_name TEXT not null ,", " start_date TEXT not null ,",
			"end_date TEXT DEFAULT '-' ,", "person_count NUMERIC not null ",
			")");

	public String TABLE_PERSON_REPORT_DETAILS_CREATE = String.format(
			"%s %s %s %s %s %s %s %s %s", "CREATE TABLE IF NOT EXISTS ",
			TABLE_PERSON_REPORT_DETAILS, " ( ", "report_id INTEGER not null ,",
			"person_name TEXT not null ,",
			"FOREIGN KEY (report_id) REFERENCES ", TABLE_REPORT_DETAILS,
			"(report_id)", ")");

	public String TABLE_EXPENSE_DETAILS_CREATE = String.format(
			"%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", "CREATE TABLE IF NOT EXISTS ",
			TABLE_EXPENSE_DETAILS, " ( ",
			"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ",
			"report_id INTEGER not null ,", "expense_name TEXT not null, ",
			"expense_by TEXT not null, ", "expense_amount numeric not null, ",
			"expense_comments TEXT not null, ",
			"expense_date DATETIME not null DEFAULT current_timestamp, ",
			"isExpenseShared boolean not null DEFAULT false, ",
			"sharing_count numeric DEFAULT 0, ",
			"FOREIGN KEY (report_id) REFERENCES ", TABLE_REPORT_DETAILS,
			"(report_id)", ")");

	public String TABLE_SHARED_EXPENSE_DETAILS_CREATE = String.format(
			"%s%s%s%s%s%s%s%s%s%s%s", "CREATE TABLE IF NOT EXISTS ",
			TABLE_SHARED_EXPENSE_DETAILS, " ( ", "_id INTEGER NOT NULL ,",
			"expense_name TEXT not null, ", "expense_by TEXT not null, ",
			"expense_amount numeric not null, ",
			"FOREIGN KEY (_id, expense_name) REFERENCES ",
			TABLE_EXPENSE_DETAILS, "(_id, expense_name)", ")");

	public String TABLE_REPORT_DETAILS_DROP = String.format("%s %s",
			"DROP TABLE IF EXISTS ", TABLE_REPORT_DETAILS);

	public String TABLE_PERSON_REPORT_DETAILS_DROP = String.format("%s %s",
			"DROP TABLE IF EXISTS ", TABLE_PERSON_REPORT_DETAILS);

	public String TABLE_EXPENSE_DETAILS_DROP = String.format("%s %s",
			"DROP TABLE IF EXISTS ", TABLE_EXPENSE_DETAILS);

	public String TABLE_SHARED_EXPENSE_DETAILS_DROP = String.format("%s %s",
			"DROP TABLE IF EXISTS ", TABLE_SHARED_EXPENSE_DETAILS);
}
