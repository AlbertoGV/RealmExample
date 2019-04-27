package example.com.realm_example.Model;

import io.realm.RealmObject;

public class Person extends RealmObject {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    String name;
    String age;


    @Override
    public String toString() {
        return "Person{" +
                "Name="+ name +'\'' +
                ",Age="+ age +
                '}';
    }
}
