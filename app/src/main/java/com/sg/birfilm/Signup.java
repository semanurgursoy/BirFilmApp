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
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    TextInputLayout regFullName, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.button)));
        getSupportActionBar().setTitle("Kayıt Ol");

        regFullName = findViewById(R.id.reg_fullname);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get all the values
                String fullName = regFullName.getEditText().getText().toString();
                String userName = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();

                checkRequirement(fullName,userName,email,phoneNo,password);

                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task){
                                if(task.isSuccessful()){

                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    String userId = firebaseUser.getUid();

                                    String ppUrl = "https://firebasestorage.googleapis.com/v0/b/birfilm-1f.appspot.com/o/defpp_white.png?alt=media&token=f026a6c8-c023-4cd3-8568-d40d564bf63b";

                                    User user = new User(userId,ppUrl,fullName,userName,email,phoneNo,password);
                                    FirebaseDatabase.getInstance()
                                            .getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Signup.this,"Kullanıcı Başarıyla Kaydedildi",Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                            else{
                                                Toast.makeText(Signup.this,"Kullanıcı Kaydedilemedi",Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(Signup.this,"Kullanıcı Kaydedilemedi",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        regToLoginBtn.setOnClickListener((view)->{
            Intent intent = new Intent(Signup.this,Login.class);
            startActivity(intent);
        });
    }

    public void checkRequirement(String fullname,String username,String email,String phoneNo,String password){
        if(fullname.isEmpty()){
            regFullName.setError("Tam İsim Giriniz");
            regFullName.requestFocus();
            return;
        }
        if(username.isEmpty()){
            regUsername.setError("Kullanıcı Adı Giriniz");
            regUsername.requestFocus();
            return;
        }
        if(email.isEmpty()){
            regEmail.setError("Email Giriniz");
            regEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            regEmail.setError("Geçersiz Email Adresi");
            regEmail.requestFocus();
            return;
        }
        if(phoneNo.isEmpty()){
            regPhoneNo.setError("Telefon Numarası Giriniz");
            regPhoneNo.requestFocus();
            return;
        }
        if(password.isEmpty()){
            regPassword.setError("Şifre Giriniz");
            regPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            regPassword.setError("Şifre En Az 6 Karakter Olmalı");
            regPassword.requestFocus();
            return;
        }
    }

}
