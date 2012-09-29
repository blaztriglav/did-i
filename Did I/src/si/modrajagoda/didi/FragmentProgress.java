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

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentProgress extends Fragment implements OnClickListener {

	public static final String TYPE = "type";

	private GraphicalView mChartView;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_progress, container, false);
		loadChart();

		return view;
	}

	private void loadChart() {
		int[] colors = new int[] {getResources().getColor(R.color.negative), getResources().getColor(R.color.positive)};
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
		mChartView = ChartFactory.getBarChartView(getActivity(), buildBarDataset(titles, values), renderer, Type.STACKED);
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
