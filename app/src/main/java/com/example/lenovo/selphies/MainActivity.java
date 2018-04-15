package com.example.lenovo.selphies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

      loadFragment(new HomeFragment());
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()){

            case R.id.navigation_home:

                fragment = new HomeFragment();
                break;

            case R.id.navigation_aroundyou:

                fragment = new AroundyouFragment();
                break;

            case R.id.navigation_leaderboard:

                fragment = new LeaderboardFragment();
                break;

                case R.id.navigation_post:

                fragment = new PostFragment();
                break;

            case R.id.navigation_profile:

                fragment = new ProfileFragment();
                break;
        }


        return loadFragment(fragment);
    }
}
