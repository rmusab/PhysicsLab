package kz.develop.physicslab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rav_4 on 09.06.2014.
 */
public class CustomList extends ArrayAdapter<String> {
    private final Context context;
    private final LayoutInflater inflater;
    private final String[] buttons;
    private final Integer[] imageId;

    public CustomList(Context context, LayoutInflater inflater, String[] buttons, Integer[] imageId) {
        super(context, R.layout.list_item, buttons);
        this.buttons = buttons;
        this.imageId = imageId;
        this.context = context;
        this.inflater = inflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.list_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(buttons[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
