package com.example.coronoadetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    FirebaseAuth signUpAuth;
    EditText emailSignUp,passwordSignUp,confirmPass;
    Button createAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialise();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailSignUp.length() == 0){
                    emailSignUp.requestFocus();
                    emailSignUp.setError("Field can't be Empty");
                    return;
                }else if(passwordSignUp.length() <= 6){
                    passwordSignUp.setError("Too short");
                    passwordSignUp.requestFocus();
                    return;
                }else if(!passwordSignUp.getText().toString().equals(confirmPass.getText().toString())){
                    confirmPass.requestFocus();
                    confirmPass.setError("Password don't match");
                    return;
                }

                startCreatingNewAccount(emailSignUp.getText().toString(),passwordSignUp.getText().toString());
            }
        });
    }

    private void startCreatingNewAccount(String email, String pass) {
        signUpAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this, "Account created Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                    finish();
                }else{
                    Toast.makeText(SignUp.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialise() {
        signUpAuth = FirebaseAuth.getInstance();
        emailSignUp = (EditText)findViewById(R.id.signupEmail);
        passwordSignUp = (EditText)findViewById(R.id.passwordSignUp);
        confirmPass = (EditText)findViewById(R.id.confirmPassword);
        createAccount = (Button)findViewById(R.id.createAccount);
    }
}