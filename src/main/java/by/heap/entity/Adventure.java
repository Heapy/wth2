package by.heap.entity;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TODO: CommentMe!
 *
 * @author Ibragimov Ruslan
 */
@Entity
public class Adventure extends AbstractEntity {

    @ManyToOne(optional = false)
    private User firstUser;

    @ManyToOne
    private User secondUser;

    @Column
    private String token;

    @Column
    private AtomicBoolean status;

    @Column
    private GameStatus gameStatus;

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

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public Adventure setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        return this;
    }
}
