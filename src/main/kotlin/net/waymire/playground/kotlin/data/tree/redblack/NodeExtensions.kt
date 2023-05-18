package net.waymire.playground.kotlin.data.tree.redblack

import net.waymire.playground.kotlin.data.tree.bst.BinarySearchTreeNode
import kotlin.math.abs
import kotlin.math.max

//region Support Types
//endregion

//region Properties
val <T: Comparable<T>> RedBlackTreeNode<T>.isRoot get() = parent == null
val <T: Comparable<T>> RedBlackTreeNode<T>.isChild get() = !isRoot
val <T: Comparable<T>> RedBlackTreeNode<T>.isLeftChild get() = parent?.let { value < it.value } ?: false
val <T: Comparable<T>> RedBlackTreeNode<T>.isRightChild get() = parent?.let { value >= it.value } ?: false
val <T: Comparable<T>> RedBlackTreeNode<T>.isRed get() = !black
val <T: Comparable<T>> RedBlackTreeNode<T>.isBlack get() = black
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

    added.parent?.let {
        if (it.isRed) {
            added.checkColor()
        }
    }
    return added
}

fun <T: Comparable<T>> RedBlackTreeNode<T>.remove(value: T): Boolean {
    throw NotImplementedError()
}

fun <T: Comparable<T>> RedBlackTreeNode<T>.checkColor() {
    val parent = this.parent ?: return
    val grandParent = parent.parent ?: return

    val aunt = if (parent.isLeftChild) grandParent.right else grandParent.left
    val auntIsBlack = aunt?.isBlack ?: true
    if (auntIsBlack) {
        //rotate and recolor
    } else {
        //color flip
    }
}
//endregion

//region Node Search
//endregion

//region Node Traversal
//endregion

//region Tree Height
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

fun <T: Comparable<T>> RedBlackTreeNode<T>.updateTreeHeightBottomUp2() {
    var node: RedBlackTreeNode<T> = this
    var height = if (node.isBlack) 1 else 0
    while(node.parent != null) {

    }
}
fun <T: Comparable<T>> RedBlackTreeNode<T>.updateTreeHeightBottomUp() {
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

private fun <T: Comparable<T>> RedBlackTreeNode<T>.clearHeight() {
    this.leftHeight = 0
    this.rightHeight = 0
}
//endregion