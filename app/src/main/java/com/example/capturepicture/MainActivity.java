package com.example.capturepicture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kotlin.jvm.Throws;

public class MainActivity extends AppCompatActivity {

    File photoFile=null;
    EditText e1;
    Button btnCapture,btnSave,btnLoad,btnShow;
    String currentPhotoPath;
    public static final int REQUEST_IMAGE_CAPTURE=1;
    AppDatabase appDatabase;
    ModelDao modelDao;
    AdapterClass adapterClass;
    private Executor executor= Executors.newSingleThreadExecutor();
    ImageModel model;
    ViewPager2 viewPager;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase=AppDatabase.getInstance(getApplicationContext());
        e1=findViewById(R.id.txtTag);
        btnCapture=findViewById(R.id.btnCapture);
        btnSave=findViewById(R.id.btnSave);
        btnLoad=findViewById(R.id.btnLoad);
        btnShow=findViewById(R.id.btnShow);
        img=findViewById(R.id.imageView);
        viewPager=findViewById(R.id.viewPager1);
        modelDao= appDatabase.modelDao();



        btnShow.setOnClickListener(view -> {
            executor.execute(()->{
                List<ImageModel>getListForLog=modelDao.getAll();
                Log.v("Data: ", "This is all data from database");
                for(ImageModel image : getListForLog ){
                    Log.v("Data: ","ID: "+image.getId()+" Path: "+image.getImageCaptured()+" Tags: "+image.getTag());
                }
            });
        });
        btnLoad.setOnClickListener(view -> {

            String enteredText = e1.getText().toString();
            List<String> tags = Arrays.asList(enteredText.split(";"));
            executor.execute(() -> {
                List<ImageModel> images = modelDao.image(tags);
                for (ImageModel image : images) {
                    Log.v("Data: ","ID: "+image.getId()+" Path: "+image.getTag()+" Tags: "+image.getTag());
                }
                runOnUiThread(() -> {
                    adapterClass = new AdapterClass(images);
                    viewPager.setAdapter(adapterClass);

                });
            });
        });


        btnCapture.setOnClickListener(view -> {
            img.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            dispatchTakePicture();

        });

        btnSave.setOnClickListener(view -> {
            if (photoFile != null) {
                processEnteredModel();
            } else {
                Toast.makeText(this, "Take a photo first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addData(String tag){
        if (model != null) {
            executor.execute(() -> {
                ModelDao modelDao1 = appDatabase.modelDao();
                ImageModel newModel = new ImageModel(currentPhotoPath, tag);
                modelDao1.insertData(newModel);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
                });
            });
        } else {
            Toast.makeText(this, "No data to save", Toast.LENGTH_SHORT).show();
        }
    }

    private void processEnteredModel() {
        String enteredText = e1.getText().toString().trim();

        if (!enteredText.isEmpty()) {
            String[] tags = enteredText.split(";");

            for (String tag : tags) {
                tag = tag.trim();
                if (!tag.isEmpty()) {
                    addData(tag);
                }
            }

            img.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            e1.getText().clear();
        }
    }


    private File createImageFile() throws IOException{

        String timeStamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName= "JPEG_" +timeStamp + "_";
        File storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null) {
            Log.e("MainActivity", "External storage directory is null");
        }
        File image=File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath=image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePicture(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_IMAGE_CAPTURE);
        }else{
            Intent takePicture= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePicture.resolveActivity(getPackageManager()) !=null){

                try{
                    photoFile=createImageFile();
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.example.capturepicture.FileProvider",
                                photoFile);
                        takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);


                    }
                }catch (IOException ex){
                    Log.d(ex.getMessage(),"Big one");
                    Toast.makeText(this, "Problem: "+ex, Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String path = photoFile.getAbsolutePath();
            model = new ImageModel(path, e1.getText().toString());
            img.setImageURI(Uri.fromFile(new File(currentPhotoPath)));
            Toast.makeText(this, "Photo captured", Toast.LENGTH_SHORT).show();

        }
    }

}