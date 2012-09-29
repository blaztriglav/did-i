package si.modrajagoda.didi.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "week_details")
public class WeekDetails {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private boolean day0;
	
	@DatabaseField
	private boolean day1;
	
	@DatabaseField
	private boolean day2;
	
	@DatabaseField
	private boolean day3;
	
	@DatabaseField
	private boolean day4;
	
	@DatabaseField
	private boolean day5;
	
	@DatabaseField
	private boolean day6;
	
	@DatabaseField(foreign = true)
	private Week week;
	
	public WeekDetails(){}
	
	public WeekDetails(Week week){
		this.week = week;
	}
}
