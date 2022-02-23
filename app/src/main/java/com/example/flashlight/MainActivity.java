package com.example.flashlight;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //declare properties
    private Button off;
    private ImageView ImageView;
    //request camera permissions
    private static final int CAMERA_REQUEST = 50;
    private boolean flashLightStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView=(ImageView)findViewById(R.id.ImageView);
        off = (Button) findViewById(R.id.off);
//check if it has a camera
        final boolean hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        //check if camera permissions are already guaranteed by the users
        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                ==PackageManager.PERMISSION_GRANTED;
        //we change according to the camera authorization
        off.setEnabled(!isEnabled);
        ImageView.setEnabled(isEnabled);
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
            }
        });
//IMAGE onClickListener
        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasCameraFlash){
                    if(flashLightStatus)
                         flashLightOff();
                    else
                        flashLightOn();
                }else{
                    Toast.makeText(MainActivity.this, "No flash available on your device", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
        private void flashLightOn(){
            CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId,true);
                    ImageView.setImageResource(R.drawable.on);
                } catch (CameraAccessException e) {

                }
            }
            private void flashLightOff(){
                CameraManager cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
                    try {
                        String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId,false);
                        ImageView.setImageResource(R.drawable.off);
                    } catch (CameraAccessException e) {

                    }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_REQUEST:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    off.setEnabled(false);
                    off.setText("Camera Enabled");
                    ImageView.setEnabled(true);
                }else {
                    Toast.makeText(this, "Permission Denied for the Camera", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
