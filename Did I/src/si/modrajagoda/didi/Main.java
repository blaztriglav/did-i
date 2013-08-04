package si.modrajagoda.didi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import si.modrajagoda.didi.database.DatabaseHelper;
import si.modrajagoda.didi.database.Day;
import si.modrajagoda.didi.database.Habit;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends FragmentActivity implements OnPageChangeListener {

	private int[] viewIndicators = {R.id.image_view_indicator_1, R.id.image_view_indicator_2, 
			R.id.image_view_indicator_3, R.id.image_view_indicator_4, R.id.image_view_indicator_5};

	private ArrayList<String> habitQuestions = null;
	private int questionCount = 0;
	private List<Habit> habits;
	private DatabaseHelper databaseHelper = null;
	private Dao<Habit, Integer> habitDao = null;
	private Dao<Day, Integer> dayDao = null;

	private Habit habit;
	private ForeignCollection<Day> days;
	private Day day;

	private SharedPreferences settings;
	private Calendar date;
	private int currentDay;
	private int lastDayOfEntry;
	private String LAST_DAY_OF_ENTRY = "LAST_DAY_OF_ENTRY";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		settings = PreferenceManager.getDefaultSharedPreferences(this);
		databaseHelper = getHelper();
	}

	@Override
	public void onResume() {
		super.onResume();

		getHabits();
		addDays();
		loadIndicators();
		loadViewPager();
		//	loadDummyValues();

	}

	/** Gets all active habits from the database.
	 * 
	 */
	private void getHabits() {
		
		habitQuestions = new ArrayList<String>();

		try {
			habitDao = databaseHelper.getHabitDao();
			habits = habitDao.queryForAll();
			questionCount = habits.size();

			for(Habit habit : habits){
				habitQuestions.add(habit.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/** Adds a day or a number of days to the habits in the database.
	 * 	Probably the worst written method in the codebase. No use in
	 * 	explaining, this entire part needs to be rewritten in the near
	 * 	future, along with the underlying database structure.
	 */
	private void addDays() {
		date = Calendar.getInstance();
		currentDay = date.get(Calendar.DAY_OF_YEAR);

		lastDayOfEntry = settings.getInt(LAST_DAY_OF_ENTRY, 0);

		try {
			dayDao = databaseHelper.getDayDao();

			for(int i = 0; i < questionCount; i++) {

				habit = habits.get(i);
				days = habit.getDays();

				if(lastDayOfEntry != 0) {

					
					if(days.size()==0) {

						day = new Day(habit, days.size()+1, false);
						dayDao.create(day);
					}
					
					if(currentDay - lastDayOfEntry > 0) {

						for (int i2 = 0; i2 < (currentDay-lastDayOfEntry); i2++) {
							day = new Day(habit, days.size()+1, false);
							dayDao.create(day);
						}
					}
					
					else if(currentDay - lastDayOfEntry < 0) {
						day = new Day(habit, days.size()+1, false);
						dayDao.create(day);
					}

				}

				else {
					day = new Day(habit, days.size()+1, false);
					dayDao.create(day);

				}
				
			}
			
			settings.edit().putInt(LAST_DAY_OF_ENTRY, currentDay).commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**	Load the view pager UI with the currently active habits.
	 * 
	 */
	private void loadViewPager() {
		ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
		ViewPagerAdapterHabit adapter = new ViewPagerAdapterHabit(this, questionCount, habitQuestions, habits, databaseHelper);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		onPageSelected(pager.getCurrentItem());
	}

	/**	Load the indicators UI, with the proper number of indicators
	 * 	based on currently active habits.
	 */
	private void loadIndicators() {
		ImageView indicator;
		
		for(int i = 0; i < questionCount; i++) {
			indicator = (ImageView) findViewById(viewIndicators[i]);
			indicator.setVisibility(View.VISIBLE);
		}
		
		for(int i = questionCount; i < viewIndicators.length; i++) {
			indicator = (ImageView) findViewById(viewIndicators[i]);
			indicator.setVisibility(View.GONE);
		}
		
		if(questionCount!=0) {
			TextView textViewNoHabits = (TextView) findViewById(R.id.text_view_no_habits);
			textViewNoHabits.setVisibility(View.GONE);
		}
		
		else {
			TextView textViewNoHabits = (TextView) findViewById(R.id.text_view_no_habits);
			textViewNoHabits.setVisibility(View.VISIBLE);
		}
		
	}

	/** Add a day with a random yes/no value to the first habit
	 * 	in the database. This method is for testing purposes,
	 * 	and is not used in production.
	 */
	private void loadDummyValues() {

		try {
			dayDao = databaseHelper.getDayDao();
			habit = habits.get(0);

			day = new Day(habit, days.size()+1, (Math.random() < 0.5));
			dayDao.create(day);
			days = habit.getDays();


		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper =
					OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		return databaseHelper;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int page) {

		ImageView imageViewIndicator = (ImageView) findViewById(viewIndicators[page]);
		imageViewIndicator.setImageResource(R.drawable.indicator_neutral_selected);

		if(page > 0 ) {
			imageViewIndicator = (ImageView) findViewById(viewIndicators[page-1]);
			imageViewIndicator.setImageResource(R.drawable.indicator_neutral);
		}
		if(page < (viewIndicators.length)-1) {
			imageViewIndicator = (ImageView) findViewById(viewIndicators[page+1]);
			imageViewIndicator.setImageResource(R.drawable.indicator_neutral);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, EditHabits.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
