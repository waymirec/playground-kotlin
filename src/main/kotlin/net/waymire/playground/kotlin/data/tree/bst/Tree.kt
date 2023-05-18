package net.waymire.playground.kotlin.data.tree.bst


class BinarySearchTree<T: Comparable<T>>(rootValue: T) {
    private var root = BinarySearchTreeNode(rootValue)

    fun traverseInOrder() = root.traverseInOrder()
    fun traversePreOrder() = root.traversePreOrder()
    fun traversePostOrder() = root.traversePostOrder()
    fun traverseBreadthFirst() = root.traverseBreadthFirst()
    fun contains(value: T) = root.contains(value)
    fun add(value: T) = root.add(value)
    fun remove(value: T) = root.remove(value)
}
