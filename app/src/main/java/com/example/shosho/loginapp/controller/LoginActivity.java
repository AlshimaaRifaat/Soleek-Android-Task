package com.example.shosho.loginapp.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shosho.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText userEmail;
    private EditText userPassword;
    private Button LoginButton;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        firebaseAuth=FirebaseAuth.getInstance();

        userEmail=findViewById( R.id.login_email );
        userPassword=findViewById( R.id.login_password );
        LoginButton=findViewById( R.id.login_login );
        registerButton=findViewById( R.id.login_register );

        progressDialog=new ProgressDialog( this );

        registerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        } );
        LoginButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allowUserToLogin();
            }
        } );
    }

    private void allowUserToLogin() {



        String email=userEmail.getText().toString();
        String password=userPassword.getText().toString();

        if (TextUtils.isEmpty( email ))
        {
            Toast.makeText( this, "please,write your email...", Toast.LENGTH_SHORT ).show();
        }else if (TextUtils.isEmpty( password ))
        {
            Toast.makeText( this, "please,write your password...", Toast.LENGTH_SHORT ).show();
        }else
        {
            progressDialog.setTitle( "Login" );
            progressDialog.setMessage( "please wait,while we are allowing you to log into your Account..." );
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside( true );

            firebaseAuth.signInWithEmailAndPassword( email,password )
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                sendUserToMainActivity();
                                Toast.makeText( LoginActivity.this, "you are logged in successfully.", Toast.LENGTH_SHORT ).show();
                                progressDialog.dismiss();
                            }else
                            {
                                String message=task.getException().getMessage();
                                Toast.makeText( LoginActivity.this, "Error occured: "+message, Toast.LENGTH_SHORT ).show();
                                progressDialog.dismiss();
                            }
                        }
                    } );
        }

    }

    private void sendUserToMainActivity() {
        Intent intent=new Intent( LoginActivity.this,MainActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity( intent );
        finish();
    }

    private void sendUserToRegisterActivity() {
        Intent intent=new Intent( LoginActivity.this,RegisterActivity.class );
        startActivity( intent );
    }

}
