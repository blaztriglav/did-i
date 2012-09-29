package si.modrajagoda.didi;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerAdapter extends PagerAdapter {

	private GraphicalView mChartView;

	private final Context context;

	public ViewPagerAdapter( Context context )
	{
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return 4;
	}

	@Override
	public Object instantiateItem( View pager, int position )
	{
		LayoutInflater inflater = (LayoutInflater) pager.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int habit_number = 0;

		switch (position) {
		case 0: 

			habit_number = R.layout.habit1;
			View v = inflater.inflate(habit_number, null);
			loadChart(v);


			((ViewPager) pager).addView(v, 0);
			return v;

		case 1: 

			habit_number = R.layout.habit1;
			v = inflater.inflate(habit_number, null);
			loadChart(v);


			((ViewPager) pager).addView(v, 0);
			return v;
			
		case 2: 

			habit_number = R.layout.habit1;
			v = inflater.inflate(habit_number, null);
			loadChart(v);


			((ViewPager) pager).addView(v, 0);
			return v;

		case 3: 

			habit_number = R.layout.habit1;
			v = inflater.inflate(habit_number, null);
			loadChart(v);


			((ViewPager) pager).addView(v, 0);
			return v;
		}

		View v = inflater.inflate(habit_number, null);

		((ViewPager) pager).addView(v, 0);
		return v;
	}

	private void loadChart(View view) {
		int[] colors = new int[] {context.getResources().getColor(R.color.negative), context.getResources().getColor(R.color.positive)};
		String[] titles = new String[] { "Yes", "No" };
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 7, 7, 7, 7, 7, 7, 7 });
		values.add(new double[] { 1, 4, 6, 7, 6, 7, 7 });

		LinearLayout layout = (LinearLayout) view.findViewById(R.id.chart);
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Week", 0.5,
				7.5, 0, 7);
		renderer.getSeriesRendererAt(0).setDisplayChartValues(false);
		renderer.getSeriesRendererAt(1).setDisplayChartValues(false);
		renderer.setPanEnabled(false, false);
		mChartView = ChartFactory.getBarChartView(context, buildBarDataset(titles, values), renderer, Type.STACKED);
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
		renderer.setBackgroundColor(Color.argb(0, 1, 1, 1));
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
