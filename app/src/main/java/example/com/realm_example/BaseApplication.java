package example.com.realm_example;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

@Override
public void onCreate() {
    super.onCreate();
    Realm.init(this);
   /* RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build()**/
    RealmConfiguration config2 = new RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build();
    Realm.setDefaultConfiguration(config2);
}


}
