package com.example.background.activities;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.background.R;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughActivity;
import com.shashank.sony.fancywalkthroughlib.FancyWalkthroughCard;

import java.util.ArrayList;
import java.util.List;

public class FancyActivity extends FancyWalkthroughActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fancy);

        FancyWalkthroughCard fancywalkthroughCard1 = new FancyWalkthroughCard("Find Restaurant", "Find the best restaurant in your neighborhood.",R.drawable.find_restaurant1);
        FancyWalkthroughCard fancywalkthroughCard2 = new FancyWalkthroughCard("Pick the best", "Pick the right place using trusted ratings and reviews.",R.drawable.pickthebest);
        FancyWalkthroughCard fancywalkthroughCard3 = new FancyWalkthroughCard("Choose your meal", "Easily find the type of food you're craving.",R.drawable.chooseurmeal);
        FancyWalkthroughCard fancywalkthroughCard4 = new FancyWalkthroughCard("Meal is on the way", "Get ready and comfortable while our biker bring your meal at your door.",R.drawable.mealisonway);

        fancywalkthroughCard1.setBackgroundColor(R.color.white);
        fancywalkthroughCard1.setIconLayoutParams(300,300,0,0,0,0);
        fancywalkthroughCard2.setBackgroundColor(R.color.white);
        fancywalkthroughCard2.setIconLayoutParams(300,300,0,0,0,0);
        fancywalkthroughCard3.setBackgroundColor(R.color.white);
        fancywalkthroughCard3.setIconLayoutParams(300,300,0,0,0,0);
        fancywalkthroughCard4.setBackgroundColor(R.color.white);
        List<FancyWalkthroughCard> pages = new ArrayList<>();

        pages.add(fancywalkthroughCard1);
        pages.add(fancywalkthroughCard2);
        pages.add(fancywalkthroughCard3);
        pages.add(fancywalkthroughCard4);

        for (FancyWalkthroughCard page : pages) {
            page.setTitleColor(R.color.black);
            fancywalkthroughCard4.setBackgroundColor(R.color.white);
            page.setDescriptionColor(R.color.black);
        }
        setFinishButtonTitle("Get Started");
        showNavigationControls(true);
        setColorBackground(R.color.colorAccent);
        //setImageBackground(R.drawable.restaurant);
        setInactiveIndicatorColor(R.color.grey_600);
        setActiveIndicatorColor(R.color.colorAccent);
        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {

    }
}