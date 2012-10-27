package si.modrajagoda.didi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import si.modrajagoda.didi.database.DatabaseHelper;
import si.modrajagoda.didi.database.Day;
import si.modrajagoda.didi.database.Habit;
import android.R.color;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

public class EditHabits extends FragmentActivity implements OnItemClickListener, OnClickListener{

	private static final String MINUTES = "minutes";
	private static final String HOURS = "hours";

	private ArrayAdapter<String> adapter;
	private ArrayList<String> habitQuestions = null;
	private DatabaseHelper databaseHelper = null;
	private Dao<Habit, Integer> habitDao = null;
	private TextView noHabits;
	private ListView list;
	private List<Habit> habits;
	private Habit habit;
	private Habit lastHabit;
	private Button timePickerButton;
	private SharedPreferences settings;
	private int minutes;
	private int hours;
	private ArrayList<Habit> habitsToDelete;

	private ForeignCollection<Day> days;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.edit_habits);
		setContentView(R.layout.activity_edit_habits);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		databaseHelper = getHelper();
		habitQuestions = new ArrayList<String>(); 
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		minutes = settings.getInt(MINUTES, 0);
		hours = settings.getInt(HOURS, 22);
		habitsToDelete = new ArrayList<Habit>();

		loadUI();

	}

	private void loadUI() {
		noHabits = (TextView) findViewById(R.id.no_habits);
		View bottomDivider = findViewById(R.id.bottom_divider);
		list = (ListView) findViewById(android.R.id.list);

		try {
			habitDao = databaseHelper.getHabitDao();
			habits = habitDao.queryForAll();
			if(habits.size() > 0){
				getHabitQuestions();
			} else {
				noHabits.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
				bottomDivider.setVisibility(View.GONE);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}


		adapter = new CustomAdapter(this, R.layout.list_item_habit, habitQuestions);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		list.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// Here you can perform updates to the CAB due to
				// an invalidate() request
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// Here you can make any necessary updates to the activity when
				// the CAB is removed. By default, selected items are deselected/unchecked.
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// Inflate the menu for the CAB
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.context, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				//				SparseBooleanArray checkedItems = list.getCheckedItemPositions();
				for (int i = 0; i < habitsToDelete.size(); i++) {
					switch (item.getItemId()) {
					case R.id.menu_delete:
						try {
							deleteHabit(habitsToDelete.get(i));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						// Update list
						adapter.notifyDataSetChanged();
						break;
					}
				}

				mode.finish(); // Action picked, so close the CAB
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position,
					long id, boolean checked) {
				mode.setTitle(getString(R.string.selected_items)+" "+
						Integer.toString(list.getCheckedItemCount()));
				if(checked){
					habitsToDelete.add(habits.get(position));
					list.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.positive));
				}
				if(!checked){
					habitsToDelete.remove(habits.get(position));
					list.getChildAt(position).setBackgroundColor(color.transparent);
				}
			}
		});

		timePickerButton = (Button) findViewById(R.id.time_picker);
		timePickerButton.setOnClickListener(this);
		int minutes = settings.getInt(MINUTES, 0);
		int hours = settings.getInt(HOURS, 22);
		if(minutes < 10){
			timePickerButton.setText(Integer.toString(hours)+":0"+Integer.toString(minutes));
		} else {
			timePickerButton.setText(Integer.toString(hours)+":"+Integer.toString(minutes));
		}

	}

	private class CustomAdapter extends ArrayAdapter<String> {
		private Context mContext;
		private List<String> habitQuestions;

		public CustomAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			this.mContext = context;
			this.habitQuestions = objects;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unnecessary calls to findViewById() on each row.
			ViewHolder holder;
			LayoutInflater li = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// When convertView is not null, we can reuse it directly, there is
			// no need to reinflate it. We only inflate a new View when the 
			// convertView supplied by ListView is null.
			if (convertView == null) {
				convertView = li.inflate(R.layout.list_item_habit, null);

				// Creates a ViewHolder and store references to children views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.habitNumber = (TextView) convertView
						.findViewById(R.id.question_number);
				holder.habitName = (TextView) convertView
						.findViewById(R.id.habit_name);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView
				holder = (ViewHolder) convertView.getTag();
			}

			holder.habitNumber.setText(Integer.toString(position + 1)+".");
			holder.habitName.setText(habitQuestions.get(position));

			return convertView;
		}
	}

	// ViewHolder for the efficient adapter
	private static class ViewHolder {
		TextView habitNumber;
		TextView habitName;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_new:
			if(habitQuestions.size() < 5){

				if(habits.isEmpty()) {
					habit = new Habit(0, "");
				}

				else {

					ListIterator<Habit> habitIterator = habits.listIterator();
					while(habitIterator.hasNext()){
						lastHabit = habitIterator.next();
					}

					habit = new Habit(lastHabit.getID()+1, "");
				}

				showDialog("");
			}
			else {
				Toast.makeText(this, "Easy there, tiger. 5 habits is the limit.", Toast.LENGTH_SHORT).show();
			}
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void deleteHabit(Habit habitToDelete) throws SQLException{
		Dao<Day, Integer> dayDao = databaseHelper.getDayDao();
		habit = habitToDelete;

		// Delete days for this habit
		days = habit.getDays();
		if(days != null){
			CloseableIterator<Day> dayIterator = days.closeableIterator();
			while(dayIterator.hasNext()){
				Day day = dayIterator.next();
				dayDao.delete(day);
			} 
			dayIterator.close();
		}

		// Delete habit
		habitDao.delete(habit);

		// Update table
		loadUI();


	}

	@Override
	protected void onDestroy() {
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		habit = habits.get(position);
		showDialog(habit.getName());
	}


	void showDialog(String habitName) {
		DialogFragment newFragment = DialogEditHabit.newInstance(habitName);
		newFragment.show(getSupportFragmentManager(), "dialog");
	}

	public void doPositiveClick(String habitName) { 
		habit.setName(habitName);
		try {
			habitDao.createOrUpdate(habit);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Update list
		loadUI();
	}

	private void getHabitQuestions(){
		noHabits.setVisibility(View.GONE);
		View bottomDivider = findViewById(R.id.bottom_divider);
		bottomDivider.setVisibility(View.VISIBLE);
		list.setVisibility(View.VISIBLE);
		habitQuestions.clear();
		List<Habit> habits;
		try {
			habits = habitDao.queryForAll();
			for(Habit habit : habits){
				habitQuestions.add(habit.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.time_picker:
			new TimePickerDialog(this, mTimeListener, hours, minutes, true).show();
			break;
		}
	}

	private TimePickerDialog.OnTimeSetListener mTimeListener =
			new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hour, int minute) {
			minutes = minute;
			hours = hour;
			if(minute < 10){
				timePickerButton.setText(Integer.toString(hour)+":0"+Integer.toString(minute));
			} else {
				timePickerButton.setText(Integer.toString(hour)+":"+Integer.toString(minute));
			}
			settings.edit().putInt(MINUTES, minute).putInt(HOURS, hour).commit();


			setRecurringAlarm(EditHabits.this, hour, minute);

		}
	};

	//Set up a recurring alarm to fire a notification for the user (to fill our answers for the day)
	//Will fire every day at the specified time.
	private void setRecurringAlarm(Context context, int hour, int minute) {

		AlarmManager alarms = (AlarmManager) getSystemService(
				Context.ALARM_SERVICE);

		Calendar reportTime = Calendar.getInstance();
		reportTime.set(Calendar.HOUR_OF_DAY, hour);
		reportTime.set(Calendar.MINUTE, minute);
		reportTime.set(Calendar.SECOND, 0);
		Intent notification = new Intent(context, AlarmReceiver.class);
		PendingIntent recurringAlarm = PendingIntent.getBroadcast(context,
				0, notification, PendingIntent.FLAG_CANCEL_CURRENT);

		alarms.setRepeating(AlarmManager.RTC, reportTime.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, recurringAlarm);

	}
}
