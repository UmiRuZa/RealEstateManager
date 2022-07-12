package com.openclassrooms.realestatemanager.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.UI.fragments.MapFragment;
import com.openclassrooms.realestatemanager.UI.fragments.SettingsFragment;
import com.openclassrooms.realestatemanager.UI.fragments.SimulatorFragment;
import com.openclassrooms.realestatemanager.databinding.ActivityItemDetailBinding;
import com.openclassrooms.realestatemanager.placeholder.PlaceholderContent;
import com.openclassrooms.realestatemanager.roomdb.AppDatabase;
import com.openclassrooms.realestatemanager.roomdb.ResidenceDAO;

public class ItemDetailHostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    AppDatabase db;
    ResidenceDAO residenceDAO;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityItemDetailBinding binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_item_detail);
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.
                Builder(navController.getGraph())
                .build();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();

        db = AppDatabase.getInstance(this);
        residenceDAO = db.residenceDAO();

        this.configureToolbar();

        this.configureDrawerLayout();

        this.configureNavigationView();
    }


    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_map :
                transaction.replace(R.id.nav_host_fragment_item_detail, new MapFragment());
                transaction.setReorderingAllowed(true).addToBackStack(null).commit();
                break;
            case R.id.activity_main_drawer_simulator:
                transaction.replace(R.id.nav_host_fragment_item_detail, new SimulatorFragment());
                transaction.setReorderingAllowed(true).addToBackStack(null).commit();
                break;
            case R.id.activity_main_drawer_settings:
                transaction.replace(R.id.nav_host_fragment_item_detail, new SettingsFragment());
                transaction.setReorderingAllowed(true).addToBackStack(null).commit();
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolbar() {
        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout(){
        this.drawerLayout = findViewById(R.id.activity_main_nav_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView(){
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_residence:
                Gson gson = new Gson();
                String json = sharedPref.getString("CurrentResidence", "");
                PlaceholderContent.PlaceholderItem residence = gson.fromJson(json, PlaceholderContent.PlaceholderItem.class);
                onDeleteResidence(residence);
                return true;
            case R.id.edit_residence:
                Intent iE = new Intent(ItemDetailHostActivity.this, EditRealEstateActivity.class);
                startActivity(iE);
                return true;
            case R.id.add_residence:
                Intent iA = new Intent(ItemDetailHostActivity.this, AddRealEstateActivity.class);
                startActivity(iA);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_item_detail);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    public void onDeleteResidence(PlaceholderContent.PlaceholderItem mItem) {
        residenceDAO.deleteResidence(Long.getLong(mItem.id));
        PlaceholderContent.deleteItem(mItem);

        onListUpdated();
    }

    public void onListUpdated() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}