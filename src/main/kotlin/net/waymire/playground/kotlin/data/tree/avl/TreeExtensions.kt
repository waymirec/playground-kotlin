package net.waymire.playground.kotlin.data.tree.avl

import java.util.Stack

fun <T: Comparable<T>> Collection<T>.toBalancedBinarySearchTree(): BalancedBinarySearchTree<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val iterator = iterator()
    val tree = BalancedBinarySearchTree(iterator.next())
    while(iterator.hasNext()) tree.add(iterator.next())
    return tree
}