package net.waymire.playground.kotlin.data.tree.bst

fun <T: Comparable<T>> Collection<T>.toRecursiveBinarySearchTree(): RecursiveBinarySearchTree<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val iterator = iterator()
    val tree = RecursiveBinarySearchTree(iterator.next())
    while(iterator.hasNext()) tree.add(iterator.next())
    return tree
}

