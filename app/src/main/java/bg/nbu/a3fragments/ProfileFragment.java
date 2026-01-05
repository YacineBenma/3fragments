package bg.nbu.a3fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * ProfileFragment - Third Fragment (User Profile)
 *
 * Updated to observe SharedViewModel and display order count dynamically
 */
public class ProfileFragment extends Fragment {

    private TextView tvOrderHistory;

    // Shared ViewModel - same instance used by Menu and Order fragments
    private OrderViewModel orderViewModel;

    /**
     * onCreateView() - Create fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate profile layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    /**
     * onViewCreated() - Initialize UI and set up observer
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the order history TextView
        tvOrderHistory = view.findViewById(R.id.tv_order_history);

        // Get the shared ViewModel (same instance as MenuFragment and OrderFragment)
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        /**
         * Observe total orders placed
         *
         * This observer automatically runs whenever OrderFragment calls
         * orderViewModel.placeOrder()
         *
         * Flow:
         * 1. User places order in OrderFragment
         * 2. OrderFragment calls orderViewModel.placeOrder()
         * 3. ViewModel increments totalOrdersPlaced and calls setValue()
         * 4. LiveData notifies ALL observers
         * 5. This observer runs automatically
         * 6. UI updates with new count!
         */
        orderViewModel.getTotalOrdersPlaced().observe(getViewLifecycleOwner(), orderCount -> {
            // This runs automatically whenever order count changes!
            // orderCount parameter contains the updated number

            // Update the TextView
            tvOrderHistory.setText("Orders placed: " + orderCount);

            // Optional: Show congratulations message for first order
            if (orderCount == 1) {
                // User just placed their first order!
                tvOrderHistory.append("\n🎉 Congrats on your first order!");
            } else if (orderCount == 10) {
                tvOrderHistory.append("\n⭐ Wow! 10 orders!");
            }
        });

        /**
         * How this works across all 3 fragments:
         *
         * MenuFragment:
         * - Adds items to ViewModel
         *
         * OrderFragment:
         * - Displays items (observes orderItems)
         * - Places order (calls placeOrder())
         * - Observes totalOrdersPlaced for confirmation
         *
         * ProfileFragment:
         * - Observes totalOrdersPlaced
         * - Updates automatically when order placed
         *
         * All 3 fragments share the SAME ViewModel instance!
         */
    }

    /**
     * Fragment Lifecycle Methods (all optional to override)
     *
     * These are called at different points in the fragment's life.
     * Understanding when each is called helps you write better code.
     */

    /**
     * onAttach() - Called when fragment is first attached to Activity
     * Use: Get reference to Activity, set up listeners
     */
    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);
        // Fragment is now attached to MainActivity
        // You can cast context to MainActivity here if needed
        System.out.println("ProfileFragment: onAttach()");
    }

    /**
     * onCreate() - Called when fragment is created
     * Use: Initialize non-UI data, restore saved state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fragment object created but view not yet created
        // Good place to restore saved data
        System.out.println("ProfileFragment: onCreate()");
    }

    // onCreateView() - already implemented above
    // This is where the UI is inflated

    // onViewCreated() - already implemented above
    // This is where we initialize UI components

    /**
     * onStart() - Called when fragment becomes visible
     * Use: Start animations, register listeners
     */
    @Override
    public void onStart() {
        super.onStart();
        System.out.println("ProfileFragment: onStart() - Fragment visible");
    }

    /**
     * onResume() - Called when fragment is interactive
     * Use: Resume paused operations, start videos/audio
     */
    @Override
    public void onResume() {
        super.onResume();
        System.out.println("ProfileFragment: onResume() - Fragment interactive");
    }

    /**
     * onPause() - Called when fragment loses focus
     * Use: Pause videos/audio, save draft data
     */
    @Override
    public void onPause() {
        super.onPause();
        System.out.println("ProfileFragment: onPause() - Fragment losing focus");
    }

    /**
     * onStop() - Called when fragment is no longer visible
     * Use: Stop animations, unregister listeners
     */
    @Override
    public void onStop() {
        super.onStop();
        System.out.println("ProfileFragment: onStop() - Fragment not visible");
    }

    /**
     * onDestroyView() - Called when fragment's view is destroyed
     * Use: Clean up view-related resources, null out view references
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // View is being destroyed (maybe switching to another fragment)
        // Set view references to null to prevent memory leaks
        tvOrderHistory = null;
        System.out.println("ProfileFragment: onDestroyView()");
    }

    /**
     * onDestroy() - Called when fragment is being destroyed
     * Use: Final cleanup
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("ProfileFragment: onDestroy()");
    }

    /**
     * onDetach() - Called when fragment is detached from Activity
     * Use: Final cleanup, null out Activity references
     */
    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("ProfileFragment: onDetach()");
    }

    /**
     * Real-World Fragment Lifecycle Example:
     *
     * When you tap "Profile" button:
     * 1. ProfileFragment: onAttach()
     * 2. ProfileFragment: onCreate()
     * 3. ProfileFragment: onCreateView() ← UI created
     * 4. ProfileFragment: onViewCreated()
     * 5. ProfileFragment: onStart()
     * 6. ProfileFragment: onResume()
     * [Fragment is now visible and interactive]
     *
     * When you tap "Menu" button (switching away):
     * 7. ProfileFragment: onPause()
     * 8. ProfileFragment: onStop()
     * 9. ProfileFragment: onDestroyView()
     * [Fragment view destroyed, but object still exists]
     *
     * When you tap "Profile" again:
     * 3. ProfileFragment: onCreateView() ← UI re-created
     * 4. ProfileFragment: onViewCreated()
     * 5. ProfileFragment: onStart()
     * 6. ProfileFragment: onResume()
     * [Fragment reused, view recreated]
     */

    /**
     * Fragment vs Activity - When to Use Each
     *
     * Use Fragment when:
     * - Building modular UI components
     * - Creating reusable screens
     * - Implementing bottom navigation
     * - Supporting tablets (multi-pane layouts)
     * - Need better control over UI lifecycle
     *
     * Use Activity when:
     * - Simple apps with few screens
     * - Need to receive system intents
     * - Launcher/entry point of app
     * - Hosting fragments
     *
     * Modern apps typically have:
     * - Few Activities (often just 1-3)
     * - Many Fragments (10-50+)
     */
}