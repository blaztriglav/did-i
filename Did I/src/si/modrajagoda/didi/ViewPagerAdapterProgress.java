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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import si.modrajagoda.didi.database.DatabaseHelper;
import si.modrajagoda.didi.database.Day;
import si.modrajagoda.didi.database.Habit;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerAdapterProgress extends PagerAdapter {

	private GraphicalView mChartView;

	private final Context context;
	private final int questionCount;
	private final ArrayList<String> habitQuestions;
	private List<Habit> habits;
	private Habit habit;
	private ForeignCollection<Day> days;
	private Day day;
	private DatabaseHelper databaseHelper = null;

	private Dao<Day, Integer> dayDao = null;

	public ViewPagerAdapterProgress(Context context, int questionCount, ArrayList<String> habitQuestions, List<Habit> habits, DatabaseHelper databaseHelper)
	{
		this.context = context;
		this.questionCount = questionCount;
		this.habitQuestions = habitQuestions;
		this.habits = habits;

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

		int viewPagerPage = R.layout.view_pager_page_progress_question;
		switch (position) {
		case 0:
			View v = inflater.inflate(viewPagerPage, null);
			loadQuestion(v, 0);

			((ViewPager) pager).addView(v, 0);
			return v;
		case 1:

			v = inflater.inflate(viewPagerPage, null);
			loadQuestion(v, 1);


			((ViewPager) pager).addView(v, 0);
			return v;
		case 2:
			v = inflater.inflate(viewPagerPage, null);
			loadQuestion(v, 2);


			((ViewPager) pager).addView(v, 0);
			return v;
		case 3:
			v = inflater.inflate(viewPagerPage, null);
			loadQuestion(v, 3);


			((ViewPager) pager).addView(v, 0);
			return v;
		case 4:
			v = inflater.inflate(viewPagerPage, null);
			loadQuestion(v, 4);

			((ViewPager) pager).addView(v, 0);
			return v;


		default:
			break;
		}

		View v = inflater.inflate(viewPagerPage, null);

		((ViewPager) pager).addView(v, 0);
		return v;
	}


	private void loadQuestion(View view, int question) {
		TextView textViewQuestion = (TextView) view.findViewById(R.id.text_view_question);
		textViewQuestion.setText(Html.fromHtml(
				context.getResources().getString(R.string.did_i) 
				+ " <b>" + habitQuestions.get(question) + "</b>" 
				+ " ?"));

		loadChart(view, question);
	}
	private void loadChart(View view, int question) {
		int[] colors = new int[] {context.getResources().getColor(R.color.positive)};
		String[] titles = new String[] { "Yes" };

		habit = habits.get(question);
		days = habit.getDays();

		double[] answersArray = new double[days.size()];
		for(int i = 0; i < days.size(); i++) {

			QueryBuilder<Day, Integer> builder = dayDao.queryBuilder();
			Where<Day, Integer> where = builder.where();
			try {
				where.eq("day_number", i+1);
				day = dayDao.query(builder.prepare()).get(0);
				day.getDayAnswer();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch ( ArrayIndexOutOfBoundsException e ) {
				Log.d("Error", "Array out of bounds");
			}

			if(day.getDayAnswer()==true) {
				answersArray[i] = 1;
			}
			else if(day.getDayAnswer()==false) {
				answersArray[i] = 0;
			}

		}

		int numberOfWeeks = (days.size()/7) + 1;
		Log.d("WEEKNUM", "Number of weeks: " + numberOfWeeks);

		double[] weeklyYesCount = new double[numberOfWeeks];
		double[] anArrayOfSevens = new double[numberOfWeeks];

		for(int i1 = 0; i1 < numberOfWeeks; i1++) {
			int yesCount = 0;
			Log.d("DOIRUN", "Yes count: " + yesCount);

			for (int i2 = 0; i2 < 6; i2++) {
				try {
					if(answersArray[i2*(i1+1)]==1) {
						yesCount = yesCount + 1;
						Log.d("Yescount", "Yes count: " + yesCount);
					}
				} catch ( ArrayIndexOutOfBoundsException e ) {
					Log.d("Error", "Array out of bounds");
				}
			}
			weeklyYesCount[i1] = yesCount;
			anArrayOfSevens[i1] = 7;
		}

		

		List<double[]> values = new ArrayList<double[]>();
		
//		List<double[]> values = new ArrayList<double[]>();
////		values.add(new double[] {7, 7, 7, 7, 7, 7, 7});
//		values.add(new double[] {6, 7, 6, 6, 5, 1, 2});

//		values.add(anArrayOfSevens);
		values.add(weeklyYesCount);
		
		

		Log.d("ARRAYS", "Arrays: " + weeklyYesCount.length + " " + anArrayOfSevens.length + " " + values.size());
		Log.d("calc", "calc: " + (numberOfWeeks + 0.5));
		
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.chart);
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Week", 0.5,
				1.5, 0, 7);
		renderer.getSeriesRendererAt(0).setDisplayChartValues(false);
		renderer.setPanEnabled(false, false);
		mChartView = ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer, Type.DEFAULT);
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
		renderer.setZoomButtonsVisible(false);
		renderer.setMarginsColor(Color.argb(0, 1, 1, 1));
		renderer.setBackgroundColor(context.getResources().getColor(R.color.negative));
		renderer.setApplyBackgroundColor(true);
		renderer.setMargins(new int[] {0,0,0,0});
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
}
