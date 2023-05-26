package net.waymire.playground.kotlin.data.tree.redblack

import kotlin.math.max

class RedBlackTreeNode<T: Comparable<T>>(
    var black: Boolean = false,
    var value: T,
    var parent: RedBlackTreeNode<T>? = null,
    var left: RedBlackTreeNode<T>? = null,
    var right: RedBlackTreeNode<T>? = null,
    var leftHeight: Int = 0,
    var rightHeight: Int = 0,
    var leftBlackHeight: Int = 0,
    var rightBlackHeight: Int = 0
) {
    val height: Int get() = max(leftHeight, rightHeight)
    val blackHeight: Int get() = max(leftBlackHeight, rightBlackHeight)
}