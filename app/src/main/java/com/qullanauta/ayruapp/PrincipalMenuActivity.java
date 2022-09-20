package com.qullanauta.ayruapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.qullanauta.ayruapp.Fragments.ClientesFragment;
import com.qullanauta.ayruapp.Fragments.MapaClientesFragment;
import com.qullanauta.ayruapp.Fragments.InfoFragment;



import java.util.ArrayDeque;
import java.util.Deque;

public class PrincipalMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navigation;

    private InfoFragment mfragmentPlantas;
    private ClientesFragment mfragmentEvaluacion;
    private MapaClientesFragment mfragmentInfo;

    private int posNavBottom;
    Deque<Integer> integerDeque = new ArrayDeque<>(4);
    boolean flag  = true;

    private FragmentRefreshListener fragmentRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_menu);
        navigation = findViewById(R.id.dashboard_navigation);
        integerDeque.push(R.id.navigation_plantas);
        mfragmentPlantas = new InfoFragment();
        loadFragment(mfragmentPlantas);
        navigation.setSelectedItemId(R.id.navigation_plantas);


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                posNavBottom = menuItem.getItemId();
                if (integerDeque.contains(posNavBottom)){
                    if (posNavBottom == R.id.navigation_plantas){
                        if (integerDeque.size() != 1){
                            if (flag){
                                integerDeque.addFirst(R.id.navigation_plantas);
                                flag = false;
                            }
                        }
                    }
                    integerDeque.remove(posNavBottom);
                }
                integerDeque.push(posNavBottom);
                loadFragment(getFragment(menuItem.getItemId()));
                return true;
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.main_frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }




    private Fragment getFragment(int itemId) {
        switch (itemId){
            case R.id.navigation_plantas:
                mfragmentPlantas = new InfoFragment();
                navigation.getMenu().getItem(0).setChecked(true);
                System.out.println("agenda position:"+ navigation.getSelectedItemId());
                return mfragmentPlantas;
            case R.id.navigation_evaluacion:
                if(getFragmentRefreshListener()!= null){
                    getFragmentRefreshListener().onRefresh();
                }
                mfragmentEvaluacion = new ClientesFragment();
                navigation.getMenu().getItem(1).setChecked(true);
                return  mfragmentEvaluacion;
            case R.id.navigation_informacion:
                mfragmentInfo = new MapaClientesFragment();
                navigation.getMenu().getItem(2).setChecked(true);
                return  mfragmentInfo;
        }
        navigation.getMenu().getItem(1).setChecked(true);
        return mfragmentPlantas;
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }

    @Override
    public void onBackPressed() {
        integerDeque.pop();
        if (!integerDeque.isEmpty()){
            loadFragment(getFragment(integerDeque.peek()));
        }else {
            finish();
        }
    }

}