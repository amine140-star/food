package com.example.food.data

data class CartItem(
    val dish: Dish,
    var quantity: Int = 1,
    val supplements: List<Supplement> = emptyList()
) {

    fun subTotal(): Double {
        val supplementSum = supplements.sumOf { it.price }
        return (dish.price + supplementSum) * quantity
    }
}
