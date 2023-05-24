package net.waymire.playground.kotlin.data.tree.bst

import net.waymire.playground.kotlin.data.tree.avl.TraversalDirection
import java.util.Stack


class BinarySearchTree<T: Comparable<T>>(rootValue: T) : Iterable<T> {
    private var root = BinarySearchTreeNode(rootValue)

    fun contains(value: T) = root.contains(value)
    fun add(value: T) = root.add(value)
    fun remove(value: T) = root.remove(value)
    override fun iterator() = asSequence().iterator()

    fun asSequence(direction: TraversalDirection = TraversalDirection.IN_ORDER): Sequence<T> = when(direction) {
        TraversalDirection.IN_ORDER -> iterateInOrder()
        TraversalDirection.PRE_ORDER -> iteratePreOrder()
        TraversalDirection.POST_ORDER -> iteratePostOrder()
        TraversalDirection.BREADTH_FIRST -> iterateBreadthFirst()
    }

    private fun iterateInOrder() = sequence {
        val stack: Stack<BinarySearchTreeNode<T>> = Stack()
        var current: BinarySearchTreeNode<T>? = root
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
        val queue: ArrayDeque<BinarySearchTreeNode<T>> = ArrayDeque()
        var current: BinarySearchTreeNode<T>? = root
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
        val stack1: Stack<BinarySearchTreeNode<T>> = Stack()
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
        val deque = ArrayDeque<BinarySearchTreeNode<T>>()
        deque.addFirst(root)

        while(deque.isNotEmpty()) {
            val node = deque.removeFirst()
            yield(node.value)
            node.left?.let { deque.addLast(it) }
            node.right?.let { deque.addLast(it) }
        }
    }
}
