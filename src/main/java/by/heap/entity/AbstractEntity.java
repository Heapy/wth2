package by.heap.entity;

import by.heap.entity.view.CommonJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import org.omg.CORBA.IdentifierHelper;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue
    @JsonView(CommonJsonView.Id.class)
    private Long id;

    public Long getId() {
        return id;
    }
}
