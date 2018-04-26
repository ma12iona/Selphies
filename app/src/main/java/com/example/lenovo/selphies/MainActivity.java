package com.example.lenovo.selphies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;



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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(fragment.getClass().equals(PostFragment.class)){
            ((PostFragment) fragment).getImage(requestCode, resultCode, data);
        }
        if(fragment.getClass().equals(ProfileFragment.class)){
            ((ProfileFragment) fragment).getImage(requestCode, resultCode, data);
        }
    }


}
