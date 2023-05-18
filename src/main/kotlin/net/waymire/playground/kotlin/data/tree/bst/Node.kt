package net.waymire.playground.kotlin.data.tree.bst

data class BinarySearchTreeNode<T: Comparable<T>>(
    var value: T,
    var left: BinarySearchTreeNode<T>? = null,
    var right: BinarySearchTreeNode<T>? = null
)
