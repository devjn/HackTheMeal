package fi.xamk.smartmealcompanion;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.StackedBarModel;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static BottomBar bottomBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_tracker) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                }
            }
        });

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int arg = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            if(arg == 1) {
                rootView = inflater.inflate(R.layout.fragment_stats, container, false);
                mPieChart = (PieChart) rootView.findViewById(R.id.piechart);
                mStackedBarChart = (StackedBarChart) rootView.findViewById(R.id.stackedbarchart);
                initePie();
                initView();

            } else {
                rootView = inflater.inflate(R.layout.fragment_burn, container, false);
            }

            return rootView;
        }

        PieChart mPieChart;
        StackedBarChart mStackedBarChart;



        public void initePie() {
            mPieChart.setLegendColor(0xFFFFFFFF);
            mPieChart.addPieSlice(new PieModel("Today", 33.8f, Color.parseColor("#FE6DA8")));
            mPieChart.addPieSlice(new PieModel("Today", 23, Color.parseColor("#56B7F1")));
            mPieChart.addPieSlice(new PieModel("Today", 10, Color.parseColor("#CDA67F")));
            mPieChart.addPieSlice(new PieModel("Today", 40, Color.parseColor("#FED70E")));

            mPieChart.startAnimation();
        }


        public void initView() {
            StackedBarModel s1 = new StackedBarModel("MON");

            // s1.addBar(new BarModel(250, 0xFF63CBB0));
            s1.addBar(new BarModel(27.5f, 0xFFFE6DA8));
            s1.addBar(new BarModel(58, 0xFF56B7F1));
            s1.addBar(new BarModel(5, 0xFFCDA67F));
            s1.addBar(new BarModel(3, 0xFFFED70E));

            StackedBarModel s2 = new StackedBarModel("TUE");
            //s2.addBar(new BarModel(158, 0xFF63CBB0));
            s2.addBar(new BarModel(26.3f, 0xFFFE6DA8));
            s2.addBar(new BarModel(6.000001f, 0xFF56B7F1));
            s2.addBar(new BarModel(13, 0xFFCDA67F));
            s2.addBar(new BarModel(30, 0xFFFED70E));

            StackedBarModel s3 = new StackedBarModel("WED");

            // s3.addBar(new BarModel(335, 0xFF63CBB0));
            s3.addBar(new BarModel(36.9f, 0xFFFE6DA8));
            s3.addBar(new BarModel(77, 0xFF56B7F1));
            s3.addBar(new BarModel(7, 0xFFCDA67F));
            s3.addBar(new BarModel(3, 0xFFFED70E));

            StackedBarModel s4 = new StackedBarModel("THU");
            // s4.addBar(new BarModel(120, 0xFF63CBB0));
            s4.addBar(new BarModel(12.1f, 0xFFFE6DA8));
            s4.addBar(new BarModel(8.111111f, 0xFF56B7F1));
            s4.addBar(new BarModel(4.111111f, 0xFFCDA67F));
            s4.addBar(new BarModel(14, 0xFFFED70E));

            StackedBarModel s5 = new StackedBarModel("FRI");
            // s5.addBar(new BarModel(335, 0xFF63CBB0));
            s5.addBar(new BarModel(33.8f, 0xFFFE6DA8));
            s5.addBar(new BarModel(23, 0xFF56B7F1));
            s5.addBar(new BarModel(10, 0xFFCDA67F));
            s5.addBar(new BarModel(40, 0xFFFED70E));

            mStackedBarChart.addBar(s1);
            mStackedBarChart.addBar(s2);
            mStackedBarChart.addBar(s3);
            mStackedBarChart.addBar(s4);
            mStackedBarChart.addBar(s5);

            mStackedBarChart.startAnimation();
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            bottomBar.selectTabAtPosition(Math.min(position, 1));
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
