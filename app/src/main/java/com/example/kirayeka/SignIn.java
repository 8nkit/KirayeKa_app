package com.example.kirayeka;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kirayeka.Checker.Checker;
import com.example.kirayeka.DemoStructure.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn;
    TextView forgotpas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = findViewById(R.id.edtph);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnsignin);
        forgotpas=(TextView)findViewById(R.id.forgotpas22);

forgotpas.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        openacti();
    }
});

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Checker.isConnectedToInterner(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Please Wating...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //check if user not exist in database
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                //get user information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.setPhone(edtPhone.getText().toString());
                                if (user.getPassword().equals(edtPassword.getText().toString())) {

                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Checker.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                    table_user.removeEventListener(this);

                                } else {
                                    Toast.makeText(SignIn.this, "Wrong Password !", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignIn.this, "User not exist in Database !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                else
                {
                    Toast.makeText(SignIn.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
    public void openacti()
    {
        Intent forgetps = new Intent(SignIn.this, forgotpass.class);
        startActivity(forgetps);

    }
}
