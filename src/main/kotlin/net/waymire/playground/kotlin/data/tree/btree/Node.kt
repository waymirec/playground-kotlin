package net.waymire.playground.kotlin.data.tree.btree

import net.waymire.playground.kotlin.SortedList
import net.waymire.playground.kotlin.sortedListOf
import kotlin.math.ceil

class TreeNode<T: Comparable<T>>(
    val order: Int,
    var parent: TreeNode<T>? = null,
    val keys: SortedList<T> = sortedListOf(),
    val children: SortedList<TreeNode<T>> = sortedListOf()
): Comparable<TreeNode<T>> {
    val maxChildrenSize = order
    val maxKeySize = maxChildrenSize - 1
    val minChildrenSize = ceil(order.toDouble() / 2)
    val minKeySize = minChildrenSize - 1

    override fun compareTo(other: TreeNode<T>) = compareValuesBy(this, other, { it.keys.first() }, { it.keys.first() })
}
