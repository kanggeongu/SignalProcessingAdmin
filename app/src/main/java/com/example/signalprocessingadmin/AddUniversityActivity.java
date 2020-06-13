package com.example.signalprocessingadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class AddUniversityActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    private ImageView image;
    Button button,mapbutton;
    EditText editName;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri selectedImageUri;
    Uri downloadUri;
    Context mContext;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_university);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        editName=(EditText)findViewById(R.id.editUvName);
        button=(Button)findViewById(R.id.ButtonUv);
        mapbutton=(Button)findViewById(R.id.ButtonMap);
        image=(ImageView)findViewById(R.id.imageView2);
        final FirebaseDatabase Database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabaseReference = Database.getReference();

        mContext=this;

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String UvName=editName.getText().toString();
                        final String UvImage=downloadUri.toString();

                        University newUv = new University(UvName,UvImage);
                        mDatabaseReference.child("Universities").child(UvName).setValue(newUv);
                        Toast.makeText(AddUniversityActivity.this, "대학교 입력 성공", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String UvName=editName.getText().toString();
                        final String MapImage=downloadUri.toString();

                        mDatabaseReference.child("Universities").child(UvName).child("mapphoto").setValue(MapImage);

                        Toast.makeText(AddUniversityActivity.this, "지도 입력 성공", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            selectedImageUri = data.getData();
            image.setImageURI(selectedImageUri);
            Log.d("이미지uri", String.valueOf(selectedImageUri));

            //알림
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("").setMessage("사진을 등록하시겠습니까");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    StorageReference storageRef = storage.getReference();
                    Log.d("이미지uri222", String.valueOf(selectedImageUri));
                    Uri file = Uri.fromFile(new File(getPath(selectedImageUri)));

                    final StorageReference UvsRef;
                    UvsRef = storageRef.child("images/"+file.getLastPathSegment());
                    final UploadTask uploadTask;
                    uploadTask = UvsRef.putFile(file);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(AddUniversityActivity.this, "실패", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            Toast.makeText(AddUniversityActivity.this, "성공", Toast.LENGTH_SHORT).show();
                            //다운로드 url 가져오기
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return UvsRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        downloadUri = task.getResult();
                                    } else {
                                        // Handle failures
                                        // ...
                                    }
                                }
                            });
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    Toast.makeText(getApplicationContext(), "취소하셧습니다", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public String getPath(Uri uri){

        String [] proj ={MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        Log.d("getPath",cursor.getString(index));
        return cursor.getString(index);
    }
}
