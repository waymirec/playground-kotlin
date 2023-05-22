package net.waymire.playground.kotlin.data.tree.avl

import java.util.Stack

enum class TraversalDirection {
    IN_ORDER, PRE_ORDER, POST_ORDER, BREADTH_FIRST
}

class BalancedBinarySearchTree<T: Comparable<T>>(rootValue: T) : Iterable<T> {
    private var root = BalancedBinarySearchTreeNode(value = rootValue)

    val isBalanced get() = root.isBalanced
    val isLeftHeavy get() = root.isLeftHeavy
    val isRightHeavy get() = root.isRightHeavy
    val height get() = root.height

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
        if (root.isNotBalanced) result.rebalance()
        return true
    }

    fun remove(value: T): Boolean {
        val removed = root.remove(value) ?: return false
        //todo: improve this to only recalc the side of the tree affected
        root.updateTreeHeightTopDown()
        if (removed.isNotBalanced) removed.rebalance()
        return true
    }

    override fun iterator() = asSequence().iterator()

    fun asSequence(direction: TraversalDirection = TraversalDirection.IN_ORDER): Sequence<T> = when(direction) {
        TraversalDirection.IN_ORDER -> iterateInOrder()
        TraversalDirection.PRE_ORDER -> iteratePreOrder()
        TraversalDirection.POST_ORDER -> iteratePostOrder()
        TraversalDirection.BREADTH_FIRST -> iterateBreadthFirst()
    }

    private fun BalancedBinarySearchTreeNode<T>.rebalance() {
        var node = this
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

    private fun iterateInOrder() = sequence {
        val stack: Stack<BalancedBinarySearchTreeNode<T>> = Stack()
        var current: BalancedBinarySearchTreeNode<T>? = root
        while(true) {
            while(current != null) {
                stack.push(current)
                current = current.left
            }
            if (stack.empty()) break

            current = stack.pop()
            yield(current.value)
            current = current.right
        }
    }

    private fun iteratePreOrder() = sequence {
        val queue: ArrayDeque<BalancedBinarySearchTreeNode<T>> = ArrayDeque()
        var current: BalancedBinarySearchTreeNode<T>? = root
        while(true) {
            while(current != null) {
                yield(current.value)
                queue.addFirst(current)
                current = current.left
            }
            if (queue.isEmpty()) break

            current = queue.removeFirst().right
        }
    }

    private fun iteratePostOrder() : Sequence<T> {
        val stack1: Stack<BalancedBinarySearchTreeNode<T>> = Stack()
        val queue = ArrayDeque<T>()
        stack1.push(root)
        while(stack1.isNotEmpty()) {
            val current = stack1.pop()
            queue.addFirst(current.value)
            current.left?.let { stack1.push(it) }
            current.right?.let { stack1.push(it) }
        }
        return queue.asSequence()
    }

    private fun iterateBreadthFirst() = sequence {
        val deque = ArrayDeque<BalancedBinarySearchTreeNode<T>>()
        deque.addFirst(root)

        while(deque.isNotEmpty()) {
            val node = deque.removeFirst()
            yield(node.value)
            node.left?.let { deque.addLast(it) }
            node.right?.let { deque.addLast(it) }
        }
    }
}

