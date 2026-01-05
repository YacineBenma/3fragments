package bg.nbu.a3fragments;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity - The Host Activity
 *
 * This is the MAIN container that holds all fragments.
 * Think of it as a stage, and fragments are actors that appear on the stage.
 *
 * Key Concepts:
 * - Activity: A full screen that can host fragments
 * - Fragment: A reusable portion of UI (like a mini-Activity)
 * - FragmentManager: Controls adding, removing, and switching fragments
 * - FrameLayout: The container where fragments are displayed
 */
public class MainActivity extends AppCompatActivity {

    // Reference to the bottom navigation bar
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the bottom navigation view from XML
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load the MenuFragment by default when app starts
        // savedInstanceState is null on first launch, not null on rotation
        if (savedInstanceState == null) {
            // Replace the fragment_container with MenuFragment
            loadFragment(new MenuFragment());
        }

        // Set up listener for bottom navigation clicks
        // This code runs whenever user clicks a navigation button
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Determine which fragment to show based on which button was clicked
            // item.getItemId() returns the id of the clicked menu item
            int itemId = item.getItemId();

            if (itemId == R.id.nav_menu) {
                // User clicked "Menu" button
                selectedFragment = new MenuFragment();
            } else if (itemId == R.id.nav_order) {
                // User clicked "Order" button
                selectedFragment = new OrderFragment();
            } else if (itemId == R.id.nav_profile) {
                // User clicked "Profile" button
                selectedFragment = new ProfileFragment();
            }

            // Load the selected fragment into the container
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }

            // Return true to indicate the click was handled
            return true;
        });
    }

    /**
     * loadFragment() - Helper method to switch fragments
     *
     * This method handles the work of replacing one fragment with another.
     *
     * How Fragment Transactions Work:
     * 1. Get FragmentManager (the controller)
     * 2. Begin a transaction (start making changes)
     * 3. Replace the old fragment with new one
     * 4. Commit the transaction (apply the changes)
     *
     * @param fragment - The fragment to display
     */
    private void loadFragment(Fragment fragment) {
        // Step 1: Get the FragmentManager
        // FragmentManager is like a stage director - it controls which fragments
        // are shown, hidden, added, or removed
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Step 2: Begin a transaction
        // A transaction is a series of fragment operations that happen together
        // Think of it like a database transaction - either all changes happen or none
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Step 3: Replace the current fragment
        // Parameters:
        // - R.id.fragment_container: The FrameLayout where fragment will be displayed
        // - fragment: The new fragment to show
        //
        // What "replace" does:
        // - Removes any existing fragment in the container
        // - Adds the new fragment to the container
        transaction.replace(R.id.fragment_container, fragment);

        // Step 4: Commit the transaction
        // This actually executes all the operations defined above
        // Without commit(), nothing would happen
        transaction.commit();

        // Note: You could also use transaction.add() instead of replace()
        // - add() keeps old fragment in memory (hidden behind new one)
        // - replace() removes old fragment completely
        // replace() is usually preferred to save memory
    }

    /**
     * Alternative approach using addToBackStack() for navigation history:
     *
     * If you want the back button to navigate between fragments:
     *
     * transaction.replace(R.id.fragment_container, fragment);
     * transaction.addToBackStack(null); // Add to back stack
     * transaction.commit();
     *
     * With addToBackStack():
     * - Menu → Order → Profile, then press back → goes to Order → Menu
     *
     * Without addToBackStack():
     * - Menu → Order → Profile, then press back → exits the app
     */
}