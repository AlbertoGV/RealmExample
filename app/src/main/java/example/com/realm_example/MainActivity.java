package example.com.realm_example;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import example.com.realm_example.Model.Person;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static io.realm.Realm.getDefaultInstance;

public class MainActivity extends AppCompatActivity {
    private Realm realm;
    private EditText txtName;
    private EditText txtAge;
    private Button btnAdd;
    private TextView tvView;
    private Button btnremove;
    private Button btnView;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = findViewById(R.id.EtNombre);
        txtAge = findViewById(R.id.EtEdad);
        tvView = findViewById(R.id.tvView);
        btnAdd = findViewById(R.id.buttonadd);
        btnSearch = findViewById(R.id.searchdatabase);
        btnremove = findViewById(R.id.dropdatabase);
        btnView = findViewById(R.id.buttonview);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                String age = txtAge.getText().toString();
                search_to_database(name,age);
            }
        });


        btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                String age = txtAge.getText().toString();
                delete_from_database(name, age);

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_to_database(txtName.getText().toString().trim(), txtAge.getText().toString().trim());

            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_database();
            }
        });


        realm = getDefaultInstance(); // opens "myrealm.realm"

    }

    private void delete_from_database(String name, String age) {
        // obtain the results of a query
        // All changes to data must happen in a transaction
        Person person = realm.where(Person.class)
                .equalTo("name",name)
                .or()
                .equalTo("age",age)
                .findFirstAsync();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                // remove a single object
                    if(person != null) {
                        person.deleteFromRealm();
                        refresh_filter(name,age);
                        refresh_database();
                    }

                // Delete all matches
               /* persons.deleteAllFromRealm();*/
            }
        });

    }

    private void search_to_database( String name,String age){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                // remove a single object
                Person person = realm.where(Person.class)
                        .contains("name", name)
                        .and()
                        .contains("age",age)
                        .findFirst();
                if(person != null) {
                    refresh_filter(name, age);
                }

                // Delete all matches
                /* persons.deleteAllFromRealm();*/

            }
        });

    }

    private void save_to_database(final String Name, final String Age) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Person person= bgRealm.createObject(Person.class);
                person.setName(Name);
                person.setAge(Age);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.v("Success","------------OK------------");
                refresh_database();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("Failed",error.getMessage());
            }
        });

    }

    private void refresh_filter(String name, String age){
        RealmResults<Person> result = realm.where(Person.class)
                .equalTo("name",name)
                .or()
                .equalTo("age",age)
                .findAll();
        result.load();
        StringBuilder output = new StringBuilder();
        for(Person person:result){
            output.append(person);

        }
        tvView.setText("");
        tvView.setText(output);

    }

    private void refresh_database() {
        RealmResults<Person> result = realm.where(Person.class)
                .findAllAsync();
        //Ordenado por Nombre Ascendete
        result = result.sort("name"/*,Sort.DESCENDING*/);

        result.load();

        StringBuilder output = new StringBuilder();
        for(Person person:result){
            output.append(person);

        }
        tvView.setText("");
        tvView.setText(output);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
