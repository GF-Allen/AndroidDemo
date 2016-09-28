package com.example.priceanimtead;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.priceanimtead.view.PriceAnimtedView;

public class MainActivity extends AppCompatActivity {

    private PriceAnimtedView pav;
    private PriceAnimtedView pav2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pav = (PriceAnimtedView) findViewById(R.id.pav);
        pav2 = (PriceAnimtedView) findViewById(R.id.pav2);
    }

    public void click(View view) {
        pav.setNumberText("012");
        pav2.setNumberText("09");
        pav.play();
        pav2.play();
    }
}
