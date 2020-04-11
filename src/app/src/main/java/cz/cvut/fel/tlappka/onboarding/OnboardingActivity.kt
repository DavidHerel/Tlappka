package cz.cvut.fel.tlappka.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.login.LoginActivity
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {
    private var onboardingAdapter: OnboardingAdapter? = null;
    private var layoutOnboardingIndicators : LinearLayout? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);

        setupOnboardingItems();

        val onBoardingViewPager: ViewPager2 = findViewById(R.id.onBoardingViewPager);
        onBoardingViewPager.adapter = onboardingAdapter;

        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener {
            if (onBoardingViewPager.currentItem + 1 < onboardingAdapter?.itemCount!!) {
                onBoardingViewPager.currentItem += 1;
            } else {
                Intent(applicationContext, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish();
            }
        }

        skipIntro.background = null;
        skipIntro.setOnClickListener {
                Intent(applicationContext, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish();
        }
    }

    private fun setupOnboardingItems(){
        val onboardingItems :ArrayList<OnboardingItem> = ArrayList();

        val onboardingItemFirst: OnboardingItem = OnboardingItem();
        onboardingItemFirst.title= resources.getString(R.string.onboarding_title_one);
        onboardingItemFirst.description = resources.getString(R.string.onboarding_desc_one);
        onboardingItemFirst.image = R.drawable.onboarding_first;

        val onboardingItemSecond: OnboardingItem = OnboardingItem();
        onboardingItemSecond.title= resources.getString(R.string.onboarding_title_two);
        onboardingItemSecond.description = resources.getString(R.string.onboarding_desc_two);
        onboardingItemSecond.image = R.drawable.onboarding_second;

        val onboardingItemThird: OnboardingItem = OnboardingItem();
        onboardingItemThird.title= resources.getString(R.string.onboarding_title_three);
        onboardingItemThird.description = resources.getString(R.string.onboarding_desc_three);
        onboardingItemThird.image = R.drawable.onboarding_third;

        val onboardingItemFour: OnboardingItem = OnboardingItem();
        onboardingItemFour.title= resources.getString(R.string.onboarding_title_four);
        onboardingItemFour.description = resources.getString(R.string.onboarding_desc_four);
        onboardingItemFour.image = R.drawable.onboarding_fourth;


        onboardingItems.add(onboardingItemFirst);
        onboardingItems.add(onboardingItemSecond);
        onboardingItems.add(onboardingItemThird);
        onboardingItems.add(onboardingItemFour);

        onboardingAdapter = OnboardingAdapter(onboardingItems);
    }

    private fun setupOnboardingIndicators(){
        var indicators : ImageView;
        var layoutParams : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        var size = onboardingAdapter?.itemCount;
        for (i in 0 until size!!){
            indicators = ImageView(applicationContext);
            indicators.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                R.drawable.onboarding_indicator_inactive
            ));
            indicators.layoutParams = layoutParams;
            layoutOnboardingIndicators?.addView(indicators);
        }

    }

    private fun setCurrentOnboardingIndicator(index : Int){
        var childCount : Int? = layoutOnboardingIndicators?.childCount
        for(i in 0 until childCount!!){
            var imageView : ImageView = layoutOnboardingIndicators?.getChildAt(i) as ImageView;
            if (i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.onboarding_indicator_active
                ))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.onboarding_indicator_inactive
                ))
            }
        }
    }

}
