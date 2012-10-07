package si.modrajagoda.didi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import si.modrajagoda.didi.database.DatabaseHelper;
import si.modrajagoda.didi.database.Habit;
import si.modrajagoda.didi.database.Day;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EditHabits extends FragmentActivity implements OnItemClickListener{
	
	private ArrayAdapter<String> adapter;
	private ArrayList<String> habitQuestions = null;
	private DatabaseHelper databaseHelper = null;
	private Dao<Habit, Integer> habitDao = null;
	private TextView noHabits;
	private ListView list;
	private Habit habit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.edit_habits);
		setContentView(R.layout.edit_habits);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		databaseHelper = getHelper();
		habitQuestions = new ArrayList<String>(); 
		
		noHabits = (TextView) findViewById(R.id.no_babits);
		try {
			habitDao = databaseHelper.getHabitDao();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			List<Habit> habits = habitDao.queryForAll();
			if(habits.size() > 0){
				getHabitQuestions();
			} else {
				noHabits.setVisibility(View.VISIBLE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		list = (ListView) findViewById(android.R.id.list);
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
				SparseBooleanArray checkedItems = list.getCheckedItemPositions();
                for (int i = 0; i < checkedItems.size(); i++) {
                	if(checkedItems.valueAt(i)){
                		int habit = checkedItems.keyAt(i);
	                	switch (item.getItemId()) {
						case R.id.menu_delete:
							try {
								deleteHabit(habit);
							} catch (SQLException e) {
								e.printStackTrace();
							}
							// Update list
							adapter.notifyDataSetChanged();
							break;
						}
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
					// TODO set background
				}
			}
		});
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
				habit = new Habit(habitQuestions.size()+1, "");
				showDialog("");
			}
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void deleteHabit(int id) throws SQLException{
		Dao<Day, Integer> dayDao = databaseHelper.getDayDao();
		Habit habit = habitDao.queryForId(id+1);
		
		// Delete days for this habit
		ForeignCollection<Day> days = habit.getDays();
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
		getHabitQuestions();
		for(int i = 0; i < habitQuestions.size(); i++){
			String question = habitQuestions.get(i);
			QueryBuilder<Habit, Integer> builder = habitDao.queryBuilder();
			Where<Habit, Integer> where = builder.where();
			where.eq("name", question);
			List<Habit> list = habitDao.query(builder.prepare());
			if(list.size() == 1){
				Habit habit1 = list.get(0);
				habitDao.updateId(habit1, i);
			}
		}
		
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
		try {
			habit = habitDao.queryForId(position+1);
			showDialog(habit.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		adapter.clear();
		getHabitQuestions();
		adapter.notifyDataSetChanged();
		
		// Hide no habits text if needed
		if(habitQuestions.size() > 0){
			noHabits.setVisibility(View.GONE);
		} else {
			noHabits.setVisibility(View.VISIBLE);
		}
	}
	
	private void getHabitQuestions(){
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
	
}
