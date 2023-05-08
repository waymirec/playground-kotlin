package net.waymire.playground.kotlin.data

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class AVLTreeNode<T: Comparable<T>>(
    val value: T,
    var parent: AVLTreeNode<T>? = null,
    var left: AVLTreeNode<T>? = null,
    var right: AVLTreeNode<T>? = null
) {
    private val leftChildCount: AtomicInteger = AtomicInteger(0)
    private val rightChildCount: AtomicInteger = AtomicInteger(0)

    fun add(value: T): Boolean {
        var current: AVLTreeNode<T> = this
        while(true) {
            // allow duplicates? set vs list logic
            // if (current.value == value) return true
            if (value < current.value) {
                current.leftChildCount.incrementAndGet()
                val targetNode = current.left
                if (targetNode != null) {
                    current = targetNode
                    continue
                } else {
                    current.left = AVLTreeNode(parent = current, value = value)
                    return true
                }
            } else {
                current.rightChildCount.incrementAndGet()
                val targetNode = current.right
                if (targetNode != null) {
                    current = targetNode
                    continue
                } else {
                    current.right = AVLTreeNode(parent = current, value = value)
                    return true
                }
            }
        }
    }

    fun leftRightRotation() {
        val nodeParent = parent ?: throw IllegalStateException("unable to Left-Right-Rotate a tree with a height of 0");
        val nodeGrandparent = nodeParent.parent ?: throw IllegalStateException("unable to Left-Right-Rotate a tree with a height of 1")

        left = nodeParent
        parent = nodeGrandparent
        nodeParent.parent = this
        nodeGrandparent.left = this
    }

    val balance get() = Pair(leftChildCount.get(), rightChildCount.get())
    val isBalanced get() = abs(leftChildCount.get() - rightChildCount.get()) < 2

    fun inOrder(accumulator: MutableList<T> = mutableListOf()): List<T> {
        left?.inOrder(accumulator)
        accumulator.add(value)
        right?.inOrder(accumulator)
        return accumulator
    }

    /*
    fun breadthFirstOrder(accumulator: MutableList<T> = mutableListOf()): List<T> {

    }
*/

    override fun equals(other: Any?): Boolean {
        other?.let {
            if (other is AVLTreeNode<*>) {
                return this.inOrder() == other.inOrder()
            }
        }
        return false
    }

    override fun hashCode() = inOrder().hashCode()
}

fun <T: Comparable<T>> List<T>.toAVLTree(): AVLTreeNode<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val root = AVLTreeNode(value = first())
    if (size > 1) {
        for (i in 1 until size) {
            root.add(this[i])
        }
    }
    return root
}
