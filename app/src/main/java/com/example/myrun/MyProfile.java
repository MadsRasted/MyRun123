package com.example.myrun;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity implements Updatable {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("profile");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;

    private ImageView imageView;
    private Bitmap bitmap;
    private EditText name;
    private EditText email;
    private EditText age;
//    private static final int IMAGE_REQUEST = 2;
//    private static final int PICK_IMAGE_REQUEST = 1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        imageView = findViewById(R.id.profilePic);

        storageReference = FirebaseStorage.getInstance().getReference().child("image.png");
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                chooseProfilePicture();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                String nameTxt = snapshot.child("name").getValue(String.class);
                String emailTxt = snapshot.child("email").getValue(String.class);
                String ageTxt = snapshot.child("age").getValue(String.class);

                name.setText(nameTxt);
                email.setText(emailTxt);
                age.setText(ageTxt);
//                loadBitmap();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed : " + error.getCode());
            }
        });
    }

//    private void openImage(){
//        Intent intent = new Intent();
//        intent.setType("image.png");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_REQUEST);
//    }


    private void chooseProfilePicture(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfile.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture, null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        ImageView imageViewADPPCamera = dialogView.findViewById(R.id.imageViewADPPCamera);
        ImageView imageViewADPPGallery = dialogView.findViewById(R.id.imageViewADPPGallery);

        final AlertDialog alertDialogProfilePic = builder.create();
        alertDialogProfilePic.show();

        imageViewADPPCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(checkAndRequestPermissions()){
                    takePictureFromCamera();
                    alertDialogProfilePic.cancel();
                }
            }
        });
        imageViewADPPGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
                alertDialogProfilePic.cancel();

            }
        });
    }

    private void takePictureFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void takePictureFromCamera(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePicture, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    imageView.setImageURI(selectedImageUri);
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImg = (Bitmap) bundle.get("data");
                    imageView.setImageBitmap(bitmapImg);
                    uploadBitmap(bitmapImg);
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions(){
        if(Build.VERSION.SDK_INT >= 23){
            int cameraPermission = ActivityCompat.checkSelfPermission(MyProfile.this, Manifest.permission.CAMERA);
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(MyProfile.this, new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePictureFromCamera();
        }else
            Toast.makeText(MyProfile.this, "Permission not granted", Toast.LENGTH_SHORT).show();
    }

    public void SaveProfilePressed(View view){
        String nameTxt = name.getText().toString();
        String emailTxt = email.getText().toString();
        String ageTxt = age.getText().toString();

        Map<String, String> profileMap = new HashMap<>();

        profileMap.put("name", nameTxt);
        profileMap.put("email", emailTxt);
        profileMap.put("age", ageTxt);

        myRef.setValue(profileMap);

        System.out.println("done save");
    }

    public void uploadBitmap(Bitmap bitmap){
        System.out.println("test");
        StorageReference ref = storage.getReference("image.png");
        ByteArrayOutputStream baoas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baoas);
        ref.putBytes(baoas.toByteArray()).addOnCompleteListener(snap -> {
            System.out.println("Upload complete " + snap);
        }).addOnFailureListener(exception -> {
            System.out.println("Upload failed "+ exception);
        });
    }

    @Override
    public void update(Object o) {

    }
}
