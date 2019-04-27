package example.com.realm_example;

import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = findViewById(R.id.EtNombre);
        txtAge = findViewById(R.id.EtEdad);
        tvView = findViewById(R.id.tvView);
        btnAdd = findViewById(R.id.buttonadd);
        btnremove = findViewById(R.id.dropdatabase);
        btnView = findViewById(R.id.buttonview);


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
        final RealmResults<Person> persons = realm.where(Person.class)
                .equalTo("name",name)
                .or()
                .equalTo("age",age)
                .findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // remove a single object
                persons.deleteFromRealm(0);
                // Delete all matches
                persons.deleteAllFromRealm();
                refresh_database();
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

    private void refresh_database() {
        RealmResults<Person> result = realm.where(Person.class)
                .findAllAsync();

        result.load();
        StringBuilder output = new StringBuilder();

        for(Person person:result){
            output.append(person);

        }

        tvView.setText(output);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
