package net.waymire.playground.kotlin.data.tree.redblack

import kotlin.math.abs
import kotlin.math.max


class RedBlackTree<T: Comparable<T>>(rootValue: T) {
    private var root = RedBlackTreeNode(value = rootValue, black = true)

    fun contains(value: T) = root.contains(value)

    fun add(value: T): Boolean {
        val result = root.add(value)
        result.updateTreeHeightBottomUp()
        //if (root.isNotBalanced) rebalance(result)
        return true
    }
    fun remove(value: T) = root.remove(value)
}

class RedBlackTreeNode<T: Comparable<T>>(
    private var black: Boolean = false,
    var value: T,
    var parent: RedBlackTreeNode<T>? = null,
    var left: RedBlackTreeNode<T>? = null,
    var right: RedBlackTreeNode<T>? = null,
    var leftHeight: Int = 0,
    var rightHeight: Int = 0,
) {
    val isRoot get() = parent == null
    val isChild get() = !isRoot
    val isLeftChild get() = parent?.let { value < it.value } ?: false
    val isRightChild get() = parent?.let { value >= it.value } ?: false
    val isRed get() = !black
    val isBlack get() = black
    val isLeafNode get() = left == null && right == null
    val isNotLeafNode get() = !isLeafNode
    val balanceFactor get() = leftHeight - rightHeight
    val isBalanced get() = abs(balanceFactor) < 2
    val isNotBalanced get() = !isBalanced
    val isLeftHeavy get() = balanceFactor > 0
    val isLeftRightHeavy get() = isLeftHeavy && left!!.isRightHeavy
    val isRightHeavy get() = balanceFactor < 0
    val isRightLeftHeavy get() = isRightHeavy && right!!.isLeftHeavy
    val height get() = max(leftHeight, rightHeight)

    fun contains(value: T): Boolean {
        var current: RedBlackTreeNode<T> = this
        while(true) {
            if (current.value == value) return true
            if (value < current.value) {
                val targetNode = current.left ?: return false
                current = targetNode
                continue
            }

            val targetNode = current.right ?: return false
            current = targetNode
            continue
        }
    }

    fun add(value: T): RedBlackTreeNode<T> {
        var current: RedBlackTreeNode<T> = this
        while(true) {
            if (current.value == value) return current
            if (value < current.value) {
                val targetNode = current.left
                if (targetNode != null) {
                    current = targetNode
                    continue
                }
                val added = RedBlackTreeNode(value = value, parent = current)
                current.left = added
                current = added
                break
            } else {
                val targetNode = current.right
                if (targetNode != null) {
                    current = targetNode
                    continue
                }
                val added = RedBlackTreeNode(value = value, parent = current)
                current.right = added
                current = added
                break
            }
        }

        return current
    }

    fun remove(value: T): Boolean {
        throw NotImplementedError()
    }

    fun updateTreeHeightTopDown() {
        val queue: ArrayDeque<RedBlackTreeNode<T>> = ArrayDeque()
        this.clearHeight()
        queue.addFirst(this)
        while(queue.isNotEmpty()) {
            val n = queue.removeFirst()
            if (n.isLeafNode) n.updateTreeHeightBottomUp()
            n.left?.let { it.clearHeight(); queue.addFirst(it) }
            n.right?.let { it.clearHeight(); queue.addFirst(it) }
        }
    }

    fun updateTreeHeightBottomUp2() {
        var node: RedBlackTreeNode<T> = this
        var height = if (node.isBlack) 1 else 0
        while(node.parent != null) {

        }
    }
    fun updateTreeHeightBottomUp() {
        var height = 0
        var node: RedBlackTreeNode<T> = this
        while(true) {
            if (node.isBlack) height++
            val parent = node.parent ?: break
            if (node.isLeftChild)
                parent.leftHeight = max(parent.leftHeight, height)
            else
                parent.rightHeight = max(parent.rightHeight, height)
            node = parent
        }
    }

    private fun clearHeight() {
        this.leftHeight = 0
        this.rightHeight = 0
    }
}


fun <T: Comparable<T>> Collection<T>.toRedBlackTree(): RedBlackTree<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val iterator = iterator()
    val tree = RedBlackTree(iterator.next())
    while(iterator.hasNext()) tree.add(iterator.next())
    return tree
}
