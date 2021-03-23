package kz.develop.physicslab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Steve Fox on 01.08.2014.
 */
public class Dialog_add extends DialogFragment {

    EditText etName, etTxt;
    Button btnAdd;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add, container, false);
        context = view.getContext();
        getDialog().setTitle(R.string.btnAdd_Text);

        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        etName = (EditText) view.findViewById(R.id.etName);
        etTxt = (EditText) view.findViewById(R.id.etTxt);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etName.getText().toString().equals("")) {
                    Toast.makeText(context, R.string.toast_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etTxt.getText().toString().equals("")) {
                    Toast.makeText(context, R.string.toast_txt, Toast.LENGTH_SHORT).show();
                    return;
                }

                ConstActivity.db.addRec(etName.getText().toString(), etTxt.getText().toString());
                getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
                etName.setText("");
                etTxt.setText("");
                Toast.makeText(context, R.string.toast_add, Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });

        return view;
    }
}
