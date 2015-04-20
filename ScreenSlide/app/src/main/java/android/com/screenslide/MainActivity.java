package android.com.screenslide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ViewPager viewPager;
    List<Fragment> data = new ArrayList<Fragment>();
    List<ImageView> imageViews = new ArrayList<ImageView>();
    int current = 0;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreenSlidePageFragment fragment_1 = new ScreenSlidePageFragment();
        fragment_1.setId(R.drawable.bg_1);

        ScreenSlidePageFragment fragment_2 = new ScreenSlidePageFragment();
        fragment_2.setId(R.drawable.bg_2);

        ScreenSlidePageFragment fragment_3 = new ScreenSlidePageFragment();
        fragment_3.setId(R.drawable.bg_3);

        ScreenSlidePageFragment fragment_4 = new ScreenSlidePageFragment();
        fragment_4.setId(R.drawable.bg_4);

        data.add(fragment_1);
        data.add(fragment_2);
        data.add(fragment_3);
        data.add(fragment_4);

        linearLayout = (LinearLayout) findViewById(R.id.llayout);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyTransformer myTransformer = new MyTransformer();
        viewPager.setPageTransformer(true, myTransformer);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        for (int i = 0; i < data.size(); i++) {
            ImageView imageView = new ImageView(this);
            if (i == current)
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.dotw));
            else
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i != 0) {
                params.setMargins(20, 0, 0, 0);
                imageView.setLayoutParams(params);
            } else
                imageView.setLayoutParams(params);
            imageViews.add(imageView);
            linearLayout.addView(imageView);
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                imageViews.get(i).setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.dotw));
                imageViews.get(current).setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.dot));
                current = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return data.get(i);
        }

        @Override
        public int getCount() {
            return data.size();
        }

    }

    class MyTransformer implements ViewPager.PageTransformer {
        private float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}
