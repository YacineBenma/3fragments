package bg.nbu.a3fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * MenuFragment - First Fragment (Pizza Menu)
 *
 * Updated to use SharedViewModel for communication with OrderFragment
 */
public class MenuFragment extends Fragment {

    // UI components
    private Button btnAddMargherita;
    private Button btnAddPepperoni;
    private Button btnAddHawaiian;

    // Shared ViewModel - accessed by both MenuFragment and OrderFragment
    private OrderViewModel orderViewModel;

    /**
     * onCreateView() - Create the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        return view;
    }

    /**
     * onViewCreated() - Initialize UI and ViewModel
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        // ViewModelProvider gets or creates the ViewModel
        // Using requireActivity() makes it activity-scoped (shared between fragments)
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        /**
         * How ViewModelProvider works:
         *
         * First time (in MenuFragment):
         * - new ViewModelProvider(requireActivity()) checks if ViewModel exists
         * - It doesn't exist, so creates new OrderViewModel
         * - Stores it in Activity's ViewModelStore
         *
         * Second time (in OrderFragment):
         * - new ViewModelProvider(requireActivity()) checks if ViewModel exists
         * - It DOES exist (MenuFragment created it)
         * - Returns the SAME OrderViewModel instance
         *
         * Result: Both fragments share the same ViewModel!
         */

        // Find buttons
        btnAddMargherita = view.findViewById(R.id.btn_add_margherita);
        btnAddPepperoni = view.findViewById(R.id.btn_add_pepperoni);
        btnAddHawaiian = view.findViewById(R.id.btn_add_hawaiian);

        // Set up click listeners for each pizza
        btnAddMargherita.setOnClickListener(v -> {
            // Create OrderItem for Margherita
            OrderItem item = new OrderItem("Margherita", 8, "🍕");

            // Add to ViewModel - this automatically notifies OrderFragment!
            orderViewModel.addItem(item);

            Toast.makeText(getContext(), "Margherita added to order!", Toast.LENGTH_SHORT).show();
        });

        btnAddPepperoni.setOnClickListener(v -> {
            OrderItem item = new OrderItem("Pepperoni", 10, "🍕");
            orderViewModel.addItem(item);
            Toast.makeText(getContext(), "Pepperoni added to order!", Toast.LENGTH_SHORT).show();
        });

        btnAddHawaiian.setOnClickListener(v -> {
            OrderItem item = new OrderItem("Hawaiian", 9, "🍕");
            orderViewModel.addItem(item);
            Toast.makeText(getContext(), "Hawaiian added to order!", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Communication Flow:
     *
     * 1. User clicks "Add Margherita" button in MenuFragment
     *    ↓
     * 2. onClick listener creates OrderItem
     *    ↓
     * 3. Calls orderViewModel.addItem(item)
     *    ↓
     * 4. ViewModel adds item to list and calls setValue()
     *    ↓
     * 5. LiveData automatically notifies ALL observers
     *    ↓
     * 6. OrderFragment's observer receives notification
     *    ↓
     * 7. OrderFragment updates its UI with new item
     *
     * All automatic! No manual notification needed!
     */
}