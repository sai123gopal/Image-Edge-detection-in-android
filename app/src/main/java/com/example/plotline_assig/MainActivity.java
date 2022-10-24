package com.example.plotline_assig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    static {
        if(OpenCVLoader.initDebug()){
            Log.d("OpenCv sai","Loaded");
        }else {
            Log.e("OpenCv sai","Error");
        }
    }
    String OriginalUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText url = findViewById(R.id.url);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        findViewById(R.id.get_url).setOnClickListener(view -> {
            if(url.getText().toString().trim().isEmpty()){
                Toast.makeText(MainActivity.this, "Enter url", Toast.LENGTH_SHORT).show();
            }else {
                progressDialog.show();
                Glide.with(this)
                        .asBitmap()
                        .load(url.getText().toString().trim())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                progressDialog.dismiss();
                                saveImageToGallery(resource,"Original");
                                convert(resource);
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                progressDialog.dismiss();
                            }
                        });
            }
        });

        findViewById(R.id.results).setOnClickListener(view -> startActivity(new Intent(this,ResultActivity.class)));

        findViewById(R.id.select).setOnClickListener(view -> {
            startActivityForResult(new Intent(Intent.ACTION_PICK),0);
        });


        findViewById(R.id.open_cam).setOnClickListener(view -> {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 1);
        });

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},10);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 || requestCode ==1){
            if(resultCode==Activity.RESULT_OK){
                Bitmap bitmap = null;
                if(requestCode == 0) {
                    try {
                        Uri imageUri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(requestCode == 1){
                    bitmap = (Bitmap) data.getExtras().get("data");
                }
                saveImageToGallery(bitmap,"Original");
                convert(bitmap);
            }
        }

    }


    public void convert(Bitmap bitmap){
        Mat rgbMat = new Mat();
        Utils.bitmapToMat(bitmap, rgbMat);

        Mat grayMat = new Mat();
        Mat bwMat = new Mat();

        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY,4);
        Imgproc.equalizeHist(grayMat, grayMat);
        Imgproc.Canny(grayMat, bwMat, 50, 200, 3, false);

        ImageView imageView = findViewById(R.id.img);
        Utils.matToBitmap(bwMat,bitmap);

        Matrix matrix = new Matrix();
        matrix.postRotate(180);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        saveImageToGallery(bitmap,"Converted");
        imageView.setImageBitmap(bitmap);
        imageView.setRotation(180);
    }

    private void saveImageToGallery(Bitmap bitmap,String type){

        OutputStream fos;
        String filename = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", new Date()));;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues =  new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename+".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator +"plotline/"+type+"/");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);

                if(type.equals("Original")){
                    OriginalUri = imageUri.toString();
                }else {
                   new HistoryDbClient(this).insert(new HistoryModel(OriginalUri,imageUri.toString()));
                }
                Toast.makeText(this, "Images Saved at Picture/Plotline folder", Toast.LENGTH_SHORT).show();
            }

        }catch(Exception e){
            Log.e("Image not saved",e.toString());
        }

    }



}