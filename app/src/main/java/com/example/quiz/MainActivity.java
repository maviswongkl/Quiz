package com.example.quiz;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnRetrieve;
    TextView tvDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRetrieve = findViewById(R.id.btnRetrieve);
        tvDisplay = findViewById(R.id.tvDisplay);

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS);

                if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 0);
                    return;
                }

                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"address", "body"};

                String filter = "address LIKE ? and body like ?";
                String[] args = {"%66%", "%RP"};
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(uri, reqCols, filter, args, null);
                String smsBody = "";
                if (cursor.moveToFirst()){
                    do{

                        String address = cursor.getString(0);
                        String body = cursor.getString(1);

                        smsBody += address + ": " + body + "\n";
                    }while (cursor.moveToNext());
                }
                tvDisplay.setText(smsBody);
            }
        });
    }
}