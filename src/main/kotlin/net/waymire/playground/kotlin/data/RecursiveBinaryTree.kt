package net.waymire.playground.kotlin.data

import java.util.Stack

data class RecursiveTreeNode<T: Comparable<T>>(
    val value: T,
    var left: RecursiveTreeNode<T>? = null,
    var right: RecursiveTreeNode<T>? = null
) {
    private enum class DIRECTION { LEFT, RIGHT }

    fun add(value: T): RecursiveTreeNode<T> {
        return when {
            value < this.value -> addDirection(value, DIRECTION.LEFT)
            else -> addDirection(value, DIRECTION.RIGHT)
        }
    }

    fun contains(value: T): Boolean {
        return true
    }

    private fun addDirection(value: T, dir: DIRECTION): RecursiveTreeNode<T> {
        val node = when(dir) {
            DIRECTION.LEFT -> left
            DIRECTION.RIGHT -> right
        }
        return if (node != null) {
            node.add(value)
            node
        } else {
            val newNode = RecursiveTreeNode(value = value)
            when(dir) {
                DIRECTION.LEFT -> left = newNode
                DIRECTION.RIGHT -> right = newNode
            }
            newNode
        }
    }

    fun inOrder(accumulator: MutableList<T> = mutableListOf()): List<T> {
        left?.inOrder(accumulator)
        accumulator.add(value)
        right?.inOrder(accumulator)
        return accumulator
    }

    fun preOrder(accumulator: MutableList<T> = mutableListOf()): List<T> {
        accumulator.add(value)
        left?.preOrder(accumulator)
        right?.preOrder(accumulator)
        return accumulator
    }

    fun postOrder(accumulator: MutableList<T> = mutableListOf()): List<T> {
        left?.postOrder(accumulator)
        right?.postOrder(accumulator)
        accumulator.add(value)
        return accumulator
    }

    fun breadthFirstOrder(): List<T> {
        fun recurse(deque: ArrayDeque<RecursiveTreeNode<T>>, accumulator: MutableList<T>): List<T> {
            if (deque.isEmpty()) return accumulator
            val node = deque.removeFirst()
            accumulator.add(node.value)
            node.left?.let { deque.addLast(it) }
            node.right?.let { deque.addLast(it) }
            return recurse(deque, accumulator)
        }

        val deque = ArrayDeque<RecursiveTreeNode<T>>()
        deque.addFirst(this)
        return recurse(deque, mutableListOf())
    }
}

fun <T: Comparable<T>> List<T>.toRecursiveTree(): RecursiveTreeNode<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val root = RecursiveTreeNode(value = first())
    if (size > 1) {
        for (i in 1 until size) {
            root.add(this[i])
        }
    }
    return root
}
