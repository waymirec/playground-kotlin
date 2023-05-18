package net.waymire.playground.kotlin.data.tree.redblack

fun <T: Comparable<T>> Collection<T>.toRedBlackTree(): RedBlackTree<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val iterator = iterator()
    val tree = RedBlackTree(iterator.next())
    while(iterator.hasNext()) tree.add(iterator.next())
    return tree
}
