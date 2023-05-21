package net.waymire.playground.kotlin.data.tree.redblack

import net.waymire.playground.kotlin.plus
import kotlin.math.abs
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
val <T: Comparable<T>> RedBlackTreeNode<T>.balanceFactor get() = leftHeight - rightHeight
val <T: Comparable<T>> RedBlackTreeNode<T>.isBalanced get() = abs(balanceFactor) < 2
val <T: Comparable<T>> RedBlackTreeNode<T>.isNotBalanced get() = !isBalanced
val <T: Comparable<T>> RedBlackTreeNode<T>.isLeftHeavy get() = balanceFactor > 0
val <T: Comparable<T>> RedBlackTreeNode<T>.isLeftRightHeavy get() = isLeftHeavy && left!!.isRightHeavy
val <T: Comparable<T>> RedBlackTreeNode<T>.isRightHeavy get() = balanceFactor < 0
val <T: Comparable<T>> RedBlackTreeNode<T>.isRightLeftHeavy get() = isRightHeavy && right!!.isLeftHeavy
val <T: Comparable<T>> RedBlackTreeNode<T>.height get() = max(leftHeight, rightHeight)
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
            added = RedBlackTreeNode(value = value, parent = current)
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
    val rightChild = this.right ?: throw IllegalStateException()
    rightChild.rightRotate()
    return this.leftRotate()
}

fun <T: Comparable<T>> RedBlackTreeNode<T>.rightLeftRotate(): RedBlackTreeNode<T> {
    val leftChild = this.left ?: throw IllegalStateException()
    leftChild.leftRotate()
    return this.rightRotate()
}
//endregion

//region Node Traversal
//endregion

//region Tree Height
fun <T: Comparable<T>> RedBlackTreeNode<T>.updateParentHeight() {
    val parent = this.parent ?: return
    if (isLeftChild)
        parent.leftHeight = max(parent.leftHeight, height + isBlack)
    else
        parent.rightHeight = max(parent.rightHeight, height + isBlack)
}

fun <T: Comparable<T>> RedBlackTreeNode<T>.updateTreeHeightTopDown() {
    val queue: ArrayDeque<RedBlackTreeNode<T>> = ArrayDeque()
    clearHeight()
    queue.addFirst(this)
    while(queue.isNotEmpty()) {
        val n = queue.removeFirst()
        if (n.isLeafNode) n.updateTreeHeightBottomUp()
        n.left?.let { it.clearHeight(); queue.addFirst(it) }
        n.right?.let { it.clearHeight(); queue.addFirst(it) }
    }
}

fun <T: Comparable<T>> RedBlackTreeNode<T>.updateTreeHeightBottomUp() {
    if (isRoot) return
    var node: RedBlackTreeNode<T>? = this
    node?.clearHeight()
    while(node != null) {
        node.updateParentHeight()
        node = node.parent
    }
}

private fun <T: Comparable<T>> RedBlackTreeNode<T>.clearHeight() {
    this.leftHeight = 1
    this.rightHeight = 1
}

//endregion
