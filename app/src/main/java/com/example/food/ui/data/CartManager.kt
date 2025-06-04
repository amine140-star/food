package com.example.food.data


object CartManager {

    private val cartItems = mutableListOf<CartItem>()

    fun getItems(): List<CartItem> = cartItems.toList()


    fun addItem(dish: Dish, supplements: List<Supplement>) {
        val existing = cartItems.firstOrNull { item ->
            item.dish.id == dish.id && item.supplements.map { it.id }.sorted() ==
                    supplements.map { it.id }.sorted()
        }
        if (existing != null) {
            existing.quantity += 1
        } else {
            val newItem = CartItem(dish = dish, supplements = supplements, quantity = 1)
            cartItems.add(newItem)
        }
    }


    fun removeItem(cartItem: CartItem) {
        cartItems.remove(cartItem)
    }


    fun updateQuantity(cartItem: CartItem, newQty: Int) {
        if (newQty <= 0) {
            removeItem(cartItem)
        } else {
            cartItem.quantity = newQty
        }
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.subTotal() }
    }

    fun getTotalQuantity(): Int {
        return cartItems.sumOf { it.quantity }
    }


    fun clearCart() {
        cartItems.clear()
    }
}
