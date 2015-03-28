package by.heap.entity.view;


public class UserJsonView {


    public interface Basic extends CommonJsonView.Id {}

    public interface Location extends Basic {}
    public interface Top extends Basic {}
    public interface Info extends Top {}

}
