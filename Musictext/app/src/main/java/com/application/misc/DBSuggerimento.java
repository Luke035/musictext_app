package com.application.misc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DBSuggerimento {
	private List<DBTag> tagSuggeriti;
	
	public DBSuggerimento(){
		tagSuggeriti = new ArrayList<DBTag>();
	}

	public List<DBTag> getTagSuggeriti() {
		return tagSuggeriti;
	}

	public void addTag(DBTag sugg) {
		this.tagSuggeriti.add(sugg);
	}

	@Override
	public String toString() {
		String s = "DBSuggerimento{" +
				"tagSuggeriti=" + tagSuggeriti+", ";

		for(DBTag tag:tagSuggeriti){
			s += tag.toString()+", ";
		}

		s+= '}';

		return s;
	}
}
