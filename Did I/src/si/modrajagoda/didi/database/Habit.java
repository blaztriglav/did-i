package si.modrajagoda.didi.database;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "habits")
public class Habit {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField()
	private String name;
	
	@DatabaseField()
	private boolean archived;
	
	@ForeignCollectionField
	ForeignCollection<Week> weeks;
	
	public Habit() {
		// ORMLite needs a no-arg constructor
	}
	
	public Habit(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public ForeignCollection<Week> getWeeks(){
		return weeks;
	}
}
