package si.modrajagoda.didi.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Habit {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField()
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
	
	@DatabaseField
	private boolean archived;
	
	public Habit() {
		// ORMLite needs a no-arg constructor
	}
}
