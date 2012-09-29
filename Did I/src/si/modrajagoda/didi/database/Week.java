package si.modrajagoda.didi.database;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "weeks")
public class Week {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(columnName = "week_number")
	private int weekNumber;
	
	@ForeignCollectionField(columnName = "week_details")
	ForeignCollection<WeekDetails> weekDetails;
	
	@DatabaseField(foreign = true)
	private Habit habit;
	
	public Week(){}
	
	public Week(int weekNumber, Habit habit){
		this.weekNumber = weekNumber;
		this.habit = habit;
	}
}
