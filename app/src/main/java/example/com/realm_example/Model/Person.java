package example.com.realm_example.Model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Person extends RealmObject {


    public Person(){

    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    @Index
    String fullname;

    String age;

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    String genere;


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
                        "FullName: "+ fullname+'\n'+
                        "Genere: "+ genere+'\n'+
                        "Age: "+ age+'\n';

    }
}
