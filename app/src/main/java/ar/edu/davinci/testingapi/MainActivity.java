package ar.edu.davinci.testingapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public void checkConnectionOnClick(View view) {
        checkConnection();
    }

    public void checkConnection(){
        LinearLayout noInternetMessage = findViewById(R.id.noInternetMessage);
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            //Async Task
            GetRicks getRicks = new GetRicks();
            getRicks.execute("https://rickandmortyapi.com/api/character");
            noInternetMessage.setVisibility(View.INVISIBLE);
        } else {
            noInternetMessage.setVisibility(View.VISIBLE);
            //Mensaje sin internet
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        //checkConnection();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.i("firebase", "hay usuario");

        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            Log.i("firebase", "deberia logearme porque no hay usuario");
        }
    }


    public void logout (View v) {
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        Log.i("firebase", "yendo a login");
    }

}