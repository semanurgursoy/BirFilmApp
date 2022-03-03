package com.sg.birfilm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText emailEditText;
    Button resetPasswordButton;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.button)));
        getSupportActionBar().setTitle("Şifremi Unuttum");

        emailEditText = (EditText) findViewById(R.id.fp_email);
        resetPasswordButton = (Button) findViewById(R.id.reset_pw_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    public void reset(View view){
        String email = emailEditText.getText().toString();
        if(email.isEmpty()){
            emailEditText.setError("Email Adresinizi Giriniz");
            emailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Geçersiz Email Adresi");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Şifrenizi Sıfırlamak İçin Emailinizi Kontrol Ediniz",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
                else
                    Toast.makeText(ForgotPassword.this,"Bir Hata Oluştu! Tekrar Deneyiniz",Toast.LENGTH_LONG).show();
            }
        });
    }
}