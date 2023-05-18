package net.waymire.playground.kotlin.data.tree.avl

class BalancedBinarySearchTreeNode<T: Comparable<T>>(
    var value: T,
    var parent: BalancedBinarySearchTreeNode<T>? = null,
    var left: BalancedBinarySearchTreeNode<T>? = null,
    var right: BalancedBinarySearchTreeNode<T>? = null,
    var leftHeight: Int = 0,
    var rightHeight: Int = 0
)