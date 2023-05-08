package net.waymire.playground.kotlin.data

import java.util.Stack

data class TreeNode<T: Comparable<T>>(
    val value: T,
    var left: TreeNode<T>? = null,
    var right: TreeNode<T>? = null
) {
    fun add(value: T): Boolean {
        var current: TreeNode<T> = this
        while(true) {
            // allow duplicates? set vs list logic
            //if (current.value == value) return true
            if (value < current.value) {
                val targetNode = current.left
                if (targetNode != null) {
                    current = targetNode
                    continue
                } else {
                    current.left = TreeNode(value = value)
                    return true
                }
            } else {
                val targetNode = current.right
                if (targetNode != null) {
                    current = targetNode
                    continue
                } else {
                    current.right = TreeNode(value = value)
                    return true
                }
            }
        }
    }

    fun contains(value: T): Boolean {
        var current: TreeNode<T> = this
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

    fun inOrder(): List<T> {
        val stack: Stack<TreeNode<T>> = Stack()
        val accumulator: MutableList<T> = mutableListOf()
        var current: TreeNode<T>? = this
        while(true) {
            while(current != null) {
                stack.push(current)
                current = current.left
            }
            if (stack.empty()) break

            current = stack.pop()
            accumulator.add(current.value)
            current = current.right
            continue
        }
        return accumulator
    }

    fun preOrder(): List<T> {
        val queue: ArrayDeque<TreeNode<T>> = ArrayDeque()
        val accumulator: MutableList<T> = mutableListOf()
        var current: TreeNode<T>? = this
        while(true) {
            while(current != null) {
                accumulator.add(current.value)
                queue.addFirst(current)
                current = current.left
            }
            if (queue.isEmpty()) break

            current = queue.removeFirst().right
            continue
        }
        return accumulator
    }

    fun postOrder(): List<T> {
        val stack1: Stack<TreeNode<T>> = Stack()
        val stack2: Stack<TreeNode<T>> = Stack()
        stack1.push(this)

        while(stack1.isNotEmpty()) {
            val current = stack1.pop()
            stack2.push(current)
            current.left?.let { stack1.push(it) }
            current.right?.let { stack1.push(it) }
        }

        return stack2.toList().map { it.value }.reversed()
    }

    fun breadthFirstOrder(): List<T> {
        val accumulator: MutableList<T> = mutableListOf()
        val deque = ArrayDeque<TreeNode<T>>()
        deque.addFirst(this)

        while(deque.isNotEmpty()) {
            val node = deque.removeFirst()
            accumulator.add(node.value)
            node.left?.let { deque.addLast(it) }
            node.right?.let { deque.addLast(it) }
        }

        return accumulator
    }
}


fun <T: Comparable<T>> List<T>.toTree(): TreeNode<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val root = TreeNode(value = first())
    if (size > 1) {
        for (i in 1 until size) {
            root.add(this[i])
        }
    }
    return root
}
