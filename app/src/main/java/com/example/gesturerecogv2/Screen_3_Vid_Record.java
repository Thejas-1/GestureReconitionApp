package com.example.gesturerecogv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Screen_3_Vid_Record extends AppCompatActivity implements LocationListener {

    private Uri fileUri;
    private String vidFileName = "";
    private static final int VIDEO_CAPTURE = 101;
    private int vidindex = 0;
    public void provideStoragePermissions(Activity activity) {

        /*ActivityCompat.checkSelfPermission(activity, "WRITE");
        ActivityCompat.checkSelfPermission(activity, "WRITEPERMISSIONS");*/
        int storagePermissionForAndroid = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        String[] StoragePer = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA

        };

        if (storagePermissionForAndroid != PackageManager.PERMISSION_GRANTED) {
            Log.i("log", "Read/Write Permissions needed!");
        }

        ActivityCompat.requestPermissions(
                activity,
                StoragePer,
                1
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_3__vid__record);

        System.out.println("Intent Values"+getIntent().getExtras().getString("selectedItem"));
        vidFileName = getIntent().getExtras().getString("selectedItem");

        Button record = (Button) findViewById(R.id.button5);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        //Upload the recorded video to server
        Button upload = (Button) findViewById(R.id.button6);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provideStoragePermissions(Screen_3_Vid_Record.this);
                OkHttpClient httpClient = new OkHttpClient();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;

                byte[] byteArray = stream.toByteArray();
                Global g = Global.getInstance();
                List<File> listOfVideos = new ArrayList<>();
                File root=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/my_folder/videoFile.mp4");  //you can replace RecordVideo by the specific folder where you want to save the video
                int temporary = g.getGestureNumber(vidFileName)-1-g.getCurPos();
                System.out.println("g.getCurPos:"+g.getCurPos()+" g.getGestureNumber(vidFileName):"+g.getGestureNumber(vidFileName)+" g.getCounter(g.getGestureNumber(vidFileName)-1):"+g.getCounter(g.getGestureNumber(vidFileName)-1));
                System.out.println(temporary);
                File vidFiles[] = new File[g.getCounter(g.getGestureNumber(vidFileName)-1)-g.getCurPos()];

                for(int i=g.getCurPos();i<g.getCounter(g.getGestureNumber(vidFileName)-1); i++){


                    int temp = i+1;
                    vidFiles[i-g.getCurPos()] = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/my_folder/"+g.getGestureNameMapper(vidFileName)+"_PRACTICE_"+temp+"_Naik.mp4");  //you can replace RecordVideo by the specific folder where you want to save the video
                }

                System.out.println("g.getCurPos:"+g.getCurPos()+" g.getGestureNumber(vidFileName):"+g.getGestureNumber(vidFileName)+" g.getCounter(g.getGestureNumber(vidFileName)-1):"+g.getCounter(g.getGestureNumber(vidFileName)-1));
                for(int i = g.getCurPos();i<g.getCounter(g.getGestureNumber(vidFileName)-1);i++) {
                    RequestBody postBodyImage = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("video_file", vidFiles[i-g.getCurPos()].getName(), RequestBody.create(MediaType.parse("/"), vidFiles[i-g.getCurPos()]))
                            .build();
                    Request request = new Request.Builder().url("http://192.168.1.9:5000/").post(postBodyImage).build();
                    httpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            System.out.println("Network Not Found");
                            //Toast.makeText(Screen_3_Vid_Record.this,"Network Not Found",Toast.LENGTH_LONG);

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            System.out.println("Network Found" + response.body().string());
                            //Toast.makeText(Screen_3_Vid_Record.this,response.body().string(),Toast.LENGTH_LONG);

                        }
                    });
                }
                Intent intent = new Intent(Screen_3_Vid_Record.this, MainActivity.class);
                startActivity(intent);
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

    /**
     * Record the video with front camera for a duration of 5 seconds
     */
    public void startRecording()
    {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,5);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

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

    /**
     * Save the Video after recording to then use it to send it to server
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                    Global g = Global.getInstance();
                    g.setCounter(g.getGestureNumber(vidFileName)-1);
                    file=new File(root,g.getGestureNameMapper(vidFileName) + "_PRACTICE_" + (g.getCounter(g.getGestureNumber(vidFileName)-1)) +"_Naik.mp4");

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