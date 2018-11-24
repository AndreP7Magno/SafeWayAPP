package safewayapp.Activity;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eightbitlab.bottomnavigationbar.BottomBarItem;
import com.eightbitlab.bottomnavigationbar.BottomNavigationBar;

import safewayapp.Fragment.HomeFragment;
import safewayapp.Fragment.ContactFragment;
import safewayapp.Fragment.ProfileFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import safewayapp.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_bar)
    BottomNavigationBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        BottomBarItem home = new BottomBarItem(R.drawable.home, R.string.homeMenuBottom);
        bottomBar.addTab(home);

        BottomBarItem contact = new BottomBarItem(R.drawable.contact, R.string.contactMenuBottom);
        bottomBar.addTab(contact);

        BottomBarItem profile = new BottomBarItem(R.drawable.profile, R.string.profileMenuBottom);
        bottomBar.addTab(profile);

        loadFragment(new HomeFragment());

        bottomBar.setOnSelectListener(new BottomNavigationBar.OnSelectListener() {
            Fragment fragment = null;
            @Override
            public void onSelect(int position) {
                switch (position){
                    case 0:
                        fragment = new HomeFragment();
                        break;
                    case 1:
                        fragment = new ContactFragment();
                        break;
                    case 2:
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = new HomeFragment();
                        break;
                }

                loadFragment(fragment);
            }
        });
    }

    private void loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

}
