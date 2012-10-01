package si.modrajagoda.didi;

import java.lang.reflect.Array;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentProgress extends Fragment implements OnClickListener, OnPageChangeListener {

	public static final String TYPE = "type";
	private int[] viewIndicators = {R.id.image_view_indicator_1, R.id.image_view_indicator_2, 
			R.id.image_view_indicator_3, R.id.image_view_indicator_4, R.id.image_view_indicator_5};

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_progress, container, false);

		ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
		ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		pager.setCurrentItem(0);

		return view;
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
	public void onPageSelected(int arg0) {
		ImageView imageViewIndicator = (ImageView) view.findViewById(viewIndicators[arg0]);
		imageViewIndicator.setImageResource(R.drawable.indicator_neutral_selected);

		if(arg0 > 0 ) {
			imageViewIndicator = (ImageView) view.findViewById(viewIndicators[arg0-1]);
			imageViewIndicator.setImageResource(R.drawable.indicator_neutral);
		}
		if(arg0 < viewIndicators.length) {
			imageViewIndicator = (ImageView) view.findViewById(viewIndicators[arg0+1]);
			imageViewIndicator.setImageResource(R.drawable.indicator_neutral);
		}

	}

}
