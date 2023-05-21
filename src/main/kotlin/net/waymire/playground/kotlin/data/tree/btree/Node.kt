package net.waymire.playground.kotlin.data.tree.btree

import net.waymire.playground.kotlin.SortedList
import net.waymire.playground.kotlin.sortedListOf
import kotlin.math.ceil

class BTreeNode<T: Comparable<T>>(
    val order: Int,
    var parent: BTreeNode<T>? = null,
    val keys: SortedList<T> = sortedListOf(),
    val children: SortedList<BTreeNode<T>> = sortedListOf()
): Comparable<BTreeNode<T>> {
    val maxChildrenSize = order
    val maxKeySize = maxChildrenSize - 1
    val minChildrenSize = ceil(order.toDouble() / 2)
    val minKeySize = minChildrenSize - 1

    override fun compareTo(other: BTreeNode<T>) = compareValuesBy(this, other, { it.keys.first() }, { it.keys.first() })
}
