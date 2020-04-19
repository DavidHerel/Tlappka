package cz.cvut.fel.tlappka.onboarding

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.login.LoginActivity
import cz.cvut.fel.tlappka.login.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding.*


/**
 * Activity that handles onboarding through viewpager2
 */
class OnboardingActivity : AppCompatActivity() {
    private var onboardingAdapter: OnboardingAdapter? = null;
    private var layoutOnboardingIndicators : LinearLayout? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        if (loadSavedPreferences()) { //kdyz jsem tu porpve ulozim ze jsem uz byl
            savePreferences()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //set up indicators (4 nowadays)
        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);

        //fill onBoardingitems with image,title and description
        setupOnboardingItems();

        //send filled onboarding items to viewpager2
        val onBoardingViewPager: ViewPager2 = findViewById(R.id.onBoardingViewPager);
        onBoardingViewPager.adapter = onboardingAdapter;

        //now set up Indicators
        setupOnboardingIndicators();
        //set first indicator as first
        setCurrentOnboardingIndicator(0);

        //allow user to swipe between indicators and onboarding items
        onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicator(position);
            }
        });

        //if user clicked on button Next lets show next indicator with onboardingitem
        buttonOnboardingAction.setOnClickListener {
            //increase current item by 1
            if (onBoardingViewPager.currentItem + 1 < onboardingAdapter?.itemCount!!) {
                onBoardingViewPager.currentItem += 1;
            } else {
                //when we are on the end start Login activity
                Intent(applicationContext, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish();
            }
        }

        //hide button background to look like textview (for some reason I couldnt clicked on textview..dont know why)
        skipIntro.background = null;

        //if user wants to skip intro
        skipIntro.setOnClickListener {
                Intent(applicationContext, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish();
        }
    }

    /*
    Function that setups all onboarding items
     */
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

    /**
     * Function that setups onboarding indicators
     */
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

    /**
     * Set current onboarding indicator
     * typical start with first (0)
     */
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

    /*
Metoda ktera nacte jestli byl login rozkliknute poprve
*/
    private fun loadSavedPreferences(): Boolean {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getBoolean("FirstLaunchOnboarding", true)
    }

    /*
Ulozi flag,ze tato aktivita uz jednou zobrazena byla
 */
    private fun savePreferences() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putBoolean("FirstLaunchOnboarding", false)
        editor.commit()
    }

}
