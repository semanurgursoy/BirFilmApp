package com.sg.birfilm.tabs.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.sg.birfilm.Home;
import com.sg.birfilm.R;
import com.theartofdev.edmodo.cropper.CropImage;

public class SetPP extends AppCompatActivity {

    Uri photoUri;
    String myUri="";

    StorageTask uploadTask;
    StorageReference uploadRef;

    ImageView imageClose , imageAdded;
    TextView txtSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pp);

        imageClose = findViewById(R.id.post_close);
        imageAdded = findViewById(R.id.post_added_photo);
        txtSend = findViewById(R.id.post_txt_send);

        uploadRef = FirebaseStorage.getInstance().getReference("profiles");

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetPP.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });

        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(SetPP.this);
    }

    private void uploadPhoto(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Profil Fotoğrafı Ayarlanıyor..");
        progressDialog.show();

        // foto yükleme kodları
        if(photoUri != null){
            StorageReference filePath = uploadRef.child(System.currentTimeMillis()
                    +"."+getFileExtension(photoUri));
            uploadTask = filePath.putFile(photoUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();

                        FirebaseDatabase.
                                getInstance()
                                .getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("ppUrl").setValue(myUri);

                        progressDialog.dismiss();

                        Intent intent = new Intent(SetPP.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(SetPP.this,"Gönderme Başarısız",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SetPP.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(SetPP.this,"Seçili Fotoğraf Yok",Toast.LENGTH_LONG).show();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            photoUri = result.getUri();

            imageAdded.setImageURI(photoUri);
        }
        else{
            Toast.makeText(this,"Fotoğraf Seçilemedi",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SetPP.this, Home.class);
            startActivity(intent);
            finish();
        }
    }
}