package bg.nbu.a3fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderViewModel - Shared data storage between fragments
 *
 * What is a ViewModel?
 * - A class that stores and manages UI-related data
 * - Survives configuration changes (like screen rotation)
 * - Shared between multiple fragments in the same activity
 *
 * Think of it like a shared notebook:
 * - MenuFragment can write orders into the notebook
 * - OrderFragment can read orders from the notebook
 * - Both fragments see the same data
 *
 * Why use ViewModel?
 * - Survives rotation (data doesn't get lost)
 * - Separates data from UI (better architecture)
 * - Easy communication between fragments
 * - Lifecycle-aware (no memory leaks)
 */
public class OrderViewModel extends ViewModel {

    /**
     * MutableLiveData - Observable data container
     *
     * What is LiveData?
     * - A data holder that can be observed for changes
     * - When data changes, observers get notified automatically
     * - Lifecycle-aware (only updates active fragments)
     *
     * Mutable vs LiveData:
     * - MutableLiveData: Can be changed (private, used internally)
     * - LiveData: Read-only (public, exposed to fragments)
     */

    // Private mutable list - can be modified inside ViewModel
    private MutableLiveData<List<OrderItem>> orderItems;

    // Private total price
    private MutableLiveData<Integer> totalPrice;

    // Private order count - tracks how many orders have been placed
    private MutableLiveData<Integer> totalOrdersPlaced;

    /**
     * Constructor - Initializes the ViewModel
     * Called once when ViewModel is first created
     */
    public OrderViewModel() {
        // Initialize with empty list
        orderItems = new MutableLiveData<>(new ArrayList<>());
        totalPrice = new MutableLiveData<>(0);
        totalOrdersPlaced = new MutableLiveData<>(0);
    }

    /**
     * getOrderItems() - Returns observable order list
     *
     * Fragments can observe this LiveData to get notified of changes.
     * Returns read-only LiveData (fragments can't directly modify it)
     *
     * @return LiveData containing list of OrderItems
     */
    public LiveData<List<OrderItem>> getOrderItems() {
        return orderItems;
    }

    /**
     * getTotalPrice() - Returns observable total price
     *
     * @return LiveData containing total price
     */
    public LiveData<Integer> getTotalPrice() {
        return totalPrice;
    }

    /**
     * getTotalOrdersPlaced() - Returns observable order count
     *
     * ProfileFragment observes this to display order history
     *
     * @return LiveData containing number of orders placed
     */
    public LiveData<Integer> getTotalOrdersPlaced() {
        return totalOrdersPlaced;
    }

    /**
     * addItem() - Adds a pizza to the order
     *
     * How it works:
     * 1. Get current list from LiveData
     * 2. Add new item to list
     * 3. Update LiveData (triggers observers)
     * 4. Recalculate total price
     *
     * @param item - The pizza to add
     */
    public void addItem(OrderItem item) {
        // Get current list (getValue() returns the actual list object)
        List<OrderItem> currentList = orderItems.getValue();

        // Check if list exists (should always be true, but safety check)
        if (currentList != null) {
            // Add new item to list
            currentList.add(item);

            // Update LiveData - this triggers all observers!
            // setValue() notifies all fragments observing this data
            orderItems.setValue(currentList);

            // Recalculate and update total price
            calculateTotalPrice();
        }
    }

    /**
     * clearOrder() - Removes all items from order
     *
     * Called when user places order or wants to start fresh
     */
    public void clearOrder() {
        // Create new empty list
        orderItems.setValue(new ArrayList<>());

        // Reset total to 0
        totalPrice.setValue(0);
    }

    /**
     * placeOrder() - Finalizes the order and increments order count
     *
     * Call this method when user clicks "Place Order" button.
     * It clears the current order and increases the total orders placed counter.
     *
     * This is separate from clearOrder() because we only want to count
     * actual placed orders, not when user just clears their cart.
     */
    public void placeOrder() {
        // Get current order count
        Integer currentCount = totalOrdersPlaced.getValue();

        if (currentCount != null) {
            // Increment by 1
            totalOrdersPlaced.setValue(currentCount + 1);
        }

        // Clear the order after placing it
        clearOrder();
    }

    /**
     * calculateTotalPrice() - Calculates total price of all items
     *
     * Private helper method that:
     * 1. Gets all items in order
     * 2. Sums up all prices
     * 3. Updates totalPrice LiveData
     */
    private void calculateTotalPrice() {
        List<OrderItem> currentList = orderItems.getValue();

        if (currentList != null) {
            int total = 0;

            // Loop through all items and sum their prices
            for (OrderItem item : currentList) {
                total += item.getPrice();
            }

            // Update total price (triggers observers)
            totalPrice.setValue(total);
        }
    }

    /**
     * How LiveData and ViewModel work together:
     *
     * 1. ViewModel created (once per Activity)
     * 2. Fragments observe LiveData:
     *    viewModel.getOrderItems().observe(this, items -> {
     *        // This code runs whenever items change
     *    });
     *
     * 3. MenuFragment adds item:
     *    viewModel.addItem(new OrderItem("Margherita", 8, "🍕"));
     *
     * 4. ViewModel updates LiveData:
     *    orderItems.setValue(updatedList);
     *
     * 5. LiveData notifies all observers
     *
     * 6. OrderFragment's observer runs:
     *    // Update UI with new items
     *
     * This all happens automatically!
     */

    /**
     * onCleared() - Called when ViewModel is destroyed
     *
     * Optional cleanup method.
     * Good place to close database connections, cancel network calls, etc.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        // Cleanup if needed
        System.out.println("OrderViewModel cleared");
    }
}