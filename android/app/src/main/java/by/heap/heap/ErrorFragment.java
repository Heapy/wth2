package by.heap.heap;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorFragment extends Fragment implements View.OnClickListener {

    public TextView textView;
    private String mText;
    public ErrorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_error, container, false);
        textView=(TextView)v.findViewById(R.id.textView3);
        textView.setText(mText);

        Button btnDone=(Button) v.findViewById(R.id.error_button);
        btnDone.setOnClickListener(this);

        return v;
    }

    public void setText(String text)
    {
        mText=text;
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.error_button:
                Tools.showFragment(new StartFragment());
                break;

            default:
                break;
        }
    }

}
