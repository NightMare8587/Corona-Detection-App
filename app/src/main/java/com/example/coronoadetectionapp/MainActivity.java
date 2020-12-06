package com.example.coronoadetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText emailLogin,passLogin;
    Button loginButton,forgotPass,createNewAccount;
    FirebaseAuth loginAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        checkPermissionForApp();
        initialise();

        if(currentUser != null){
            startActivity(new Intent(getApplicationContext(),HomePage.class));
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailLogin.length() == 0){
                    emailLogin.requestFocus();
                    emailLogin.setError("Field can't be Empty");
                    return;
                }else if(passLogin.length() == 0){
                    passLogin.requestFocus();
                    passLogin.setError("Field can't be Empty");
                    return;
                }

                startLoginProcedure(emailLogin.getText().toString(),passLogin.getText().toString());
            }
        });
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startForgotPassProcedure();
            }
        });

    }

    private void startForgotPassProcedure() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");
        final EditText input = new EditText(this);
        builder.setView(input);
        input.setHint("Enter Email");
        builder.setCancelable(true);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.length() == 0){
                    input.requestFocus();
                    input.setError("Field can't be Empty");
                    return;
                }else{
                    loginAuth.sendPasswordResetEmail(input.getText().toString()).addOnCompleteListener((Activity) getApplicationContext(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Link Sent Successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Something went Wrong!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create();
        builder.show();
    }



    private void startLoginProcedure(String email, String pass) {
        loginAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                    finish();
                }
            }
        });
    }

    private void initialise() {
        emailLogin = (EditText)findViewById(R.id.emailLogin);
        passLogin = (EditText)findViewById(R.id.passwordLogin);
        loginButton = (Button)findViewById(R.id.loginButton);
        forgotPass = (Button)findViewById(R.id.forgotPassword);
        createNewAccount = (Button)findViewById(R.id.createNewAccount);
        loginAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void checkPermissionForApp() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            System.exit(0);
        }
    }
}