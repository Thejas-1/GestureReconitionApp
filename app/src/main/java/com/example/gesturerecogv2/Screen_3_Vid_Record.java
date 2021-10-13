package com.example.gesturerecogv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Screen_3_Vid_Record extends AppCompatActivity implements LocationListener {

    private Uri fileUri;
    private static final int VIDEO_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_3__vid__record);

        Button record = (Button) findViewById(R.id.button5);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        Button upload = (Button) findViewById(R.id.button6);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient httpClient = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.1.9:5000/").build();
                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Network Not Found");
                        //Toast.makeText(Screen_3_Vid_Record.this,"Network Not Found",Toast.LENGTH_LONG);

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        System.out.println("Network Found"+response.body().string());
                        //Toast.makeText(Screen_3_Vid_Record.this,response.body().string(),Toast.LENGTH_LONG);

                    }
                });
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(),"Current Longitute: " + location.getLongitude() + " Current Latitude: " + location.getLatitude(), Toast.LENGTH_LONG).show();
    }

    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        } else {
            return false;
        }
    }

    public void startRecording()
    {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, VIDEO_CAPTURE);
        }

    }

    protected void onPostExecute(String text){
        VideoView vv = (VideoView) findViewById(R.id.videoView);
        vv.setVideoPath(Environment.getExternalStorageDirectory()+"/my_folder/Action1.mp4");
        vv.start();
        Button bt4 = (Button)findViewById(R.id.button3);
        bt4.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {



                try {
                    AssetFileDescriptor videoAsset = null;
                    String extStorageState = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
                        System.out.println("Yes");
                    }

                    videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");

                    FileInputStream fis = videoAsset.createInputStream();
                    File root=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/my_folder/");  //you can replace RecordVideo by the specific folder where you want to save the video
                    if (!root.exists()) {
                        System.out.println("No directory");
                        root.mkdirs();
                    }

                    File file;
                    file=new File(root,"videoFile.mp4" );

                    FileOutputStream fos = null;

                    fos = new FileOutputStream(file);


                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = fis.read(buf)) > 0) {
                        try {
                            fos.write(buf, 0, len);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                /*Toast.makeText(this, "Video has been saved to:\n" +
                        rootPath.toString(), Toast.LENGTH_LONG).show();*/

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}