package com.sg.birfilm;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button callsSignUp,loginBtn,forgotPwBtn;
    TextInputLayout logEmail, logPassword;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.button)));
        getSupportActionBar().setTitle("Giriş Yap");

        callsSignUp = (Button) findViewById(R.id.log_signup_btn);
        loginBtn = (Button) findViewById(R.id.login_btn) ;
        forgotPwBtn = (Button) findViewById(R.id.log_forgot_password);
        logEmail = findViewById(R.id.log_email);
        logPassword = findViewById(R.id.log_password);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener((view)->{
            String email = logEmail.getEditText().getText().toString();
            String password = logPassword.getEditText().getText().toString();

            checkRequirement(email,password);

            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user.isEmailVerified()){
                                    Intent intent = new Intent(Login.this, Home.class);
                                    startActivity(intent);
                                }else{
                                    user.sendEmailVerification();
                                    Toast.makeText(Login.this,"Hesabını Doğrulamak İçin Emailinizi Kontrol Ediniz",Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(Login.this,"Giriş Yapma Başarısız",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

        callsSignUp.setOnClickListener((view)->{
            Intent intent = new Intent(Login.this,Signup.class);
            startActivity(intent);
        });

        forgotPwBtn.setOnClickListener((view)-> {
            Intent intent = new Intent(Login.this, ForgotPassword.class);
            startActivity(intent);
        });
    }

    public void checkRequirement(String email,String password){
        if(email.isEmpty()){
            logEmail.setError("Email Giriniz");
            logEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            logEmail.setError("Geçersiz Email Adresi");
            logEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            logPassword.setError("Şifre Giriniz");
            logPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            logPassword.setError("Şifre En Az 6 Karakter Olmalı");
            logPassword.requestFocus();
            return;
        }
    }

}
