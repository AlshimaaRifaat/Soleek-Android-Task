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

public class RegisterActivity extends AppCompatActivity {
    private EditText userEmail,userPassword,userConfirmPassword;
    private Button createAccountButton;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        firebaseAuth=FirebaseAuth.getInstance();

        userEmail=findViewById( R.id.register_email );
        userPassword=findViewById( R.id.register_password );
        userConfirmPassword=findViewById( R.id.register_confirmPassword);
        createAccountButton=findViewById( R.id.register_createAccount );
        loginButton=findViewById( R.id.register_login );

        progressDialog=new ProgressDialog( this );

        createAccountButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        } );
        loginButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUsertoLoginActivity();
            }
        } );

    }



    private void sendUsertoLoginActivity() {
        Intent intent=new Intent( RegisterActivity.this,LoginActivity.class );
        startActivity( intent );
    }


    private void createNewAccount() {
        String email=userEmail.getText().toString();
        String password=userPassword.getText().toString();
        String confirmPassword=userConfirmPassword.getText().toString();

        if(TextUtils.isEmpty( email ))
        {
            Toast.makeText( this, "Please write your email...", Toast.LENGTH_SHORT ).show();
        }
        else  if(TextUtils.isEmpty( password ))
        {
            Toast.makeText( this, "Please write your password...", Toast.LENGTH_SHORT ).show();
        }
        else  if(TextUtils.isEmpty( confirmPassword ))
        {
            Toast.makeText( this, "Please confirm your password...", Toast.LENGTH_SHORT ).show();
        }
        else  if(!password.equals(  confirmPassword))
        {
            Toast.makeText( this, "your password do not match with your confirm password ...", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            progressDialog.setTitle( "Creating New Account" );
            progressDialog.setMessage( "please wait,while we are creating new Account..." );
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside( true );
            firebaseAuth.createUserWithEmailAndPassword( email,password )
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText( RegisterActivity.this, "you are authenticated successfully...", Toast.LENGTH_SHORT ).show();
                                progressDialog.dismiss();
                            }else
                            {
                                String message=task.getException().getMessage();
                                Toast.makeText( RegisterActivity.this, "Error occured : "+message, Toast.LENGTH_SHORT ).show();
                                progressDialog.dismiss();
                            }
                        }
                    } );
        }

    }
}
