package net.waymire.playground.kotlin.data.tree.bptree


fun <K : Comparable<K>, V> Map<K, V>.toBPTree(order: Int): BPTree<K, V> {
    if (isEmpty()) throw IllegalArgumentException("input cannot be empty")
    val tree = BPTree<K, V>(order)
    val iterator = iterator()
    while(iterator.hasNext()) {
        val entry = iterator.next()
        tree.put(entry.key, entry.value)
    }
    return tree
}
