package net.waymire.playground.kotlin.data.tree.btree

import java.util.Stack

class BTree<T : Comparable<T>>(order: Int) : Iterable<T> {
    private var root = BTreeNode<T>(order)

    fun contains(value: T) = root.contains(value)

    fun add(value: T): Boolean {
        val added = root.add(value)
        if (added.isRoot) root = added
        return true
    }

    fun remove(value: T) = root.remove(value) != null

    override fun iterator() = asSequence().iterator()

    fun asSequence(): Sequence<T> = sequence {
        val stack: Stack<Pair<BTreeNode<T>, Int>> = Stack()
        stack.push(Pair(root, 0))

        while (stack.isNotEmpty()) {
            val (node, index) = stack.pop()
            if (node.isLeaf) {
                yieldAll(node.keys)
                continue
            }

            if (index >= node.children.size) continue
            if (index > 0) yield(node.keys[index - 1])
            stack.push(Pair(node, index + 1))
            stack.push(Pair(node.children[index], 0))
        }
    }
}
