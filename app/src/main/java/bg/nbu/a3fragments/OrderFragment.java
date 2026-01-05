package bg.nbu.a3fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;

/**
 * OrderFragment - Second Fragment (Order Details)
 *
 * Updated to observe SharedViewModel and automatically update when items are added
 */
public class OrderFragment extends Fragment {

    // UI components
    private TextView tvOrderDetails;
    private TextView tvTotalPrice;
    private Button btnPlaceOrder;

    // Shared ViewModel - same instance as MenuFragment
    private OrderViewModel orderViewModel;

    /**
     * onCreateView() - Create the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        return view;
    }

    /**
     * onViewCreated() - Initialize UI and set up observers
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find UI components
        tvOrderDetails = view.findViewById(R.id.tv_order_details);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        btnPlaceOrder = view.findViewById(R.id.btn_place_order);

        // Get the SAME ViewModel instance that MenuFragment uses
        // requireActivity() ensures both fragments share the same ViewModel
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        /**
         * Set up observers - This is where the magic happens!
         *
         * observe() takes two parameters:
         * 1. LifecycleOwner (this fragment)
         * 2. Observer callback (lambda function)
         *
         * The observer callback runs automatically whenever LiveData changes.
         */

        // Observe order items
        // getViewLifecycleOwner() ties observer to fragment's view lifecycle
        // This prevents memory leaks
        orderViewModel.getOrderItems().observe(getViewLifecycleOwner(), orderItems -> {
            // This code runs AUTOMATICALLY whenever order items change!
            // orderItems parameter contains the updated list

            updateOrderDisplay(orderItems);
        });

        /**
         * How observe() works:
         *
         * 1. OrderFragment calls observe() and provides a callback
         * 2. LiveData stores this callback in its list of observers
         * 3. MenuFragment adds an item → calls setValue()
         * 4. setValue() loops through all observers and calls them
         * 5. This callback runs with the new data
         * 6. UI updates automatically!
         *
         * Benefits:
         * - Automatic updates (no manual refresh)
         * - Lifecycle-aware (stops when fragment not visible)
         * - No memory leaks (observer removed when fragment destroyed)
         */

        // Observe total price
        orderViewModel.getTotalPrice().observe(getViewLifecycleOwner(), total -> {
            // This runs whenever total price changes
            tvTotalPrice.setText("Total: €" + total);
        });

        // Place order button
        btnPlaceOrder.setOnClickListener(v -> {
            // Get current items to check if order is empty
            List<OrderItem> currentItems = orderViewModel.getOrderItems().getValue();

            if (currentItems != null && !currentItems.isEmpty()) {
                // Get total for confirmation message
                Integer total = orderViewModel.getTotalPrice().getValue();

                Toast.makeText(getContext(),
                        "Order placed! Total: €" + total,
                        Toast.LENGTH_LONG).show();

                // Place the order - this increments order count AND clears items
                // ProfileFragment will automatically see the updated count!
                orderViewModel.placeOrder();
            } else {
                Toast.makeText(getContext(),
                        "No items in order!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * updateOrderDisplay() - Updates UI with order items
     *
     * Called automatically by the observer whenever items change
     *
     * @param orderItems - List of items in the order
     */
    private void updateOrderDisplay(List<OrderItem> orderItems) {
        if (orderItems != null && !orderItems.isEmpty()) {
            // Build display string from list of items
            StringBuilder orderDetails = new StringBuilder("Items in order:\n\n");

            // Loop through each item and add to display
            for (OrderItem item : orderItems) {
                // item.toString() returns formatted string like "🍕 Margherita - €8"
                orderDetails.append(item.toString()).append("\n");
            }

            tvOrderDetails.setText(orderDetails.toString());
        } else {
            // Empty order - show default message
            tvOrderDetails.setText("No items in order yet.\n\nGo to Menu tab to add pizzas!");
        }
    }

    /**
     * Complete Communication Flow Example:
     *
     * User Action: Click "Add Margherita" in MenuFragment
     *
     * Step 1: MenuFragment
     *   OrderItem item = new OrderItem("Margherita", 8, "🍕");
     *   orderViewModel.addItem(item);
     *
     * Step 2: OrderViewModel.addItem()
     *   currentList.add(item);
     *   orderItems.setValue(currentList);  ← Triggers notification
     *
     * Step 3: LiveData.setValue()
     *   Loops through all observers and calls them
     *
     * Step 4: OrderFragment observer callback
     *   orderViewModel.getOrderItems().observe(..., orderItems -> {
     *       updateOrderDisplay(orderItems);  ← This runs!
     *   });
     *
     * Step 5: updateOrderDisplay()
     *   Builds string from items
     *   Updates TextView
     *   User sees new item instantly!
     *
     * All of this happens automatically, in milliseconds!
     */

    /**
     * Why use ViewModel + LiveData?
     *
     * Alternative 1: Pass data through Activity
     * - Activity needs to know about all fragments
     * - Tight coupling (hard to maintain)
     * - Data lost on rotation
     *
     * Alternative 2: Use interfaces
     * - Lots of boilerplate code
     * - Manual notification
     * - Data lost on rotation
     *
     * ViewModel + LiveData:
     * ✅ Survives rotation
     * ✅ Automatic updates
     * ✅ Loose coupling
     * ✅ No memory leaks
     * ✅ Minimal code
     * ✅ Modern Android architecture
     */
}