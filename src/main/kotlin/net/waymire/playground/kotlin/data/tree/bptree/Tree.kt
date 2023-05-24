package net.waymire.playground.kotlin.data.tree.bptree

class BTree<K : Comparable<K>, V>(order: Int) : Iterable<TreeRecord<K, V>> {
    private var root = TreeNode<K, V>(order)

    fun contains(key: K) = root.containsKey(key)

    fun put(key: K, value: V): Boolean {
        val added = root.put(key, value)
        if (added.isRoot) root = added
        return true
    }

    fun remove(key: K) = root.remove(key) != null

    fun asSequence() = sequence {
        var current: TreeNode<K, V>? = root
        while(current!!.isNotLeaf) current = current.children.first()
        while(current != null) {
            yieldAll(current.records)
            current = current.next
        }
    }

    override fun iterator() = asSequence().iterator()

    fun keys() = asSequence().map { it.key }.toList()
    fun values() = asSequence().map { it.value }.toList()
}
