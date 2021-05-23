package com.code.lelabtest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.code.lelabtest.R;
import com.code.lelabtest.model.Address;

public class UserDetailsActivity extends AppCompatActivity {

    private String TAG = UserDetailsActivity.class.getSimpleName();
    private Context context = UserDetailsActivity.this;

    private Toolbar toolbar;
    private TextView txtUserName,txtEmail,txtWebsite,txtAddress;
    private Intent intent;
    private String name = "",userName= "",email="",website="";

    private Address address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        intent = getIntent();
        name = intent.getStringExtra("name");
        userName = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        website = intent.getStringExtra("website");
        address = intent.getParcelableExtra("Address");
        Log.e(TAG,"address city: "+address.getCity());

        initToolbar(name);

        initViews();
    }

    private void initToolbar(String name) {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(""+name);
    }

    private void initViews(){

        txtUserName = findViewById(R.id.txt_user_name);
        txtEmail = findViewById(R.id.txt_email);
        txtWebsite = findViewById(R.id.txt_website);
        txtAddress = findViewById(R.id.txt_address);

        txtUserName.setText(""+userName);
        txtEmail.setText(""+email);
        txtWebsite.setText(""+website);
        txtAddress.setText(""+address.getStreet()+", "+address.getSuite()+", "+address.getCity()+", "+address.getZipcode());

        txtWebsite.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(website));
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            goBack();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        goBack();
    }

    private void goBack(){

        intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}