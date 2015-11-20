package by.heap.heap;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InterestFragment extends Fragment implements View.OnClickListener {

    private ListView listview;
    private ItemAdapter adapter;
    private ArrayList<ItemBean> arrlistItems = new ArrayList<ItemBean>();
    int selectionCount=0;
    private Button btnDone;

    public InterestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_interest, container, false);

        listview = (ListView) v.findViewById(R.id.listview);
        btnDone=(Button) v.findViewById(R.id.btnDone);

        btnDone.setOnClickListener(this);

        loadInterests();

        return v;
    }

    public void loadInterests()
    {
        Loader loader=new Loader(Tools.host+"user/interests", ServiceHandler.GET){
            protected void result() {
                if (response.code==200) {
                    try {
                        JSONArray root = new JSONArray(response.body);

                        for(int i=0;i<root.length();i++)
                        {
                            JSONObject o=root.getJSONObject(i);
                            ItemBean bean=new ItemBean();
                            bean.setItemName(o.getString("name"));
                            arrlistItems.add(bean);
                        }

                        adapter = new ItemAdapter(Tools.getMainActivity(), arrlistItems);
                        listview.setAdapter(adapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        loader.execute();
    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnDone:

                ArrayList<ItemBean> arraydata = (ArrayList<ItemBean>) adapter.arraylistData;

                ItemBean selected=null;
                for (int i = 0; i < arraydata.size(); i++)
                {
                    ItemBean bean = arraydata.get(i);

                    if (bean.isSelected)
                    {
                        selected=arraydata.get(i);
                    }
                }
                if(selected==null)
                    Toast.makeText(Tools.getContext(), "Необходимо выбрать интерес", Toast.LENGTH_SHORT).show();
                else
                    checkInterest(selected.getItemName());

                break;

            default:
                break;
        }
    }

    public void checkInterest(String interest)
    {
        try {
            JSONObject params = new JSONObject();
            params.put("interest",interest);

            Loader loader=new Loader(Tools.host+"adventure/"+Tools.adventure_id+"/interest", ServiceHandler.POST, params.toString()){
                protected void result() {
                    if (response.code==200) {
                        try {
                            JSONObject root = new JSONObject(response.body);
                            boolean status=root.getBoolean("status");
                            ErrorFragment fragment=new ErrorFragment();
                            String text=status?"Поздварляем! Интерес выбран правильно.":"Интерес выбран неверно.";
                            fragment.setText(text);
                            Tools.showFragment(fragment);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            loader.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
