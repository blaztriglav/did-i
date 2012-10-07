package si.modrajagoda.didi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.CloseableIterable;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import si.modrajagoda.didi.database.DatabaseHelper;
import si.modrajagoda.didi.database.Day;
import si.modrajagoda.didi.database.Habit;
import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPagerAdapterHabit extends PagerAdapter implements OnClickListener {

	private final Context context;
	private final int questionCount;
	private final ArrayList<String> habitQuestions;
	private List<Habit> habits;
	private Habit habit;
	private ForeignCollection<Day> days;
	private Day day;
	private Button buttonYes;
	private Button buttonNo;
	private DatabaseHelper databaseHelper = null;
	
	private Dao<Day, Integer> dayDao = null;

	public ViewPagerAdapterHabit(Context context, int questionCount, ArrayList<String> habitQuestions, List<Habit> habits, DatabaseHelper databaseHelper)
	{
		this.context = context;
		this.questionCount = questionCount;
		this.habitQuestions = habitQuestions;
		this.habits = habits;
		this.databaseHelper = databaseHelper;
		
		try {
			dayDao = databaseHelper.getDayDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCount()
	{
		return questionCount;
	}

	@Override
	public Object instantiateItem( View pager, int position )
	{
		LayoutInflater inflater = (LayoutInflater) pager.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int viewPagerPageHabit = R.layout.view_pager_page_habit_question;
		switch (position) {
		case 0:
			View v = inflater.inflate(viewPagerPageHabit, null);
			loadQuestion(v, 0);

			((ViewPager) pager).addView(v, 0);
			return v;
		case 1:

			v = inflater.inflate(viewPagerPageHabit, null);
			loadQuestion(v, 1);


			((ViewPager) pager).addView(v, 0);
			return v;
		case 2:
			v = inflater.inflate(viewPagerPageHabit, null);
			loadQuestion(v, 2);


			((ViewPager) pager).addView(v, 0);
			return v;
		case 3:
			v = inflater.inflate(viewPagerPageHabit, null);
			loadQuestion(v, 3);


			((ViewPager) pager).addView(v, 0);
			return v;
		case 4:
			v = inflater.inflate(viewPagerPageHabit, null);
			loadQuestion(v, 4);

			((ViewPager) pager).addView(v, 0);
			return v;


		default:
			break;
		}

		View v = inflater.inflate(viewPagerPageHabit, null);

		((ViewPager) pager).addView(v, 0);
		return v;
	}


	private void loadQuestion(View view, int question) {
		TextView textViewQuestion = (TextView) view.findViewById(R.id.text_view_question);
		textViewQuestion.setText(Html.fromHtml(
				context.getResources().getString(R.string.did_i) 
				+ " <b>" + habitQuestions.get(question) + "</b>" 
				+ " ?"));

		buttonYes = (Button) view.findViewById(R.id.button_yes);
		buttonYes.setOnClickListener(this);
		buttonYes.setTag(("Q"+question));

		buttonNo = (Button) view.findViewById(R.id.button_no);
		buttonNo.setOnClickListener(this);
		buttonNo.setTag("Q"+question);

	}

	@Override
	public void destroyItem( View pager, int position, Object view )
	{
		((ViewPager)pager).removeView( (RelativeLayout)view );
	}

	@Override
	public boolean isViewFromObject( View view, Object object )
	{
		return view.equals( object );
	}

	@Override
	public void finishUpdate( View view ) {}


	@Override
	public void restoreState( Parcelable p, ClassLoader c ) {}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate( View view ) {}

	@Override
	public void onClick(View v) {

		ViewPager viewPager = (ViewPager) ((Activity) context).findViewById(R.id.view_pager);


		//Question 1
		if(v.getId()==R.id.button_yes) {
			if(v.getTag().equals("Q0")) {
				
				answerQuestion(0, true);
				viewPager.setCurrentItem(1); 
			}
			else if(v.getTag().equals("Q1")) {
				answerQuestion(1, true);
				viewPager.setCurrentItem(2); 
			}
			else if(v.getTag().equals("Q2")) {
				answerQuestion(2, true);
				viewPager.setCurrentItem(3); 
			}
			else if(v.getTag().equals("Q3")) {
				answerQuestion(3, true);
				viewPager.setCurrentItem(4); 
			}
			else if(v.getTag().equals("Q4")) {
				answerQuestion(4, true);
				viewPager.setCurrentItem(4); 
			}

		}

		else if(v.getId()==R.id.button_no) {
			if(v.getTag().equals("Q0")) {
				answerQuestion(0, false);
				viewPager.setCurrentItem(1); 
			}
			else if(v.getTag().equals("Q1")) {
				answerQuestion(1, false);
				viewPager.setCurrentItem(2); 
			}
			else if(v.getTag().equals("Q2")) {
				answerQuestion(2, false);
				viewPager.setCurrentItem(3); 
			}
			else if(v.getTag().equals("Q3")) {
				answerQuestion(3, false);
				viewPager.setCurrentItem(4); 
			}
			else if(v.getTag().equals("Q4")) {
				answerQuestion(4, false);
				viewPager.setCurrentItem(4); 
			}
		}

	}

	private void answerQuestion(int question, boolean answer) {
		habit = habits.get(question);
		days = habit.getDays();
		
		CloseableIterator<Day> daysIterator = days.closeableIterator();
		while(daysIterator.hasNext()) {
			day = daysIterator.next();
		}
		
		day.setDayAnswer(answer);
		
		try {
			dayDao.update(day);
			daysIterator.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
