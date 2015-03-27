package by.heap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class User extends AbstractEntity {

    @Column
    private String displayName;

    @Column
    private String username;

    @Column
    private String password;


}
