package com.application.misc;

public class DBOsservazione {
	private String name;
	private String value;
	
	public DBOsservazione(){ }
	
	public DBOsservazione(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "DBOsservazione{" +
				"name='" + name + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
