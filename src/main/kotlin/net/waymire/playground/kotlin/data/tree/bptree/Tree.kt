package net.waymire.playground.kotlin.data.tree.bptree

import java.util.Stack

class BPTree<K : Comparable<K>, V>(order: Int) : Iterable<K> {
    private var root = TreeNode<K, V>(order)

    fun contains(key: K) = root.containsKey(key)

    fun put(key: K, value: V): Boolean {
        val added = root.put(key, value)
        if (added.isRoot) root = added
        return true
    }

    fun remove(key: K) = root.remove(key) != null

    override fun iterator() = asSequence().iterator()

    fun asSequence(): Sequence<K> = sequence {
        val stack: Stack<Pair<TreeNode<K, V>, Int>> = Stack()
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
