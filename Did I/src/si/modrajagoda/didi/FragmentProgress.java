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
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentProgress extends Fragment implements OnClickListener {

	public static final String TYPE = "type";

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_progress, container, false);
		
        ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);

		return view;
	}

	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
