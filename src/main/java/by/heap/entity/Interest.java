package by.heap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class Interest extends AbstractEntity {

    public Interest() {
    }

    public Interest(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public Interest setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interest interest = (Interest) o;

        return !(name != null ? !name.equals(interest.name) : interest.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "name='" + name + '\'' +
                '}';
    }
}
