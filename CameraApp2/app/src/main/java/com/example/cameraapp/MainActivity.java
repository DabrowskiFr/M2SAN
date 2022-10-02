package com.example.cameraapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cameraapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_CAMERA_PERMISSION_CODE = 1000;
    ActivityMainBinding binding;
    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        button = binding.button;
        imageView = binding.imageView;
    }

    @Override
    public void onClick(View view) {
        // Check if the system has a camera feature
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){

            // Check if the permission is already granted
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                // if not request the permission, the request is identified by MY_CAMERA_PERMISSION_CODE
                // result handled by onRequestPermissionResult
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                //requestPermissions(Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                // otherwise, take the picture
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                // Replace startActivityForResult, which is deprecated, see someActivityResultLauncher bellow
                someActivityResultLauncher.launch(cameraIntent);
            }
        } else {
            // No Camera
            Toast.makeText(this, "no camera", Toast.LENGTH_LONG).show();
        }
    }

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                // Replace startActivityForResult, which is deprecated, see someActivityResultLauncher bellow
                someActivityResultLauncher.launch(cameraIntent);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }*/

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted){
                        Toast.makeText(MainActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        // Replace startActivityForResult, which is deprecated, see someActivityResultLauncher bellow
                        someActivityResultLauncher.launch(cameraIntent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "camera permission denied", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data  = result.getData();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(photo);
                    }
                }
            }
    );
}