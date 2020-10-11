package com.himanshu.payo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himanshu.payo.model.Users;

public class SignUpActivity extends AppCompatActivity {

    private EditText fNameEditText, lNameEditText, emailEditText, pwdEditText, cPwdEditText, mobileEditText, addEditText;
    private CheckBox checkBox;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fNameEditText = findViewById(R.id.first_name_editText);
        lNameEditText = findViewById(R.id.last_name_editText);
        emailEditText = findViewById(R.id.email_editText);
        pwdEditText = findViewById(R.id.password_editText);
        cPwdEditText = findViewById(R.id.confirm_password_editText);
        mobileEditText = findViewById(R.id.mobile_number_editText);
        addEditText = findViewById(R.id.address_editText);
        checkBox = findViewById(R.id.checkBox);

        cPwdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!cPwdEditText.getText().toString().equals(pwdEditText.getText().toString())) {
                        cPwdEditText.setError("Password and Confirm password do not match");
                        cPwdEditText.setText("");
                    }
                }
            }
        });

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
    }

    public void logInAccount(View view) {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {

        final String fName = fNameEditText.getText().toString().trim();
        final String lName = lNameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String pwd = cPwdEditText.getText().toString().trim();
        final String mobile = mobileEditText.getText().toString().trim();
        final String address = addEditText.getText().toString().trim();

        if (TextUtils.isEmpty(fName)) {
            fNameEditText.setError("Required");
        } else if (TextUtils.isEmpty(lName)) {
            lNameEditText.setError("Required");
        } else if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required");
        } else if (pwdEditText.getText().toString().isEmpty()) {
            pwdEditText.setError("Required");
        } else if (TextUtils.isEmpty(pwd)) {
            cPwdEditText.setError("Required");
        } else if (TextUtils.isEmpty(mobile)) {
            mobileEditText.setError("Required");
        } else if (TextUtils.isEmpty(address)) {
            addEditText.setError("Required");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Incorrect email");
        } else if (pwd.length() < 6) {
            pwdEditText.setError("Must be greater than 6 character");
        } else if (mobile.length() < 10) {
            cPwdEditText.setError("Incorrect");
        } else if (!checkBox.isChecked()) {
            Toast.makeText(this, "Accept our Terms and Services", Toast.LENGTH_SHORT).show();
        } else {

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_progress);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            auth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Users member = new Users();
                                member.setfName(fName);
                                member.setlName(lName);
                                member.setEmail(email);
                                member.setMobileNo(mobile);
                                member.setAddress(address);
                                reference.child("Users").child(task.getResult().getUser().getUid()).setValue(member);
                                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException() + "", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
            finish();
        }
    }
}