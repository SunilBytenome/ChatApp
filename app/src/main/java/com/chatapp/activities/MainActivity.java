package com.chatapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chatapp.utils.Constants;
import com.chatapp.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity{
    TextView register;
    EditText username, password;
    Button loginButton;
    String user, pass;
    private Firebase mFirebaseRef, mFirebaseRef_for_user_clubs;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = (TextView) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        Firebase.setAndroidContext(this);

        ShortcutBadger.with(getApplicationContext()).count(12);

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

       // mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://fir-chatapp-b4ba0.firebaseio.com/";
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("can't be blank");
                } else if (pass.equals("")) {
                    password.setError("can't be blank");
                } else {


                    String url = "https://chatapp-9dacf.firebaseio.com/user.json";

                    final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Toast.makeText(MainActivity.this, "response", Toast.LENGTH_SHORT).show();
                            if (s.equals("null")) {
                                Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                    } else if (obj.getJSONObject(user).getJSONObject("Detail").getString("passwor").equals(pass)) {
                                        String password=  obj.getJSONObject(user).getJSONObject("Detail").getString("passwor");
                                        String userId=  obj.getJSONObject(user).getJSONObject("Detail").getString("userId");
                                        Constants.username = user;
                                        Constants.password = password;
                                        Constants.userId = userId;
                                          startActivity(new Intent(MainActivity.this, UsersListActivity.class));
                                        Intent intent = new Intent(MainActivity.this, UsersListActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(MainActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
                    rQueue.add(request);
                }

            }
        });

    }



    /*mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange (DataSnapshot dataSnapshot){
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                //Getting the data from snapshot
                //User person = postSnapshot.getValue(User.class);

                //Adding it to a string
                // String string = "Name: "+person.getemail()+"\nAddress: "+person.getfname()+"\n"+person.getlname()+"\n"+person.getgender()+"\n"+person.mobile_number;

                //Displaying it on textview
                Log.d(">>>", "Value is: " + string);

            }

        }*/

}
