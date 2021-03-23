package kz.develop.physicslab;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

public class ConvertActivity extends Fragment implements OnClickListener {
	
	private Context context;
	Spinner spMain, spFrom, spTo;
	EditText etValue, etResult;
	public static Button btnCalc;
	CheckBox chClipboard;
	String[] names, values;
	int from, to, main;
	
	private final int ITEM_LENGTH = 0;
	private final int ITEM_MASS = 1;
	private final int ITEM_TIME = 2;
	private final int ITEM_TEMP = 3;
	private final int ITEM_AREA = 4;
	private final int ITEM_VOLUME = 5;
	private final int ITEM_PRESS = 6;
    private final int ITEM_VELO = 7;
    private final int ITEM_ENERGY = 8;
    private final int ITEM_POWER = 9;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.convert_layout, container, false);
		context = view.getContext();
		
		spMain = (Spinner) view.findViewById(R.id.spMain);
		spFrom = (Spinner) view.findViewById(R.id.spFrom);
		spTo = (Spinner) view.findViewById(R.id.spTo);
		etValue = (EditText) view.findViewById(R.id.etValue);
		etResult = (EditText) view.findViewById(R.id.etResult);
		btnCalc = (Button) view.findViewById(R.id.btnCalc);
		btnCalc.setOnClickListener(this);
		chClipboard = (CheckBox) view.findViewById(R.id.chClipboard);
        Button btnClear = (Button) view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etValue.setText("");
                etResult.setText("");
            }
        });
        Button btnUp = (Button) view.findViewById(R.id.btnUp);
        btnUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etValue.setText(etResult.getText());
            }
        });
		
		ArrayAdapter<String> adMain = new ArrayAdapter<String>(context, 
				android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.main));
		adMain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spMain.setAdapter(adMain);
	    spMain.setSelection(ITEM_LENGTH);
	    SelectMain(ITEM_LENGTH);
		spMain.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				SelectMain(position);				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		ScrollView svConvert = (ScrollView) view.findViewById(R.id.svConvert);
		MainActivity.fixBackgroundRepeat(svConvert);		
		return view;
	}

	private void SelectMain(int i) {
		switch (i) {
		case ITEM_LENGTH:
			names = getResources().getStringArray(R.array.length_names);
			values = getResources().getStringArray(R.array.length_values);
			break;

		case ITEM_MASS:
			names = getResources().getStringArray(R.array.mass_names);
			values = getResources().getStringArray(R.array.mass_values);
			break;
			
		case ITEM_TIME:
			names = getResources().getStringArray(R.array.time_names);
			values = getResources().getStringArray(R.array.time_values);
			break;
			
		case ITEM_TEMP:
			names = getResources().getStringArray(R.array.temp_names);
			values = getResources().getStringArray(R.array.temp_values);
			break;
			
		case ITEM_AREA:
			names = getResources().getStringArray(R.array.area_names);
			values = getResources().getStringArray(R.array.area_values);
			break;
			
		case ITEM_VOLUME:
			names = getResources().getStringArray(R.array.volume_names);
			values = getResources().getStringArray(R.array.volume_values);
			break;
			
		case ITEM_PRESS:
			names = getResources().getStringArray(R.array.press_names);
			values = getResources().getStringArray(R.array.press_values);
			break;

        case ITEM_VELO:
            names = getResources().getStringArray(R.array.velocity_names);
            values = getResources().getStringArray(R.array.velocity_values);
            break;

        case ITEM_ENERGY:
            names = getResources().getStringArray(R.array.energy_names);
            values = getResources().getStringArray(R.array.energy_values);
            break;
        case ITEM_POWER:
            names = getResources().getStringArray(R.array.power_names);
            values = getResources().getStringArray(R.array.power_values);
            break;
		}
		main = i;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, 
				android.R.layout.simple_spinner_item, names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spFrom.setAdapter(adapter);
		spTo.setAdapter(adapter);
		
		spFrom.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				from = position;				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
	    });
		spTo.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				to = position;				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
	    });
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View view) {
		double f = 0.00, t = 0.00, x = 0.00, result = 0.00;
		try {
			x = Double.parseDouble(etValue.getText().toString());
			f = Double.parseDouble(values[from]);
			t = Double.parseDouble(values[to]);
		} catch (NumberFormatException nfe) {
			Toast.makeText(context, R.string.toast_error, Toast.LENGTH_SHORT).show();
			return;
		}
		if (main == ITEM_TEMP) {
			switch (from) {
			case 2:
				x = x - 273;
				break;
			}
		}
		result = x*f/t;
		if (main == ITEM_TEMP) {
			switch (to) {
			case 0:
				if (from == 1) result = result - (160/9);
				break;
			case 1:
				if (from != 1) result = result + 32;
				break;
     		case 2:
				if (from == 1) result = result - (160/9);
     			result = result + 273;
				break;
			}
		}
		etResult.setText(Double.toString(result));
		
		if (chClipboard.isChecked()) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				ClipboardManager ClipMan = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("text", etResult.getText().toString());
				ClipMan.setPrimaryClip(clip);
			} else {
				android.text.ClipboardManager ClipMan = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				ClipMan.setText(etResult.getText().toString());
			}
			
			Toast.makeText(context, R.string.toast_clipboard, Toast.LENGTH_SHORT).show();
		}
	}

}
