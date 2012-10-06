package si.modrajagoda.didi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

public class DialogEditHabit extends DialogFragment {
	
	private static final String HABIT_NAME = "habit_name"; 

	public static DialogEditHabit newInstance(String habitName) {
		DialogEditHabit frag = new DialogEditHabit();
		Bundle args = new Bundle();
		args.putString(HABIT_NAME, habitName);
		frag.setArguments(args);
		return frag;
	}

	   @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        String habitName = getArguments().getString(HABIT_NAME);
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        
	        LayoutInflater li = LayoutInflater.from(getActivity());
	        final EditText editText = (EditText) li.inflate(R.layout.dialog_edit, null);
	        editText.setText(habitName);
	        builder.setView(editText);

	        return builder
	                .setTitle(R.string.dialog_edit_habit_title)
	                .setPositiveButton(R.string.ok,
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                            ((EditHabits)getActivity()).doPositiveClick(editText.getText().toString());
	                        }
	                    }
	                )
	                .setNegativeButton(R.string.cancel,
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int whichButton) {
	                            dismiss();
	                        }
	                    }
	                )
	                .create();
	    }
}
