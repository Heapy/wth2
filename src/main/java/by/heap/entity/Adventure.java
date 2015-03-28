package by.heap.entity;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TODO: CommentMe!
 *
 * @author Ibragimov Ruslan
 */
@Deprecated
public class Adventure {

    private User firstUser;

    private User secondUser;

    private String token;

    private AtomicBoolean status;

    public User getFirstUser() {
        return firstUser;
    }

    public Adventure setFirstUser(User firstUser) {
        this.firstUser = firstUser;
        return this;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public Adventure setSecondUser(User secondUser) {
        this.secondUser = secondUser;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Adventure setToken(String token) {
        this.token = token;
        return this;
    }

    public AtomicBoolean getStatus() {
        return status;
    }

    public Adventure setStatus(AtomicBoolean status) {
        this.status = status;
        return this;
    }
}
