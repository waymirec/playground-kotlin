package net.waymire.playground.kotlin.data.tree.btree

import net.waymire.playground.kotlin.data.tree.avl.remove

class BTree<T : Comparable<T>>(val order: Int) {
    private var root = BTreeNode<T>(order)

    fun contains(value: T) = root.contains(value)

    fun add(value: T): Boolean {
        val added = root.add(value)
        if (added.isRoot) root = added
        return true
    }

    fun remove(value: T): Boolean {
        val removed = root.remove(value) ?: return false
        return true
    }

    fun toList() = root.traverseInOrder()
}