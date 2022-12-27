package com.example.miniproject1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {

    Button click;
    TextView showText;
    ImageView showImage;
    Activity activity;
    private static final int CAMERA_REQUEST =10;
    private static final int PERMISSION_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;

        click = findViewById(R.id.button);
        showText = findViewById(R.id.textView);
        showImage = findViewById(R.id.imageView);

        click.setOnClickListener(v ->{
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] {Manifest.permission.CAMERA}, PERMISSION_CODE);
            }else{
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity,"granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity,"not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST && resultCode==Activity.RESULT_OK){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            showImage.setImageBitmap(image);
            textDetece();
        }
    }
    public void textDetece(){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(activity).build();
        Bitmap bitmap = ((BitmapDrawable)showImage.getDrawable()).getBitmap();

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<TextBlock> sparseArray = textRecognizer.detect(frame);

        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0;i<sparseArray.size();i++){
            TextBlock textBlock = sparseArray.get(i);
            String str = textBlock.getValue();
            stringBuilder.append(str);
        }
        showText.setText(stringBuilder);
    }
}