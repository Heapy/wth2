package by.heap.heap;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter
{
    private Context mContext;
    private LayoutInflater inflater;
    public List<ItemBean> arraylistData = null;
    private ArrayList<ItemBean> arraylist;

    public ItemAdapter(Context context, List<ItemBean> listbean)
    {
        mContext = context;
        this.arraylistData = listbean;
        inflater = LayoutInflater.from(mContext);

        this.arraylist = new ArrayList<ItemBean>();
        this.arraylist.addAll(listbean);

    }

    public class ViewHolder
    {
        TextView txtItemName;
        ImageView imgvCheck;
    }

    @Override
    public int getCount()
    {
        return arraylistData.size();
    }

    @Override
    public ItemBean getItem(int position)
    {
        return arraylistData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent)
    {
        final ViewHolder holder;
        if (view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_item, null);
            holder.txtItemName = (TextView) view.findViewById(R.id.txtItemName);
            holder.imgvCheck = (ImageView) view.findViewById(R.id.imgvCheck);
            view.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        // Set data to view=================================================
        try
        {
            ItemBean bean = arraylistData.get(position);

            if (bean.isSelected)
                holder.imgvCheck.setSelected(true);
            else
                holder.imgvCheck.setSelected(false);


            holder.txtItemName.setText(bean.getItemName());


            view.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    for (int i = 0; i < arraylistData.size(); i++)
                    {
                        if (i == position)
                        {
                            ItemBean bean = arraylistData.get(position);
                            if (bean.isSelected())
                            {}
                            else
                            {
                                bean.setSelected(true);
                            }
                        }
                        else
                        {
                            ItemBean bean = arraylistData.get(i);
                            bean.setSelected(false);
                        }
                        notifyDataSetChanged();
                    }
                }
            });
            holder.imgvCheck.setTag(bean);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

    // Filter Class
    public void filter(String charText)
    {
        charText = charText.toLowerCase();
        arraylistData.clear();
        if (charText.length() == 0)
        {
            arraylistData.addAll(arraylist);
        }
        else
        {
            for (ItemBean contact : arraylist)
            {
                if (contact.getItemName().toLowerCase().contains(charText))
                {
                    arraylistData.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

}