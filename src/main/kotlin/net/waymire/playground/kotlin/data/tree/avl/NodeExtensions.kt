package net.waymire.playground.kotlin.data.tree.avl

import java.util.Stack
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

//region Support Types
private enum class DIRECTION { LEFT, RIGHT }
//endregion

//region Properties
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.balanceFactor get() = leftHeight - rightHeight
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isBalanced get() = abs(balanceFactor) < 2
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isNotBalanced get() = !isBalanced
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isLeftHeavy get() = balanceFactor > 0
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isLeftRightHeavy get() = isLeftHeavy && left!!.isRightHeavy
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isRightHeavy get() = balanceFactor < 0
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isRightLeftHeavy get() = isRightHeavy && right!!.isLeftHeavy
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.height get() = max(leftHeight, rightHeight)
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isLeafNode get() = left == null && right == null
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isNotLeafNode get() = !isLeafNode
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isRoot get() = parent == null
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.hasParent get() = parent != null
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isChild get() = !isRoot
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isLeftChild get() =  parent?.let { value < it.value } ?: false
val <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.isRightChild get() = parent?.let { value >= it.value } ?: false
//endregion

//region Contains, Add, Remove
fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.contains(value: T): Boolean {
    var current: BalancedBinarySearchTreeNode<T> = this
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

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.add(value: T): BalancedBinarySearchTreeNode<T> {
    var current: BalancedBinarySearchTreeNode<T> = this
    while(true) {
        if (current.value == value) return current
        if (value < current.value) {
            val targetNode = current.left
            if (targetNode != null) {
                current = targetNode
                continue
            }
            val added = BalancedBinarySearchTreeNode(value = value, parent = current)
            current.left = added
            current = added
            break
        } else {
            val targetNode = current.right
            if (targetNode != null) {
                current = targetNode
                continue
            }
            val added = BalancedBinarySearchTreeNode(value = value, parent = current)
            current.right = added
            current = added
            break
        }
    }
    return current
}

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.remove(value: T): BalancedBinarySearchTreeNode<T>? {
    val nodeToBeDeleted = findNode(value = value) ?: return null
    val parent = nodeToBeDeleted.parent


    // target has no children
    if (nodeToBeDeleted.left == null && nodeToBeDeleted.right == null) {
        if (parent != null) {
            if (nodeToBeDeleted.isLeftChild) parent.left = null else parent.right = null
        }
        return nodeToBeDeleted
    }

    // target has two children
    if (nodeToBeDeleted.left != null && nodeToBeDeleted.right != null) {
        val candidateNode = nodeToBeDeleted.findReplacementNodeForDelete() ?: throw IllegalStateException()
        this.remove(candidateNode.value)
        nodeToBeDeleted.value = candidateNode.value
        return nodeToBeDeleted
    }

    // target has one child
    val survivor = nodeToBeDeleted.left ?: nodeToBeDeleted.right
    survivor?.parent = parent
    nodeToBeDeleted.left = null
    nodeToBeDeleted.right = null
    if (parent != null) {
        if (nodeToBeDeleted.isLeftChild) parent.left = survivor else parent.right = survivor
    }
    return nodeToBeDeleted

}
//endregion

//region Node Search
private fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.findNode(value: T): BalancedBinarySearchTreeNode<T>? {
    if (value == this.value) return this
    var current: BalancedBinarySearchTreeNode<T>? = this
    while(current != null) {
        if (value == current.value) return current
        current = if (value < current.value) current.left else current.right
    }
    return null
}

private fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.findInOrderSuccessor(): BalancedBinarySearchTreeNode<T>? = right?.findLeftMostLeaf()

private fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.findInOrderPredecessor(): BalancedBinarySearchTreeNode<T>? = left?.findRightMostLeaf()

private fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.findFarthestLeaf(dir: DIRECTION): BalancedBinarySearchTreeNode<T> {
    var current: BalancedBinarySearchTreeNode<T> = this
    while (true) {
        val child = when(dir) {
            DIRECTION.LEFT -> current.left
            DIRECTION.RIGHT -> current.right
        } ?: return current
        current = child
    }
}

private fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.findLeftMostLeaf() = findFarthestLeaf(DIRECTION.LEFT)

private fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.findRightMostLeaf() = findFarthestLeaf(DIRECTION.RIGHT)

private fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.findReplacementNodeForDelete(): BalancedBinarySearchTreeNode<T>? {
    return if (Random.nextInt(0, 100) < 150)
        findInOrderSuccessor()
    else
        findInOrderPredecessor()
}
//endregion

//region Node Rotations
fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.rightRotate(): BalancedBinarySearchTreeNode<T> {
    val leftChild = this.left ?: throw IllegalStateException()
    this.left = leftChild.right
    leftChild.right?.parent = this
    leftChild.right = this
    this.parent?.let { if (isLeftChild) it.left = leftChild else it.right = leftChild }
    leftChild.parent = this.parent
    this.parent = leftChild
    return leftChild
}

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.leftRotate(): BalancedBinarySearchTreeNode<T> {
    val rightChild = this.right ?: throw IllegalStateException()
    this.right = rightChild.left
    rightChild.left?.parent = this
    rightChild.left = this
    this.parent?.let { if (isLeftChild) it.left = rightChild else it.right = rightChild }
    rightChild.parent = this.parent
    this.parent = rightChild
    return rightChild
}

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.leftRightRotate(): BalancedBinarySearchTreeNode<T> {
    val rightChild = this.right ?: throw IllegalStateException()
    rightChild.rightRotate()
    return this.leftRotate()
}

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.rightLeftRotate(): BalancedBinarySearchTreeNode<T> {
    val leftChild = this.left ?: throw IllegalStateException()
    leftChild.leftRotate()
    return this.rightRotate()
}
//endregion

//region Node Traversal
fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.traverseInOrder(): List<T> {
    val stack: Stack<BalancedBinarySearchTreeNode<T>> = Stack()
    val accumulator: MutableList<T> = mutableListOf()
    var current: BalancedBinarySearchTreeNode<T>? = this
    while(true) {
        while(current != null) {
            stack.push(current)
            current = current.left
        }
        if (stack.empty()) break

        current = stack.pop()
        accumulator.add(current.value)
        current = current.right
    }
    return accumulator
}

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.traversePreOrder(): List<T> {
    val queue: ArrayDeque<BalancedBinarySearchTreeNode<T>> = ArrayDeque()
    val accumulator: MutableList<T> = mutableListOf()
    var current: BalancedBinarySearchTreeNode<T>? = this
    while(true) {
        while(current != null) {
            accumulator.add(current.value)
            queue.addFirst(current)
            current = current.left
        }
        if (queue.isEmpty()) break

        current = queue.removeFirst().right
    }
    return accumulator
}

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.traversePostOrder(): List<T> {
    val stack1: Stack<BalancedBinarySearchTreeNode<T>> = Stack()
    val stack2: Stack<BalancedBinarySearchTreeNode<T>> = Stack()
    stack1.push(this)

    while(stack1.isNotEmpty()) {
        val current = stack1.pop()
        stack2.push(current)
        current.left?.let { stack1.push(it) }
        current.right?.let { stack1.push(it) }
    }

    return stack2.toList().map { it.value }.reversed()
}

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.traverseBreadthFirst(): List<T> {
    val accumulator: MutableList<T> = mutableListOf()
    val deque = ArrayDeque<BalancedBinarySearchTreeNode<T>>()
    deque.addFirst(this)

    while(deque.isNotEmpty()) {
        val node = deque.removeFirst()
        accumulator.add(node.value)
        node.left?.let { deque.addLast(it) }
        node.right?.let { deque.addLast(it) }
    }

    return accumulator
}
//endregion

//region Tree Height
fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.updateParentHeight() {
    val parent = this.parent ?: return
    if (isLeftChild)
        parent.leftHeight = max(parent.leftHeight, height+1)
    else
        parent.rightHeight = max(parent.rightHeight, height+1)
}

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.updateTreeHeightTopDown() {
    val queue: ArrayDeque<BalancedBinarySearchTreeNode<T>> = ArrayDeque()
    clearHeight()
    queue.addFirst(this)
    while(queue.isNotEmpty()) {
        val n = queue.removeFirst()
        if (n.isLeafNode) n.updateTreeHeightBottomUp()
        n.left?.let { it.clearHeight(); queue.addFirst(it) }
        n.right?.let { it.clearHeight(); queue.addFirst(it) }
    }
}

fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.updateTreeHeightBottomUp() {
    if (isRoot) return
    var node: BalancedBinarySearchTreeNode<T>? = this
    node?.clearHeight()
    while(node != null) {
        node.updateParentHeight()
        node = node.parent
    }
}

private fun <T: Comparable<T>> BalancedBinarySearchTreeNode<T>.clearHeight() {
    this.leftHeight = 0
    this.rightHeight = 0
}

//endregion
