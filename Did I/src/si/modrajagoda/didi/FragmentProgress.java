package si.modrajagoda.didi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import si.modrajagoda.didi.database.DatabaseHelper;
import si.modrajagoda.didi.database.Habit;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class FragmentProgress extends Fragment implements OnClickListener, OnPageChangeListener {

	public static final String TYPE = "type";
	private int[] viewIndicators = {R.id.image_view_indicator_1, R.id.image_view_indicator_2, 
			R.id.image_view_indicator_3, R.id.image_view_indicator_4, R.id.image_view_indicator_5};

	View view;

	private ArrayList<String> habitQuestions = null;
	private int questionCount = 0;
	private DatabaseHelper databaseHelper = null;
	private Dao<Habit, Integer> habitDao = null;

	private List<Habit> habits; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_progress, container, false);

		loadUI();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
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
		ViewPagerAdapterProgress adapter = new ViewPagerAdapterProgress(getActivity(), questionCount, habitQuestions, habits, databaseHelper);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		pager.setCurrentItem(0);


	}



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

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


}
