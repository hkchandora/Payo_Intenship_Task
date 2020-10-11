package com.himanshu.payo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText, pwdEditText;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.login_email_editText);
        pwdEditText = findViewById(R.id.login_password_editText);

        auth = FirebaseAuth.getInstance();
    }

    public void logIn(View view) {

        String email = emailEditText.getText().toString().trim();
        String pwd = pwdEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required");
        } else if (TextUtils.isEmpty(pwd)) {
            pwdEditText.setError("Required");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Incorrect email");
        } else {

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_progress);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            auth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, task.getException() + "", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
        }
    }

    public void signUpAccount(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}