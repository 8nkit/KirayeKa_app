package com.example.kirayeka;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kirayeka.Checker.Checker;
import com.example.kirayeka.DemoStructure.ReturnReq;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import static com.example.kirayeka.R.*;
public class ReturnItem extends AppCompatActivity {
    MaterialEditText rentid, pickupaddress, feedback1;
    Button ReturnRequest;
    int j=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_return_item);
        rentid = findViewById(id.rentid);
        pickupaddress = findViewById(id.pickupaddress);
        feedback1 = findViewById(id.feedback1);
        ReturnRequest = findViewById(id.returnrequest);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("ReturnRequest");

        ReturnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Checker.isConnectedToInterner(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(ReturnItem.this);
                    mDialog.setMessage("Please Wating...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if user not exist in database
                            if (!dataSnapshot.child(rentid.getText().toString()).exists()) {
                                //get user information
                                mDialog.dismiss();
                                ReturnReq req1 = new ReturnReq(pickupaddress.getText().toString(), feedback1.getText().toString());
                                table_user.child(rentid.getText().toString()).setValue(req1);
                                j=2;
                            //    Toast.makeText(ReturnItem.this, "Return Requests Sent !", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {

                                //Toast.makeText(ReturnItem.this, "Return request exists already !", Toast.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }
                            if(j==1)
                            Toast.makeText(ReturnItem.this, "Return request exists already !", Toast.LENGTH_SHORT).show();
                            if(j==2)
                                Toast.makeText(ReturnItem.this, "Return Requests Sent !", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(ReturnItem.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;

                }
            }
        });

    }
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //  Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}


