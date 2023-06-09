package net.waymire.playground.kotlin.data.tree.redblack

import java.util.Stack
import kotlin.math.max

//region Support Types
//endregion

//region Properties
val <T: Comparable<T>> RedBlackTreeNode<T>.isRoot get() = parent == null
val <T: Comparable<T>> RedBlackTreeNode<T>.isChild get() = !isRoot
val <T: Comparable<T>> RedBlackTreeNode<T>.isLeftChild get() = parent?.let { value < it.value } ?: false
val <T: Comparable<T>> RedBlackTreeNode<T>.isRightChild get() = parent?.let { value >= it.value } ?: false
val <T: Comparable<T>> RedBlackTreeNode<T>.grandParent get() = parent?.parent
val <T: Comparable<T>> RedBlackTreeNode<T>.aunt: RedBlackTreeNode<T>? get() {
    val parent =  this.parent ?: return null
    val grandParent = parent.parent ?: return null
    return if (parent.isLeftChild) grandParent.right else grandParent.left
}
var <T: Comparable<T>> RedBlackTreeNode<T>.red: Boolean
    get() = !black
    set(value) { black = !value }

val <T: Comparable<T>> RedBlackTreeNode<T>?.isRed get() = this?.let { !black } ?: false
val <T: Comparable<T>> RedBlackTreeNode<T>?.isBlack get() = this?.let { black } ?: true
val <T: Comparable<T>> RedBlackTreeNode<T>.isLeafNode get() = left == null && right == null
val <T: Comparable<T>> RedBlackTreeNode<T>.isNotLeafNode get() = !isLeafNode
//endregion

//region Contains, Add, Remove
fun <T: Comparable<T>> RedBlackTreeNode<T>.contains(value: T): Boolean {
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

fun <T: Comparable<T>> RedBlackTreeNode<T>.add(value: T): RedBlackTreeNode<T> {
    var current: RedBlackTreeNode<T> = this
    val added: RedBlackTreeNode<T>
    while(true) {
        if (current.value == value) return current
        if (value < current.value) {
            val targetNode = current.left
            if (targetNode != null) {
                current = targetNode
                continue
            }
            added = RedBlackTreeNode(value = value, parent = current, black = false)
            current.left = added
            break
        } else {
            val targetNode = current.right
            if (targetNode != null) {
                current = targetNode
                continue
            }
            added = RedBlackTreeNode(value = value, parent = current)
            current.right = added
            break
        }
    }
    return added
}

fun <T: Comparable<T>> RedBlackTreeNode<T>.remove(value: T): Boolean {
    throw NotImplementedError()
}
//endregion

//region Node Search
//endregion

//region Node Rotations
fun <T: Comparable<T>> RedBlackTreeNode<T>.rightRotate(): RedBlackTreeNode<T> {
    val leftChild = this.left ?: throw IllegalStateException()
    this.left = leftChild.right
    leftChild.right?.parent = this
    leftChild.right = this
    this.parent?.let { if (isLeftChild) it.left = leftChild else it.right = leftChild }
    leftChild.parent = this.parent
    this.parent = leftChild
    return leftChild
}


fun <T: Comparable<T>> RedBlackTreeNode<T>.leftRotate(): RedBlackTreeNode<T> {
    val rightChild = this.right ?: throw IllegalStateException()
    this.right = rightChild.left
    rightChild.left?.parent = this
    rightChild.left = this
    this.parent?.let { if (isLeftChild) it.left = rightChild else it.right = rightChild }
    rightChild.parent = this.parent
    this.parent = rightChild
    return rightChild
}

fun <T: Comparable<T>> RedBlackTreeNode<T>.leftRightRotate(): RedBlackTreeNode<T> {
    val leftChild = left ?: throw IllegalStateException()
    leftChild.leftRotate()
    return this.rightRotate()
}

fun <T: Comparable<T>> RedBlackTreeNode<T>.rightLeftRotate(): RedBlackTreeNode<T> {
    val rightChild = right ?: throw IllegalStateException()
    rightChild.rightRotate()
    return this.leftRotate()
}
//endregion

//region Node Traversal
//endregion

//region Tree Height
fun <T : Comparable<T>> RedBlackTreeNode<T>.updateHeight() {
    val stack1: Stack<RedBlackTreeNode<T>> = Stack()
    val queue = ArrayDeque<RedBlackTreeNode<T>>()
    stack1.push(this)
    while(stack1.isNotEmpty()) {
        val current = stack1.pop()
        queue.addFirst(current)
        current.left?.let { stack1.push(it) }
        current.right?.let { stack1.push(it) }
    }

    val iterator = queue.iterator()
    while(iterator.hasNext()) {
        val node = iterator.next()
        val nodeBlackHeight = if (node.isBlack) 1 else 0

        node.leftBlackHeight = node.left?.let { it.blackHeight + nodeBlackHeight } ?: nodeBlackHeight
        node.rightBlackHeight = node.right?.let { it.blackHeight + nodeBlackHeight } ?: nodeBlackHeight

        node.leftHeight = node.left?.let { it.height + 1 } ?: 0
        node.rightHeight = node.right?.let { it.height + 1 } ?: 0
    }
}
//endregion
