package by.heap.heap;

public class ItemBean
{
    public String ItemName;
    public boolean isSelected ;

    public String getItemName()
    {
        return ItemName;
    }
    public void setItemName(String displayName)
    {
        this.ItemName = displayName;
    }
    public boolean isSelected()
    {
        return isSelected;
    }
    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }
}