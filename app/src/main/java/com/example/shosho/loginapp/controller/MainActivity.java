package com.example.shosho.loginapp.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shosho.loginapp.R;
import com.example.shosho.loginapp.adapter.ItemAdapter;
import com.example.shosho.loginapp.api.Client;
import com.example.shosho.loginapp.model.CountryResponse;
import com.example.shosho.loginapp.model.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
private NavigationView navigationView;
private Toolbar toolbar;
private ActionBarDrawerToggle actionBarDrawerToggle;
private DrawerLayout drawerLayout;
FirebaseAuth firebaseAuth;

ProgressDialog progressDialog;
private RecyclerView recyclerView;
private SwipeRefreshLayout swipeContainer;
TextView textView_disconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

       firebaseAuth=FirebaseAuth.getInstance();
        toolbar=findViewById( R.id.main_page_toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setTitle( "Home" );



        drawerLayout=findViewById( R.id.main_drawer_layout );
        actionBarDrawerToggle=new ActionBarDrawerToggle( MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close );
        drawerLayout.addDrawerListener( actionBarDrawerToggle );
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

       navigationView=findViewById( R.id.navigation_view );
        View navView=navigationView.inflateHeaderView( R.layout.navigation_header );
        navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        } );


        initViews();
        swipeContainer = findViewById( R.id.main_swip_container );
        swipeContainer.setColorSchemeResources( android.R.color.holo_orange_dark );

        swipeContainer.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJson();
                Toast.makeText( MainActivity.this, "Countries list Refreshed", Toast.LENGTH_SHORT ).show();
            }
        } );

    }


   private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId())
        {

            case R.id.item_LogOut:
                firebaseAuth.signOut();
                sendUsertoLoginActivity();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected( item ))
        {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser==null)
        {
            sendUsertoLoginActivity();
        }
    }

    private void sendUsertoLoginActivity() {
        Intent intent=new Intent( MainActivity.this,LoginActivity.class );
        startActivity( intent );
        finish();
    }

    public void initViews() {
        progressDialog = new ProgressDialog( this );
        progressDialog.setMessage( "Fetching Countries list ..." );
        progressDialog.setCancelable( false );
        progressDialog.show();
        recyclerView = findViewById( R.id.main_country_list );
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        recyclerView.smoothScrollToPosition( 0 );
        loadJson();
    }

    public void loadJson() {
        textView_disconnected = (TextView) findViewById(R.id.main_text_disconnected);
        try{
            com.example.shosho.loginapp.api.Service apiService =
                    Client.getClient().create( com.example.shosho.loginapp.api.Service.class);
            Call<CountryResponse> call = apiService.getitems();
            call.enqueue(new Callback<CountryResponse>() {
                @Override
                public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                    List<Item> items = response.body().getItems();
                    recyclerView.setAdapter(new ItemAdapter( items,getApplicationContext()));
                    recyclerView.smoothScrollToPosition(0);
                    swipeContainer .setRefreshing(false);
                    progressDialog.hide();
                }

                @Override
                public void onFailure(Call<CountryResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    textView_disconnected.setVisibility(View.VISIBLE);
                    progressDialog.hide();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
