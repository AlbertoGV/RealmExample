package example.com.realm_example;

import android.content.Intent;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import example.com.realm_example.Model.Person;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

import static io.realm.Realm.getDefaultConfiguration;
import static io.realm.Realm.getDefaultInstance;
import static io.realm.Realm.migrateRealm;

public class MainActivity extends AppCompatActivity {

    private EditText txtName,txtDNI,txtGenere,txtLastName;
    private EditText txtAge,txtId;
    private Button btnAdd;
    private TextView tvView;
    private Button btnremove,btnUpdate;
    private Button btnView;
    private Button btnSearch;


    public int calculateIndex(){
        Realm realm = Realm.getDefaultInstance();
        Number currentIdNum = realm.where(Person.class).max("id");
        int nextId;
        if (currentIdNum == null){
            nextId = 0;
        }else {
            nextId = currentIdNum.intValue()+1;
        }
        return nextId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        txtName = findViewById(R.id.ETfullname);
        txtAge = findViewById(R.id.EtEdad);
        txtId = findViewById(R.id.EtId);
        tvView = findViewById(R.id.tvView);
        txtGenere = findViewById(R.id.Etgenere);
        btnAdd = findViewById(R.id.buttonadd);
        btnSearch = findViewById(R.id.searchdatabase);
        btnremove = findViewById(R.id.dropdatabase);
        btnView = findViewById(R.id.buttonview);
        btnUpdate = findViewById(R.id.updatedatabase);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtId.getText().toString().isEmpty()) {
                 /*   int id = Integer.parseInt(txtId.getText().toString());*/
                    String fullname = txtName.getText().toString();
                    String age = txtAge.getText().toString();
                    String genere = txtGenere.getText().toString();


                    search_to_database_withoutID(fullname, age, genere);
                } else {
                    if (txtId.getText().toString().length()>0) {
                        int id = Integer.parseInt(String.valueOf(txtId.getText().toString()));
                        String fullname = txtName.getText().toString();
                        String age = txtAge.getText().toString();
                        String genere = txtGenere.getText().toString();

                        search_to_database(id, fullname, age, genere);
                    }
                }
            }
        });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (txtId.getText().toString().isEmpty()) {
                            txtId.setError("Necesita poner una ID");
                        }
                                String fullname = txtName.getText().toString();
                                String age = txtAge.getText().toString();
                                String genere = txtGenere.getText().toString();

                                if (txtId.getText().toString().equals("")) {
                                    txtId.setError("Obligatorio un id para poder modificar");
                                } else if (fullname.isEmpty()) {
                                    txtName.setError("Introduzca un nombre");
                                } else if (age.isEmpty()) {
                                    txtAge.setError("Introduzca una edad ");
                                } else if (genere.isEmpty()) {
                                    txtGenere.setError("Introduzca un genero");
                                }else{
                                update_person( fullname,age, genere);
                            }
                        }
                    });



                btnremove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            /*   int id = Integer.parseInt(txtId.getText().toString());*/
                            String fullname = txtName.getText().toString();
                            String age = txtAge.getText().toString();
                            String genere = txtGenere.getText().toString();
                            if(txtId.getText().toString().length()<0) {
                            } if (fullname.isEmpty() || age.isEmpty() || genere.isEmpty()) {
                            Toast.makeText(getApplicationContext(),"Ponga minimo el campo id o el nombre para eliminar",Toast.LENGTH_SHORT).show();
                            }else {
                                delete_from_database_withoutID(fullname, age , genere);
                            }

                            if(txtId.getText().toString().length()>0){
                            int id = Integer.parseInt(String.valueOf(txtId.getText().toString()));
                            delete_from_database(id);
                            }else{
                                Toast.makeText(getApplicationContext(),"Ponga minimo el campo id o el nombre para eliminar",Toast.LENGTH_SHORT).show();
                            }
                        }
                });

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fullname = txtName.getText().toString().trim();
                        String age = txtAge.getText().toString().trim();
                        String genere = txtGenere.getText().toString().trim();
                            if (fullname.isEmpty()) {
                            txtName.setError("Introduzca un nombre");
                        } else if (age.isEmpty()) {
                            txtAge.setError("Introduzca una edad ");
                        } else if (genere.isEmpty()) {
                            txtGenere.setError("Introduzca un genero");
                        }else {
                                save_to_database(fullname, age, genere);
                            }
                    }
                });
                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh_database();
                    }
                });



            }

    private void delete_from_database_withoutID(String fullname, String age, String genere) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Person person = realm.where(Person.class)
                        .equalTo("fullname", fullname)
                        .or()
                        .equalTo("age", age)
                        .or()
                        .equalTo("genere", genere)
                        .findFirst();

                if(person!= null) {
                    person.deleteFromRealm();
                    refresh_database();
                }else{
                    Toast.makeText(getApplicationContext(),"Nada que eliminar",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


 private void delete_from_database(int id) {
     Realm realm = Realm.getDefaultInstance();
     realm.executeTransaction(new Realm.Transaction() {

         @Override
         public void execute(Realm realm) {
             int id = Integer.parseInt(String.valueOf(Integer.parseInt(txtId.getText().toString())));
             Person person = realm.where(Person.class)
                     .equalTo("id", id)
                     .findFirst();
                if(person!=null) {
                    person.deleteFromRealm();
                    refresh_database();
             }else{
                 Toast.makeText(getApplicationContext(),"Nada que eliminar",Toast.LENGTH_SHORT).show();
             }
         }
     });


 }

            private void search_to_database(int id, String fullname, String age, String genere) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        // remove a single object
                        Person person = realm.where(Person.class)
                                .equalTo("id", id)
                                .or()
                                .equalTo("fullname", fullname)
                                .or()
                                .equalTo("age", age)
                                .or()

                                .equalTo("genere", genere)
                                .findFirst();

                        if (person != null) {
                            refresh_filter(id,fullname,age, genere);
                        }

                    }
                });

            }
    private void search_to_database_withoutID( String fullname, String age, String genere) {
        Realm realm = Realm.getDefaultInstance();
     realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                // remove a single object
                Person person = realm.where(Person.class)

                        .equalTo("fullname", fullname)
                        .or()
                        .equalTo("age", age)
                        .or()
                        .equalTo("genere", genere)
                        .findFirst();


                    refresh_filter_withoutID( fullname, age, genere);


                // Delete all matches
                /* persons.deleteAllFromRealm();*/

            }
        });

    }

    private void save_to_database(final String fullname, final String age, final String genere) {
        Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        int index = calculateIndex();
                        Person person = realm.createObject(Person.class, index);
                        person.setFullname(fullname);
                        person.setAge(age);
                        person.setGenere(genere);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        // Transaction was a success.
                        Log.v("Success", "------------OK------------");
                        refresh_database();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        // Transaction failed and was automatically canceled.
                        Log.e("Failed", error.getMessage());
                    }
                });

            }

            private void update_person( String fullname, String age, String genere) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        int id = Integer.parseInt(String.valueOf(Integer.parseInt(txtId.getText().toString())));
                        Person person = realm.where(Person.class)
                                .equalTo("id", id)
                                .findFirst();
                        person.setFullname(fullname);

                        person.setAge(age);
                        person.setGenere(genere);
                        realm.insertOrUpdate(person);
                        refresh_database();

                    }
                 }); new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            // Transaction was a success.
                            Log.v("Success", "------------OK------------");
                            refresh_database();
                        }
                    };new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        // Transaction failed and was automatically canceled.
                        Log.e("Failed", error.getMessage());
                    }
                };
            }


            private void refresh_filter(int id, String fullname, String age, String genere) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Person> result = realm.where(Person.class)
                        .equalTo("id", id)
                        .or()
                        .equalTo("fullname", fullname)
                        .or()
                        .equalTo("age", age)
                        .or()
                        .equalTo("genere", genere)
                        .findAll();
                if (result != null) {
                    result.load();
                    StringBuilder output = new StringBuilder();
                    for (Person person : result) {
                        output.append(person);

                    }
                    tvView.setText("");
                    if (output != null) {
                        tvView.setText(output);
                    }
                }
            }
    private void refresh_filter_withoutID( String fullname, String age, String genere) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Person> result = realm.where(Person.class)
                .equalTo("fullname", fullname)
                .or()
                .equalTo("age", age)
                .or()
                .equalTo("genere", genere)
                .findAll();
        if (result != null) {
            result.load();
            StringBuilder output = new StringBuilder();
            for (Person person : result) {
                output.append(person);

            }

            tvView.setText("");
            if (output != null) {
                tvView.setText(output);
            }
        }
    }

            private void refresh_database() {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Person> result = realm.where(Person.class)
                        .findAllAsync();
                //Ordenado por id Ascendete
                result = result.sort("id", Sort.DESCENDING);

                result.load();

                StringBuilder output = new StringBuilder();
                for (Person person : result) {
                    output.append(person);

                }
                tvView.setText("");
                tvView.setText(output);

            }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item:   //this item has your app icon
               Intent i = new Intent(this,InfoActivity.class);
               startActivity(i);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Realm realm = Realm.getDefaultInstance();
        realm.close();
        }
    }

