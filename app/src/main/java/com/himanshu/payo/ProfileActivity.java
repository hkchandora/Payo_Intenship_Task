package com.himanshu.payo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, email, phoneNo, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        phoneNo = findViewById(R.id.profile_phone_no);
        address = findViewById(R.id.profile_address);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            name.setText(snapshot.child("fName").getValue() + " " + snapshot.child("lName").getValue());
                            email.setText("Email : "+snapshot.child("email").getValue().toString());
                            phoneNo.setText("Phone No : "+snapshot.child("mobileNo").getValue().toString());
                            address.setText("Address : "+snapshot.child("address").getValue().toString());
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public void logOut(View view) {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle("Log Out");
        alertBox.setMessage("Are you sure you want to log out ?");
        alertBox.setNegativeButton("No", null);
        alertBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        alertBox.show();
    }

    public void backArrow(View view) {
        onBackPressed();
    }
}