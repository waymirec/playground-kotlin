package net.waymire.playground.kotlin.data.tree.bst

class RecursiveBinarySearchTree<T: Comparable<T>>(rootValue: T) {
    private var root = BinarySearchTreeNode(rootValue)

    fun traverseInOrder() = root.recursiveTraverseInOrder()
    fun traversePreOrder() = root.recursiveTraversePreOrder()
    fun traversePostOrder() = root.recursiveTraversePostOrder()
    fun traverseBreadthFirst() = root.recursiveTraverseBreadthFirst()
    fun contains(value: T) = root.recursiveContains(value)
    fun add(value: T) = root.recursiveAdd(value)
    fun remove(value: T) = root.recursiveRemove(value)
}
