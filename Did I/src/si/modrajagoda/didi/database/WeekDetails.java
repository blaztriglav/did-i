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
	
	public boolean getDay0(){
		return day0;
	}
	
	public boolean getDay1(){
		return day1;
	}
	
	public boolean getDay2(){
		return day2;
	}
	
	public boolean getDay3(){
		return day3;
	}
	
	public boolean getDay4(){
		return day4;
	}
	
	public boolean getDay5(){
		return day5;
	}
	
	public boolean getDay6(){
		return day6;
	}
	
	public void setDay0(boolean completedTask){
		this.day0 = completedTask;
	}
	
	public void setDay1(boolean completedTask){
		this.day1 = completedTask;
	}
	
	public void setDay2(boolean completedTask){
		this.day2 = completedTask;
	}
	
	public void setDay3(boolean completedTask){
		this.day3 = completedTask;
	}
	
	public void setDay4(boolean completedTask){
		this.day4 = completedTask;
	}
	
	public void setDay5(boolean completedTask){
		this.day5 = completedTask;
	}
	
	public void setDay6(boolean completedTask){
		this.day6 = completedTask;
	}
}
