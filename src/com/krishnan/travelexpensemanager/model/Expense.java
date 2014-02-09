package com.krishnan.travelexpensemanager.model;

import java.io.InvalidObjectException;
import java.io.Serializable;

/**
 * @author krishm90
 * A POJO to store the expense details
 * Builder-Design pattern
 *
 */
public class Expense implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Report report_Name;
	private String expense_Name;
	private String expense_Date;
	private String expense_Time;

	private Expense() {

	}

	private Float expense_Amount;

	private String expense_Comments;

	private String expense_Type;

	private Person person;

	private int sharing_Count;

	public Report getReport_Name() {
		return report_Name;
	}

	public String getExpense_Name() {
		return expense_Name;
	}

	public String getExpense_Date() {
		return expense_Date;
	}

	public String getExpense_Time() {
		return expense_Time;
	}

	public Float getExpense_Amount() {
		return expense_Amount;
	}

	public String getExpense_Comments() {
		return expense_Comments;
	}

	public String getExpense_Type() {
		return expense_Type;
	}

	public Person getPerson() {
		return person;
	}

	public void setReport_Name(Report report_Name) {
		this.report_Name = report_Name;
	}

	public void setExpense_Name(String expense_Name) {
		this.expense_Name = expense_Name;
	}

	public void setExpense_Date(String expense_Date) {
		this.expense_Date = expense_Date;
	}

	public void setExpense_Time(String expense_Time) {
		this.expense_Time = expense_Time;
	}

	public void setExpense_Amount(Float expense_Amount) {
		this.expense_Amount = expense_Amount;
	}

	public void setExpense_Comments(String expense_Comments) {
		this.expense_Comments = expense_Comments;
	}

	public void setExpense_Type(String expense_Type) {
		this.expense_Type = expense_Type;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public int getSharing_Count() {
		return sharing_Count;
	}

	public void setSharing_Count(int sharing_Count) {
		this.sharing_Count = sharing_Count;
	}

	public static class ExpenseBuilder {
		private Expense expense;

		public ExpenseBuilder() {
			this.expense = new Expense();
		}

		public Expense build() throws InvalidObjectException {
			if (this.expense.getExpense_Amount() != null)
				return expense;
			else
				throw new InvalidObjectException(
						"Cannot create an expense without an amount specified");
		}

		public ExpenseBuilder name(String name) {
			this.expense.setExpense_Name(name);
			return this;
		}

		public ExpenseBuilder date(String date) {
			this.expense.setExpense_Date(date);
			return this;
		}

		public ExpenseBuilder time(String time) {
			this.expense.setExpense_Time(time);
			return this;
		}

		public ExpenseBuilder amount(Float amount) {
			this.expense.setExpense_Amount(amount);
			return this;
		}

		public ExpenseBuilder comments(String comments) {
			this.expense.setExpense_Comments(comments);
			return this;
		}

		public ExpenseBuilder type(String type) {
			this.expense.setExpense_Type(type);
			return this;
		}

		public ExpenseBuilder person(Person p) {
			this.expense.setPerson(p);
			return this;
		}

		public ExpenseBuilder report(Report report) {
			this.expense.setReport_Name(report);
			return this;
		}

		public ExpenseBuilder sharing(int count) {
			this.expense.setSharing_Count(count);
			return this;
		}
	}
}
