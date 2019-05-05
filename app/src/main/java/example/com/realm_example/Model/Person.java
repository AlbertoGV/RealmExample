package example.com.realm_example.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    String genere;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    String dni;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @PrimaryKey
    int id;


    @Override
    public String toString() {

                return  " " +'\n'+
                        "-----Customer----" +'\n'+
                        "ID: "+ id +'\n'+
                        "Name: "+ name +'\n'+
                        "DNI: "+ dni +'\n'+
                        "Genere: "+genere+'\n'+
                        "Age: "+ age+'\n';

    }
}
