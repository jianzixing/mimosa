package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.JoinName;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.util.List;

@Table
public class BeanUser {
    @Column(pk = true, strategy = AutoIncrementStrategy.class)
    private int id;

    @Column(length = 60, nullable = false)
    private String userName;

    @Column(type = int.class, defaultValue = "20")
    private int age;

    @JoinName("pays")
    private List<BeanPay> pays;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<BeanPay> getPays() {
        return pays;
    }

    public void setPays(List<BeanPay> pays) {
        this.pays = pays;
    }
}
