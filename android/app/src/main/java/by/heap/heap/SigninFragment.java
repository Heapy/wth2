package by.heap.heap;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SigninFragment extends Fragment implements View.OnClickListener {
    private TextView loginInput;
    private TextView passwordInput;
    public SigninFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signin, container, false);
        loginInput=(TextView)v.findViewById(R.id.loginInput);
        passwordInput=(TextView)v.findViewById(R.id.passwordInput);

        Button login_submit=(Button)v.findViewById(R.id.login_submit);
        login_submit.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_submit:
                loginSubmit();
                break;
        }
    }

    private void loginSubmit()
    {
        InputMethodManager imm = (InputMethodManager)Tools.getMainActivity().getSystemService(
                Tools.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginInput.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordInput.getWindowToken(), 0);
        try {
            JSONObject params = new JSONObject();
            params.put("username",loginInput.getText()+"");
            params.put("password",passwordInput.getText()+"");

            Loader loader=new Loader(Tools.host+"auth/login", ServiceHandler.POST, params.toString()) {
                protected void result() {

                    if(response.code==200) {
                        try {
                            JSONObject root = new JSONObject(response.body);
                            Tools.auth_token=root.getString("token");
                            Tools.showFragment(new StartFragment());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(Tools.getContext(),"Неверный логин или пароль",Toast.LENGTH_LONG).show();
                    }
                }
            };
            loader.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
