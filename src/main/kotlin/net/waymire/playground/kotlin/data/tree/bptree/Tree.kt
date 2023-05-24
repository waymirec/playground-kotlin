package net.waymire.playground.kotlin.data.tree.bptree

class BPTree<K : Comparable<K>, V>(order: Int) : Iterable<K> {
    private var root = TreeNode<K, V>(order)

    fun contains(key: K) = root.containsKey(key)

    fun put(key: K, value: V): Boolean {
        val added = root.put(key, value)
        if (added.isRoot) root = added
        return true
    }

    fun remove(key: K) = root.remove(key) != null

    override fun iterator() = keyIterator()
    fun valueIterator() = valuesSequence().iterator()
    fun keyIterator() = keysSequence().iterator()

    fun keysSequence(): Sequence<K> = sequence {
        var current: TreeNode<K, V>? = root
        while(current!!.isNotLeaf) current = current.children.first()
        while(current != null) {
            yieldAll(current.keys)
            current = current.next
        }
    }

    fun valuesSequence(): Sequence<V> = sequence {
        var current: TreeNode<K, V>? = root
        while(current!!.isNotLeaf) current = current.children.first()
        while(current != null) {
            yieldAll(current.records.map { it.value })
            current = current.next
        }
    }
}
