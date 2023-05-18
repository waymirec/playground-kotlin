package net.waymire.playground.kotlin.data.tree.redblack


class RedBlackTree<T: Comparable<T>>(rootValue: T) {
    private var root = RedBlackTreeNode(value = rootValue, black = true)

    fun contains(value: T) = root.contains(value)

    fun add(value: T): Boolean {
        val result = root.add(value)
        result.updateTreeHeightBottomUp()
        //if (root.isNotBalanced) rebalance(result)
        return true
    }
    fun remove(value: T) = root.remove(value)
}
