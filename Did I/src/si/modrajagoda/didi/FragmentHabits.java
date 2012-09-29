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

	View view;
	Button buttonYes;
	Button buttonNo;
	int count = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_habits, container, false);

		buttonYes = (Button) view.findViewById(R.id.button2);
		buttonYes.setOnClickListener(this);

		buttonNo = (Button) view.findViewById(R.id.button3);
		buttonNo.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {

		if(v.getId()==R.id.button2) {

			if(count==1) {
				ImageView indicator1 = (ImageView) view.findViewById(R.id.image_view_indicator_1);
				indicator1.setImageResource(R.drawable.indicator_positive);
				ImageView indicator2 = (ImageView) view.findViewById(R.id.image_view_indicator_2);
				indicator2.setImageResource(R.drawable.indicator_neutral_selected);
				count= count + 1;
				TextView textViewHabit = (TextView) view.findViewById(R.id.button1);
				textViewHabit.setText("exercise");
			}

			else if(count==2) {
				
				ImageView indicator2 = (ImageView) view.findViewById(R.id.image_view_indicator_2);
				indicator2.setImageResource(R.drawable.indicator_positive);
				ImageView indicator3 = (ImageView) view.findViewById(R.id.image_view_indicator_3);
				indicator3.setImageResource(R.drawable.indicator_neutral_selected);
				count= count + 1;
				TextView textViewHabit = (TextView) view.findViewById(R.id.button1);
				textViewHabit.setText("eat healthy");
			}
			
			else if(count==3) {
				ImageView indicator3 = (ImageView) view.findViewById(R.id.image_view_indicator_3);
				indicator3.setImageResource(R.drawable.indicator_positive);
				ImageView indicator4 = (ImageView) view.findViewById(R.id.image_view_indicator_4);
				indicator4.setImageResource(R.drawable.indicator_neutral_selected);
				count= count + 1;
				TextView textViewHabit = (TextView) view.findViewById(R.id.button1);
				textViewHabit.setText("meditate");
			}

			else if(count==4) {
				
				ImageView indicator4 = (ImageView) view.findViewById(R.id.image_view_indicator_4);
				indicator4.setImageResource(R.drawable.indicator_positive);
				ImageView indicator5 = (ImageView) view.findViewById(R.id.image_view_indicator_5);
				indicator5.setImageResource(R.drawable.indicator_neutral_selected);
				count= count + 1;
				TextView textViewHabit = (TextView) view.findViewById(R.id.button1);
				textViewHabit.setText("wake up early");
			}
			else if(count==5) {
				ImageView indicator5 = (ImageView) view.findViewById(R.id.image_view_indicator_5);
				indicator5.setImageResource(R.drawable.indicator_positive);
			}

		}
		
		if(v.getId()==R.id.button3) {

			if(count==1) {
				ImageView indicator1 = (ImageView) view.findViewById(R.id.image_view_indicator_1);
				indicator1.setImageResource(R.drawable.indicator_negative);
				ImageView indicator2 = (ImageView) view.findViewById(R.id.image_view_indicator_2);
				indicator2.setImageResource(R.drawable.indicator_neutral_selected);
				count= count + 1;
				TextView textViewHabit = (TextView) view.findViewById(R.id.button1);
				textViewHabit.setText("exercise");
			}

			else if(count==2) {
				
				ImageView indicator2 = (ImageView) view.findViewById(R.id.image_view_indicator_2);
				indicator2.setImageResource(R.drawable.indicator_negative);
				ImageView indicator3 = (ImageView) view.findViewById(R.id.image_view_indicator_3);
				indicator3.setImageResource(R.drawable.indicator_neutral_selected);
				count= count + 1;
				TextView textViewHabit = (TextView) view.findViewById(R.id.button1);
				textViewHabit.setText("eat healthy");
			}
			
			else if(count==3) {
				ImageView indicator3 = (ImageView) view.findViewById(R.id.image_view_indicator_3);
				indicator3.setImageResource(R.drawable.indicator_negative);
				ImageView indicator4 = (ImageView) view.findViewById(R.id.image_view_indicator_4);
				indicator4.setImageResource(R.drawable.indicator_neutral_selected);
				count= count + 1;
				TextView textViewHabit = (TextView) view.findViewById(R.id.button1);
				textViewHabit.setText("meditate");
			}

			else if(count==4) {
				
				ImageView indicator4 = (ImageView) view.findViewById(R.id.image_view_indicator_4);
				indicator4.setImageResource(R.drawable.indicator_negative);
				ImageView indicator5 = (ImageView) view.findViewById(R.id.image_view_indicator_5);
				indicator5.setImageResource(R.drawable.indicator_neutral_selected);
				count= count + 1;
				TextView textViewHabit = (TextView) view.findViewById(R.id.button1);
				textViewHabit.setText("wake up early");
			}
			else if(count==5) {
				ImageView indicator5 = (ImageView) view.findViewById(R.id.image_view_indicator_5);
				indicator5.setImageResource(R.drawable.indicator_negative);
			}

		}



	}

}
