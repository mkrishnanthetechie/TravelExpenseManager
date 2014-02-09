package com.krishnan.travelexpensemanager.model;

import java.io.Serializable;

/**
 * @author krishm90
 * @date 09/02/2014
 * POJO to store person details
 *
 */
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	private String personName;

	public Person(String name) {
		this.personName = name;
	}

	public String getPersonName() {
		return this.personName;
	}

	public void setPersonName(String name) {
		this.personName = name;
	}

	@Override
	public String toString() {
		return personName;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o == null)
			return false;
		else {
			Person p = (Person) o;
			return (this.personName.equalsIgnoreCase(p.personName));
		}
	}

}
