package by.heap.heap;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class MainFragment extends Fragment implements View.OnClickListener {


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        Button signin_button=(Button)v.findViewById(R.id.signin_button);
        signin_button.setOnClickListener(this);
        Button signup_button=(Button)v.findViewById(R.id.singup_button);
        signup_button.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                Tools.showFragment(new SigninFragment());
                break;
            case R.id.singup_button:
                break;
        }
    }
}
