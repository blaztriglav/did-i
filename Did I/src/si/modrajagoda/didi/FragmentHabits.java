package si.modrajagoda.didi;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import si.modrajagoda.didi.database.DatabaseHelper;
import si.modrajagoda.didi.database.Day;
import si.modrajagoda.didi.database.Habit;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

public class FragmentHabits extends Fragment implements OnClickListener, OnPageChangeListener {

	private View view;
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

	private static SharedPreferences settings;
	private Calendar date;
	private int currentDay;
	private int lastDayOfEntry;
	private String LAST_DAY_OF_ENTRY = "LAST_DAY_OF_ENTRY";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_habits, container, false);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("IRAN", "RAN SO FAR AWAY");
		loadUI();
	}


	private void loadUI() {
		// Get all the habits from the database
		databaseHelper = getHelper();
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

		// Check the date, add no's for unanswered days in between last check-in

		settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

		date = Calendar.getInstance();
		currentDay = date.get(Calendar.DAY_OF_YEAR);

		lastDayOfEntry = settings.getInt(LAST_DAY_OF_ENTRY, 0);

		try {
			dayDao = databaseHelper.getDayDao();

			for(int i = 0; i < questionCount; i++) {

				habit = habits.get(i);
				days = habit.getDays();

				if(lastDayOfEntry != 0) {

					//If this is a newly added question, a day needs to be added either way
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
					
					settings.edit().putInt(LAST_DAY_OF_ENTRY, currentDay).commit();

				}

				else {
					day = new Day(habit, days.size()+1, false);
					dayDao.create(day);

					settings.edit().putInt(LAST_DAY_OF_ENTRY, currentDay).commit();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Dynamically display number of habits indicator
		ImageView indicator;
		switch (questionCount) {
		case 5:
			indicator = (ImageView) view.findViewById(R.id.image_view_indicator_5);
			indicator.setVisibility(View.VISIBLE);
		case 4:
			indicator = (ImageView) view.findViewById(R.id.image_view_indicator_4);
			indicator.setVisibility(View.VISIBLE);
		case 3:
			indicator = (ImageView) view.findViewById(R.id.image_view_indicator_3);
			indicator.setVisibility(View.VISIBLE);
		case 2:
			indicator = (ImageView) view.findViewById(R.id.image_view_indicator_2);
			indicator.setVisibility(View.VISIBLE);
		case 1:
			indicator = (ImageView) view.findViewById(R.id.image_view_indicator_1);
			indicator.setVisibility(View.VISIBLE);
			TextView textViewNoHabits = (TextView) view.findViewById(R.id.text_view_no_habits);
			textViewNoHabits.setVisibility(View.GONE);
			break;

		default:
			break;
		}

		//	loadDummyValues();

		ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
		ViewPagerAdapterHabit adapter = new ViewPagerAdapterHabit(getActivity(), questionCount, habitQuestions, habits, databaseHelper);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		pager.setCurrentItem(0);

	}

	public static long daysBetween(Calendar lastDayOfEntry, Calendar currentDay) {  
		Calendar date = (Calendar) lastDayOfEntry.clone();  
		long daysBetween = 0;  
		while (date.before(currentDay)) {  
			date.add(Calendar.DAY_OF_MONTH, 1);  
			daysBetween++;  
		}  
		return daysBetween;  
	}  

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
	public void onClick(View v) {

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
					OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
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

		ImageView imageViewIndicator = (ImageView) view.findViewById(viewIndicators[page]);
		imageViewIndicator.setImageResource(R.drawable.indicator_neutral_selected);

		if(page > 0 ) {
			imageViewIndicator = (ImageView) view.findViewById(viewIndicators[page-1]);
			imageViewIndicator.setImageResource(R.drawable.indicator_neutral);
		}
		if(page < (viewIndicators.length)-1) {
			imageViewIndicator = (ImageView) view.findViewById(viewIndicators[page+1]);
			imageViewIndicator.setImageResource(R.drawable.indicator_neutral);
		}

	}

}
