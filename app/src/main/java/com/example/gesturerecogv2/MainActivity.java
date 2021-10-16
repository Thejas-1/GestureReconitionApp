package com.example.gesturerecogv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LocationListener {
    private Uri fileUri;
    private String selectedItem = "Turn on lights";
    private static final int VIDEO_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner symptomDropdown = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gestures, android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        symptomDropdown.setAdapter(adapter);

        symptomDropdown.setOnItemSelectedListener(this);

        Button submitButton = (Button) findViewById(R.id.button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Screen_2_Practice.class);
                Bundle b = new Bundle();
                b.putString("selectedItem", selectedItem);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(),"Current Longitute: " + location.getLongitude() + " Current Latitude: " + location.getLatitude(), Toast.LENGTH_LONG).show();
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

    /**
     Select a specific item from dropdown.
     **/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner dropDown = (Spinner) findViewById(R.id.spinner);
        selectedItem = dropDown.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
