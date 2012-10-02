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
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPagerAdapterHabit extends PagerAdapter implements OnClickListener {

	private final Context context;
	private final int questionCount;
	private final ArrayList<String> habitQuestions;
	private Button buttonYes;
	private Button buttonNo;

	public ViewPagerAdapterHabit(Context context, int questionCount, ArrayList<String> habitQuestions)
	{
		this.context = context;
		this.questionCount = questionCount;
		this.habitQuestions = habitQuestions;
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

		buttonNo = (Button) view.findViewById(R.id.button_no);
		buttonNo.setOnClickListener(this);
		
//		Button buttonTest = (Button) view.findViewById(R.id.test_button);
//		buttonTest.setOnClickListener(this);
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
		// TODO Auto-generated method stub
//		if (v.getId() == R.id.test_button){ // TODO delete this
//			Intent intetn = new Intent(context, EditHabits.class);
//			context.startActivity(intetn);
//		}
		
	}
}
