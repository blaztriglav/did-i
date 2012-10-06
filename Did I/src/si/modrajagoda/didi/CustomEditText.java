package si.modrajagoda.didi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Menu;
import android.widget.EditText;

public class CustomEditText extends EditText{

	public CustomEditText(Context context){
		super(context);
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
		menu.add(Menu.NONE, R.id.context_delete, 1, R.string.menu_delete);
		super.onCreateContextMenu(menu);
	}

}
