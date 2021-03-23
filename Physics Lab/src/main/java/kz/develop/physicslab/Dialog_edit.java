package kz.develop.physicslab;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class Dialog_edit extends DialogFragment implements OnClickListener {
	
	EditText etNameChange, etTxtChange;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(R.string.item_value_text);
		View v = inflater.inflate(R.layout.dialog_edit, null);
	    v.findViewById(R.id.btnPos).setOnClickListener(this);
	    v.findViewById(R.id.btnNeg).setOnClickListener(this);
	    etNameChange = (EditText) v.findViewById(R.id.etNameChange);
		etTxtChange = (EditText) v.findViewById(R.id.etTxtChange);
		Cursor item_c = ConstActivity.db.getRec((int) ConstActivity.menu_id);
		if (item_c.moveToFirst()) {
		 etNameChange.setText(item_c.getString(item_c.getColumnIndex(DB.COLUMN_NAME)));
		 etTxtChange.setText(item_c.getString(item_c.getColumnIndex(DB.COLUMN_TXT)));
		}
		item_c.close();
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPos:
			if (etNameChange.getText().toString().equals("")) {
		 		Toast.makeText(getActivity(), R.string.toast_name, Toast.LENGTH_SHORT).show();
				return;
			  }
			
			  if (etTxtChange.getText().toString().equals("")) {
				 Toast.makeText(getActivity(), R.string.toast_txt, Toast.LENGTH_SHORT).show();
				 return;
			  }
			  
			  ConstActivity.db.changeRec(ConstActivity.menu_id, 
					  etNameChange.getText().toString(), 
					  etTxtChange.getText().toString());
			  getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
			  Toast.makeText(getActivity(), R.string.toast_changed_text, Toast.LENGTH_SHORT).show();
			break;

		case R.id.btnNeg:
			break;
		}
		dismiss();		
	}

}
