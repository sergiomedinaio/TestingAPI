package ar.edu.davinci.testingapi;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public void checkConnection(){
        LinearLayout noInternetMessage = findViewById(R.id.noInternetMessage);
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            //Async Task
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
        checkConnection();
    }
}