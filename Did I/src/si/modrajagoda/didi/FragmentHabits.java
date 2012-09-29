package si.modrajagoda.didi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentHabits extends Fragment implements OnClickListener {

	private View view;
	private Button buttonYes;
	private Button buttonNo;
	private int count = 1;
	private int[] viewIndicators = {R.id.image_view_indicator_1, R.id.image_view_indicator_2, 
			R.id.image_view_indicator_3, R.id.image_view_indicator_4, R.id.image_view_indicator_5};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_habits, container, false);

		buttonYes = (Button) view.findViewById(R.id.button_yes);
		buttonYes.setOnClickListener(this);

		buttonNo = (Button) view.findViewById(R.id.button_no);
		buttonNo.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {

		if(v.getId()==R.id.button2) {
			switch (count) {
			case 1:
				setIndicatorAndQuestion(v, true, 1, "exercise");
				break;
			case 2:
				setIndicatorAndQuestion(v, true, 2, "eat healthy");
				break;
			case 3:
				setIndicatorAndQuestion(v, true, 3, "meditate");
				break;
			case 4:
				setIndicatorAndQuestion(v, true, 4, "wake up early");
				break;
			default:
				ImageView indicator5 = (ImageView) view.findViewById(R.id.image_view_indicator_5);
				indicator5.setImageResource(R.drawable.indicator_positive);
				break;
			}
		} else if(v.getId()==R.id.button3) {
			switch (count) {
			case 1:
				setIndicatorAndQuestion(v, false, 1, "exercise");
				break;
			case 2:
				setIndicatorAndQuestion(v, false, 2, "eat healthy");
				break;
			case 3:
				setIndicatorAndQuestion(v, false, 3, "meditate");
				break;
			case 4:
				setIndicatorAndQuestion(v, false, 4, "wake up early");
				break;
			default:
				ImageView indicator5 = (ImageView) view.findViewById(R.id.image_view_indicator_5);
				indicator5.setImageResource(R.drawable.indicator_negative);
				break;
			}
		}
	}
	
	private void setIndicatorAndQuestion(View view, boolean answer, int currentPosition, String nextHabit){
		ImageView thisIndicator = (ImageView) view.findViewById(viewIndicators[currentPosition]);
		thisIndicator.setImageResource(answer ? R.drawable.indicator_positive : R.drawable.indicator_negative);
		ImageView nextIndicator = (ImageView) view.findViewById(viewIndicators[currentPosition+1]);
		nextIndicator .setImageResource(R.drawable.indicator_neutral_selected);
		TextView textViewHabit = (TextView) view.findViewById(R.id.button1);
		textViewHabit.setText(nextHabit);
		count += 1;
	}

}
