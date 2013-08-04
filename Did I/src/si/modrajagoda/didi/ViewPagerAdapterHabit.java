package si.modrajagoda.didi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

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
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPagerAdapterHabit extends PagerAdapter implements OnClickListener {

	private GraphicalView mChartView;
	
	private Context context;
	private int questionCount;
	private ArrayList<String> habitQuestions;
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
			
			v.setTag(0);
			loadQuestion(v, 0);

			((ViewPager) pager).addView(v, 0);
			return v;
		case 1:

			v = inflater.inflate(viewPagerPageHabit, null);
			v.setTag(1);
			loadQuestion(v, 1);


			((ViewPager) pager).addView(v, 0);
			return v;
		case 2:
			v = inflater.inflate(viewPagerPageHabit, null);
			v.setTag(2);
			loadQuestion(v, 2);


			((ViewPager) pager).addView(v, 0);
			return v;
		case 3:
			v = inflater.inflate(viewPagerPageHabit, null);
			v.setTag(3);
			loadQuestion(v, 3);


			((ViewPager) pager).addView(v, 0);
			return v;
		case 4:
			v = inflater.inflate(viewPagerPageHabit, null);
			v.setTag(4);
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
		
		loadChart(view, question);

	}

	private void loadChart(View view, int question) {
		int[] colors = new int[] {context.getResources().getColor(R.color.positive)};
		String[] titles = new String[] { "Yes" };

		//Get all the days entered so far for this habit
		habit = habits.get(question);
		days = habit.getDays();

		//Fill an array with the answers for those days (true, false) by iterating through all the days
		double[] answersArray = new double[days.size()];
		int count = 0;
		CloseableIterator<Day> daysIterator = days.closeableIterator();
		while(daysIterator.hasNext()) {
			day = daysIterator.next();
			
			if(day.getDayAnswer()==true) {
				answersArray[count] = 1;
			}
			else if(day.getDayAnswer()==false) {
				answersArray[count] = 0;
			}
		
			count = count + 1;
		}
		
		try {
			daysIterator.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		//Calculate the number of weeks entered for the habit so far (the chart will display yes/no's by week)
		//The days()size - 1 is so we get the proper week count (7 days comes out to 2 weeks without this). Good code TM.

		int numberOfWeeks = ((days.size()- 1)/7)+ 1;

		//Make array with the size of the number of weeks + 6 
		//The +6 is to make the chart render properly, for some reason it needs those dummy values. Good code TM.
		
		double[] weeklyYesCount = new double[numberOfWeeks + 6];

		//Get the amount of yes's per week, add it to the array
		for(int i1 = 0; i1 < numberOfWeeks; i1++) {
			int yesCount = 0;

			for (int i2 = 0; i2 < 7; i2++) {
				try {
					
					if(answersArray[i2 + (7*i1)]==1) {
						yesCount = yesCount + 1;
					}
				} catch ( ArrayIndexOutOfBoundsException e ) {
				}
			}
			weeklyYesCount[i1] = yesCount;
			
		}

		List<double[]> values = new ArrayList<double[]>();
		values.add(weeklyYesCount);
		
		//Draw the chart. Every week is displayed as a separate column, for a maximum of 7.
		//If there is data for more than 7 weeks, the last 7 are presented.
		//This felt like a nice amount that gives you perspective on past performance
		//and still keeps every week neatly visible.
		
		int numberOfWeeksOverSeven = 0;
		if (numberOfWeeks>7) {
			numberOfWeeksOverSeven = numberOfWeeks-7;
		}
		
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.chart);
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, 
				"Week", 
				(numberOfWeeksOverSeven + 0.5),
				(numberOfWeeks + 0.5), 
				0, 
				7);
		
		mChartView = ChartFactory.getBarChartView(context, 
				buildBarDataset(titles, values), 
				renderer, 
				Type.DEFAULT);
		
		mChartView.setTag("C" + question);
		
		layout.addView(mChartView);

	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle,
			double xMin, double xMax, double yMin, double yMax) {
		renderer.setXTitle(xTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setShowLegend(false);
		renderer.setShowAxes(false);
		renderer.setShowLabels(false);
		renderer.getSeriesRendererAt(0).setDisplayChartValues(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomButtonsVisible(false);
		renderer.setMarginsColor(Color.argb(0, 1, 1, 1));
		renderer.setBackgroundColor(context.getResources().getColor(R.color.negative));
		renderer.setApplyBackgroundColor(true);
		renderer.setMargins(new int[] {0,0,0,0});
	}

	protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}
	
	@Override
	public void destroyItem( View pager, int position, Object view )
	{
		((ViewPager)pager).removeView( (LinearLayout)view );
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
				reloadChart(0, viewPager);
				
				viewPager.setCurrentItem(1); 
			}
			else if(v.getTag().equals("Q1")) {
				answerQuestion(1, true);
				reloadChart(1, viewPager);

				viewPager.setCurrentItem(2); 
			}
			else if(v.getTag().equals("Q2")) {
				answerQuestion(2, true);
				reloadChart(2, viewPager);
				
				viewPager.setCurrentItem(3); 
			}
			else if(v.getTag().equals("Q3")) {
				answerQuestion(3, true);
				reloadChart(3, viewPager);
				
				viewPager.setCurrentItem(4); 
			}
			else if(v.getTag().equals("Q4")) {
				answerQuestion(4, true);
				reloadChart(4, viewPager);
				
				viewPager.setCurrentItem(4); 
			}

		}

		else if(v.getId()==R.id.button_no) {
			if(v.getTag().equals("Q0")) {
				answerQuestion(0, false);
				reloadChart(0, viewPager);
				
				viewPager.setCurrentItem(1); 
			}
			else if(v.getTag().equals("Q1")) {
				answerQuestion(1, false);
				reloadChart(1, viewPager);
				
				viewPager.setCurrentItem(2); 
			}
			else if(v.getTag().equals("Q2")) {
				answerQuestion(2, false);
				reloadChart(2, viewPager);
				
				viewPager.setCurrentItem(3); 
			}
			else if(v.getTag().equals("Q3")) {
				answerQuestion(3, false);
				reloadChart(3, viewPager);
				
				viewPager.setCurrentItem(4); 
			}
			else if(v.getTag().equals("Q4")) {
				answerQuestion(4, false);
				reloadChart(4, viewPager);
				
				viewPager.setCurrentItem(4); 
			}
		}


	}

	private void reloadChart(int i, ViewPager vp) {

		View questionView = vp.findViewWithTag(i);
		LinearLayout layoutL = (LinearLayout) questionView.findViewById(R.id.chart);
		layoutL.removeAllViews();
		loadQuestion(questionView, i);
		
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
