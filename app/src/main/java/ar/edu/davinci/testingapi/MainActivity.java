package ar.edu.davinci.testingapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private User user;

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
        db = FirebaseFirestore.getInstance();
        //checkConnection();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String uid = currentUser.getUid();

            if(currentUser.isEmailVerified()) {
                Log.i("firebase", "hay usuario");
                db
                        .collection("users")
                        //.whereEqualTo("uid", uid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot documento: task.getResult()) {
                                        String id = documento.getId();
                                        Object data = documento.getData();
                                        user = documento.toObject(User.class);


                                        db
                                                .collection("users")
                                                .document(id)
                                                .update("verificado", true);


                                        Log.i("firebase firestore", "apellido " + user.getApellido());
                                        Log.i("firebase firestore", "nombre " + user.getNombre());
                                        Log.i("firebase firestore", "verificado " + user.isVerificado());

                                        ConstraintLayout app = findViewById(R.id.app);
                                        TextView miNombre = new TextView(getApplicationContext());
                                        miNombre.setText(user.getApellido() + " " + user.getNombre());
                                        app.addView(miNombre);


                                        //Log.i("firebase firestore", "id: " + id + " data: " + data.toString());
                                    }
                                }
                            }
                        });
            } else {
                currentUser.sendEmailVerification();
            }

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