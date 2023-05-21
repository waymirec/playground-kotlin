package net.waymire.playground.kotlin.data.tree.btree


fun <T: Comparable<T>> Collection<T>.toBTree(order: Int): BTree<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val iterator = iterator()
    val tree = BTree<T>(order)
    while(iterator.hasNext()) tree.add(iterator.next())
    return tree
}
