package net.waymire.playground.kotlin.data.tree.bptree

import net.waymire.playground.kotlin.SortedList
import net.waymire.playground.kotlin.sortedListOf
import kotlin.math.ceil

data class TreeRecord<K: Comparable<K>, V>(val key: K, val value: V): Comparable<TreeRecord<K, V>> {
    override fun compareTo(other: TreeRecord<K, V>) = compareValuesBy(this, other, { it.key}, { it.key })
}

class TreeNode<K: Comparable<K>, V>(
    val order: Int,
    var parent: TreeNode<K, V>? = null,
    val keys: SortedList<K> = sortedListOf(),
    val children: SortedList<TreeNode<K, V>> = sortedListOf(),
    val records: SortedList<TreeRecord<K, V>> = sortedListOf()
): Comparable<TreeNode<K, V>> {
    val minElementsAllowed = ceil(order.toDouble() / 2)
    val maxElementsAllowed = order
    val minKeySize = minElementsAllowed - 1
    val maxKeySize = maxElementsAllowed - 1

    var next: TreeNode<K, V>? = null
    var previous: TreeNode<K, V>? = null

    override fun compareTo(other: TreeNode<K, V>) = compareValuesBy(this, other, { it.keys.first() }, { it.keys.first() })

    override fun toString() = "TreeNode(keys=${keys.joinToString()})"
}
