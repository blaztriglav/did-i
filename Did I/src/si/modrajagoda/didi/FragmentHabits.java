package si.modrajagoda.didi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import si.modrajagoda.didi.database.DatabaseHelper;
import si.modrajagoda.didi.database.Habit;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class FragmentHabits extends Fragment implements OnClickListener, OnPageChangeListener {

	private View view;
	private int count = 1;
	private int[] viewIndicators = {R.id.image_view_indicator_1, R.id.image_view_indicator_2, 
			R.id.image_view_indicator_3, R.id.image_view_indicator_4, R.id.image_view_indicator_5};
	
	private ArrayList<String> habitQuestions = null;
	private int questionCount = 0;
	private DatabaseHelper databaseHelper = null;
	private Dao<Habit, Integer> habitDao = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_habits, container, false);
		
		// Get all the habits from the database
		databaseHelper = getHelper();
		habitQuestions = new ArrayList<String>();
		try {
			habitDao = databaseHelper.getHabitDao();
			List<Habit> habits = habitDao.queryForAll();
			questionCount = habits.size();
			for(Habit habit : habits){
				habitQuestions.add(habit.getName());
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
		
		ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
		ViewPagerAdapterHabit adapter = new ViewPagerAdapterHabit(getActivity(), questionCount, habitQuestions);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		pager.setCurrentItem(0);


		return view;
	}

	@Override
	public void onClick(View v) {

//		if(v.getId()==R.id.button_yes) {
//			if(count != questionCount){
//				setIndicatorAndQuestion(true, count, habitQuestions.get(count));
//			} else {
//				ImageView indicator = (ImageView) view.findViewById(viewIndicators[count-1]);
//				indicator.setImageResource(R.drawable.indicator_positive);
//			}
//		} else if(v.getId()==R.id.button_no) {
//			if(count != questionCount){
//				setIndicatorAndQuestion(false, count, habitQuestions.get(count));
//			} else {
//				ImageView indicator = (ImageView) view.findViewById(viewIndicators[count-1]);
//				indicator.setImageResource(R.drawable.indicator_negative);
//			}
//		} 
	}
	
	private void setIndicatorAndQuestion(boolean answer, int currentPosition, String nextHabit){
		ImageView thisIndicator = (ImageView) view.findViewById(viewIndicators[currentPosition-1]);
		thisIndicator.setImageResource(answer ? R.drawable.indicator_positive : R.drawable.indicator_negative);
		ImageView nextIndicator = (ImageView) view.findViewById(viewIndicators[currentPosition]);
		nextIndicator .setImageResource(R.drawable.indicator_neutral_selected);
		TextView textViewHabit = (TextView) view.findViewById(R.id.text_view_question);
		textViewHabit.setText(nextHabit);
		count += 1;
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
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}

}
