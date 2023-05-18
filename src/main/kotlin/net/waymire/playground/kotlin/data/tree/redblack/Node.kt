package net.waymire.playground.kotlin.data.tree.redblack

class RedBlackTreeNode<T: Comparable<T>>(
    var black: Boolean = false,
    var value: T,
    var parent: RedBlackTreeNode<T>? = null,
    var left: RedBlackTreeNode<T>? = null,
    var right: RedBlackTreeNode<T>? = null,
    var leftHeight: Int = 0,
    var rightHeight: Int = 0,
)