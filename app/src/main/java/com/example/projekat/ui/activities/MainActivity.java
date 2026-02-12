package com.example.projekat.ui.activities;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import com.example.projekat.R;
import com.example.projekat.ui.fragments.AddTradeFragment;
import com.example.projekat.ui.fragments.ClosedTradesFragment;
import com.example.projekat.ui.fragments.TradesFragment;
import com.example.projekat.ui.fragments.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private boolean twoPane;
    private View left, right, divider;
    private int primaryContainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        WindowCompat.setDecorFitsSystemWindows(window, false);
        window.setStatusBarColor(Color.TRANSPARENT);

        bottomNav = findViewById(R.id.bottom_nav);

        left    = findViewById(R.id.fragment_container_left);
        right   = findViewById(R.id.fragment_container_right);
        divider = findViewById(R.id.view_divider);

        twoPane = (right != null && divider != null);

        if (!twoPane) {
            View phoneContainer = findViewById(R.id.fragment_container);
            if (phoneContainer == null) {
                throw new IllegalStateException("Nema fragment_container u telefonskom activity_main.xml");
            }
            primaryContainerId = R.id.fragment_container;
        } else {
            primaryContainerId = R.id.fragment_container_left;
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_active) {
                if (twoPane) {
                    showTwoPane();
                    replace(R.id.fragment_container_left,  new TradesFragment());
                    replace(R.id.fragment_container_right, new AddTradeFragment());
                } else {
                    showSinglePane();
                    replace(primaryContainerId, new TradesFragment());
                }
                return true;

            } else if (id == R.id.nav_add) {
                if (twoPane) {
                    showTwoPane();
                    replace(R.id.fragment_container_left,  new AddTradeFragment());
                    replace(R.id.fragment_container_right, new TradesFragment());
                } else {
                    showSinglePane();
                    replace(primaryContainerId, new AddTradeFragment());
                }
                return true;

            } else if (id == R.id.nav_closed) {
                showSinglePane();
                replace(primaryContainerId, new ClosedTradesFragment());
                return true;

            } else if (id == R.id.nav_news) {
                showSinglePane();
                replace(primaryContainerId, new NewsFragment());
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_active);
        }
    }

    private void replace(int containerId, Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, f)
                .commit();
    }

    private void showTwoPane() {
        if (!twoPane) return;
        right.setVisibility(View.VISIBLE);
        divider.setVisibility(View.VISIBLE);

    }

    private void showSinglePane() {
        if (twoPane) {
            right.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
    }
}
