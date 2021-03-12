package ru.sfedu.coursezz.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.coursezz.utils.Converters.PlanConverter;

import java.util.List;
import java.util.Objects;

/**
 * Class Client
 */
public class Client {

    @CsvBindByName(required = true)
    private Long id;

    @CsvBindByName(required = true)
    private String name;

    @CsvBindByName(required = true)
    private String login;

    @CsvBindByName(required = true)
    private String password;

    @CsvBindByName(required = true)
    private String email;

    @CsvCustomBindByName(required = true, converter = PlanConverter.class)
    private List<Plan> planList;

    public Client() {}

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Plan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(planList, client.planList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planList);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login=" + login +
                ", password=" + password +
                ", email=" + email +
                ", planList=" + planList +
                '}';
    }

}
