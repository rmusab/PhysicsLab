package kz.develop.physicslab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuActivity extends Fragment {
	
	private Context context;
		
	ListView lvMain;

    Integer[] imageId = {
            R.drawable.button1,
            R.drawable.button2,
            R.drawable.button3,
            R.drawable.button4,
            R.drawable.button5,
            R.drawable.button6,
            R.drawable.button7,
            R.drawable.button8,
            R.drawable.button9,
            R.drawable.button10,
            R.drawable.button11,
            R.drawable.button12,
            R.drawable.button13,
            R.drawable.button14,
            R.drawable.button15,
            R.drawable.button16,
            R.drawable.button17,
            R.drawable.button18
    };
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.menu_layout, container, false);
        context = view.getContext();
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		        context, R.array.buttons, android.R.layout.simple_list_item_1);
		lvMain = (ListView) view.findViewById(R.id.lvMain);
        lvMain.setAdapter(adapter);
		lvMain.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			          int position, long id) {
				Intent intent = new Intent(getActivity(), Form.class);
				intent.putExtra("image", position);
				startActivity(intent);				
			}
			
		});*/
        lvMain = (ListView) view.findViewById(R.id.lvMain);
        CustomList adapter = new CustomList(context, inflater, getResources().getStringArray(R.array.buttons), imageId);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), Form.class);
                intent.putExtra("image", position);
                startActivity(intent);
            }

        });
        return view;
    }

}
