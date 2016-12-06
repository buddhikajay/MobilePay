package com.example.buddhikajay.mobilepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class finishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Button btn=(Button)findViewById(R.id.finish_btn);
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                finish();
                    Intent myIntent = new Intent(finishActivity.this, scanActivity.class);
                finishActivity.this.startActivity(myIntent);



            }
        });
    }
}
