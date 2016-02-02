package com.example.venomaddiction.cameratest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class OCRResult extends AppCompatActivity {
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrresult);
        result=(TextView) findViewById(R.id.txtResult);
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            String value=extras.get("result").toString();
            result.setText(value);
        }
    }
}
