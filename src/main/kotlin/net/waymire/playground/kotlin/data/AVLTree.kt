package net.waymire.playground.kotlin.data

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class AVLTree<T: Comparable<T>>(
    rootValue: T
) {
    private var root: AVLTreeNode<T> = AVLTreeNode(rootValue)

    fun add(value: T): Boolean {
        val added = root.add(value)
        val target = added.parent?.parent
        target?.let {
            if (!it.isBalanced) {
                it.leftRotation()
                root = added
            }
        }

        return true
    }

    val balance get() = root.balance
    val balanceFactor get() = root.balanceFactor
    val isBalanced get() = root.isBalanced
}

class AVLTreeNode<T: Comparable<T>>(
    val value: T,
    var parent: AVLTreeNode<T>? = null,
    var left: AVLTreeNode<T>? = null,
    var right: AVLTreeNode<T>? = null
) {
    private val leftChildCount: AtomicInteger = AtomicInteger(0)
    private val rightChildCount: AtomicInteger = AtomicInteger(0)

    fun add(value: T): AVLTreeNode<T> {
        var current: AVLTreeNode<T> = this
        val newNode: AVLTreeNode<T>?
        while(true) {
            if (current.value == value) return current
            if (value < current.value) {
                current.leftChildCount.incrementAndGet()
                val targetNode = current.left
                if (targetNode != null) {
                    current = targetNode
                    continue
                } else {
                    newNode = AVLTreeNode(parent = current, value = value)
                    current.left = newNode
                    return newNode
                }
            } else {
                current.rightChildCount.incrementAndGet()
                val targetNode = current.right
                if (targetNode != null) {
                    current = targetNode
                    continue
                } else {
                    newNode = AVLTreeNode(parent = current, value = value)
                    current.right = newNode
                    return newNode
                }
            }
        }
    }

    fun leftRotation() {
        val rightChild = this.right ?: throw IllegalStateException("cannot perform left rotation on a node with no right child")
        rightChild.parent = this.parent
        this.parent = rightChild
        rightChild.left = this
    }

    fun leftRightRotation() {
        val nodeParent = parent ?: throw IllegalStateException("unable to Left-Right-Rotate a tree with a height of 0");
        val nodeGrandparent = nodeParent.parent ?: throw IllegalStateException("unable to Left-Right-Rotate a tree with a height of 1")

        parent = null
        left = nodeParent
        nodeParent.parent = this

        right = nodeGrandparent
        nodeGrandparent.parent = this
    }

    val balance get() = Pair(leftChildCount.get(), rightChildCount.get())
    val balanceFactor get() = abs(leftChildCount.get() - rightChildCount.get())
    val isBalanced get() = balanceFactor < 2

    fun inOrder(accumulator: MutableList<T> = mutableListOf()): List<T> {
        left?.inOrder(accumulator)
        accumulator.add(value)
        right?.inOrder(accumulator)
        return accumulator
    }

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

fun <T: Comparable<T>> List<T>.toAVLTree(): AVLTree<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val tree = AVLTree(rootValue = first())
    if (size > 1) {
        for (i in 1 until size) {
            tree.add(this[i])
        }
    }
    return tree
}
