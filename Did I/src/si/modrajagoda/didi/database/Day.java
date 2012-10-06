package si.modrajagoda.didi.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "days")
public class Day {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(columnName = "day_number")
	private int dayNumber;
	
	@DatabaseField(columnName = "day_answer")
	private boolean dayAnswer;
	
	@DatabaseField(foreign = true)
	private Habit habit;
	
	public Day(){}
	
	public Day(Habit habit, int dayNumber, boolean questionAnswer){
		this.habit = habit;
		this.dayNumber = dayNumber;
		this.dayAnswer = questionAnswer;
	}
	
	public int getDayNumber(){
		return dayNumber;
	}
	
	public void setDayNumber(int dayNumber){
		this.dayNumber = dayNumber;
	}
	
	public boolean getDayAnswer(){
		return dayAnswer;
	}
	
	public void setDayAnswer(boolean dayAnswer){
		this.dayAnswer = dayAnswer;
	}
	
}
