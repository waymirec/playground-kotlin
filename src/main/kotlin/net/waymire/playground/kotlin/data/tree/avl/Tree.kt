package net.waymire.playground.kotlin.data.tree.avl

class BalancedBinarySearchTree<T: Comparable<T>>(rootValue: T) {
    private var root = BalancedBinarySearchTreeNode(value = rootValue)

    val isBalanced get() = root.isBalanced
    val isLeftHeavy get() = root.isLeftHeavy
    val isRightHeavy get() = root.isRightHeavy
    val height get() = root.height

    fun traverseInOrder() = root.traverseInOrder()
    fun traversePreOrder() = root.traversePreOrder()
    fun traversePostOrder() = root.traversePostOrder()
    fun traverseBreadthFirst() = root.traverseBreadthFirst()
    fun contains(value: T) = root.contains(value)

    fun add(values: Iterable<T>): Int {
        val iterator = values.iterator()
        var count = 0
        while (iterator.hasNext()) {
            root.add(iterator.next())
            count++
        }
        // todo: is this more efficient, on average, then recalculating the tree height after each add?
        root.updateTreeHeightTopDown()
        return count
    }

    fun add(value: T): Boolean {
        val result = root.add(value)
        result.updateTreeHeightBottomUp()
        if (root.isNotBalanced) rebalance(result)
        return true
    }

    fun remove(value: T): Boolean {
        val removed = root.remove(value) ?: return false
        //todo: improve this to only recalc the side of the tree affected
        root.updateTreeHeightTopDown()
        if (removed.isNotBalanced) rebalance(removed)
        return true
    }

    private fun rebalance(startingNode: BalancedBinarySearchTreeNode<T>) {
        var node = startingNode
        if (node.isBalanced) {
            while(true) {
                val parent = node.parent
                if (node.isNotBalanced) break
                if (parent == null) break
                node = parent
            }
        }

        if (node.isNotBalanced) {
            val updated = when {
                node.isRightLeftHeavy -> node.leftRightRotate()
                node.isRightHeavy -> node.leftRotate()
                node.isLeftRightHeavy -> node.rightLeftRotate()
                else -> node.rightRotate()
            }
            if (root.value == node.value) root = updated
            root.updateTreeHeightTopDown()
        }
    }
}

