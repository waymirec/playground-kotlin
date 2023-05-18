package net.waymire.playground.kotlin.data.tree.bst

import java.util.Stack
import kotlin.random.Random

//region Support Types
enum class DIRECTION { LEFT, RIGHT }

data class BinarySearchResult<T: Comparable<T>>(
    val parent: BinarySearchTreeNode<T>? = null,
    val node: BinarySearchTreeNode<T>
)
//endregion

//region Properties
val <T: Comparable<T>> BinarySearchTreeNode<T>.isLeafNode get() = left == null && right == null
val <T: Comparable<T>> BinarySearchTreeNode<T>.isNotLeafNode get() = !isLeafNode

//endregion

//region Contains, Add, Remove
fun <T: Comparable<T>> BinarySearchTreeNode<T>.contains(value: T): Boolean {
    var current: BinarySearchTreeNode<T> = this
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

fun <T: Comparable<T>> BinarySearchTreeNode<T>.add(value: T): Boolean {
    var current: BinarySearchTreeNode<T> = this
    while(true) {
        if (current.value == value) return true
        if (value < current.value) {
            val targetNode = current.left
            if (targetNode != null) {
                current = targetNode
                continue
            } else {
                current.left = BinarySearchTreeNode(value = value)
                return true
            }
        } else {
            val targetNode = current.right
            if (targetNode != null) {
                current = targetNode
                continue
            } else {
                current.right = BinarySearchTreeNode(value = value)
                return true
            }
        }
    }
}

fun <T: Comparable<T>> BinarySearchTreeNode<T>.remove(value: T): Boolean {
    val searchResult = findNode(parent = this, value = value) ?: return false
    val parent = searchResult.parent
    val nodeToBeDeleted = searchResult.node

    // target has no children
    if (nodeToBeDeleted.left == null && nodeToBeDeleted.right == null) {
        if (parent != null) {
            if (nodeToBeDeleted.value < parent.value) parent.left = null else parent.right = null
        }
        return true
    }

    // target has two children
    if (nodeToBeDeleted.left != null && nodeToBeDeleted.right != null) {
        val candidateResult = nodeToBeDeleted.findReplacementNodeForDelete() ?: throw IllegalStateException()
        val candidateNode = candidateResult.node
        this.remove(candidateNode.value)
        nodeToBeDeleted.value = candidateNode.value
        return true
    }

    // target has one child
    val survivor = nodeToBeDeleted.left ?: nodeToBeDeleted.right
    nodeToBeDeleted.left = null
    nodeToBeDeleted.right = null
    if (parent != null) {
        if (nodeToBeDeleted.value < parent.value) parent.left = survivor else parent.right = survivor
    }
    return true
}
//endregion

//region Node Search
private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findInOrderSuccessor(): BinarySearchResult<T>? = right?.findLeftMostLeaf()

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findInOrderPredecessor(): BinarySearchResult<T>? = left?.findRightMostLeaf()

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findFarthestLeaf(parent: BinarySearchTreeNode<T>? = null, dir: DIRECTION): BinarySearchResult<T> {
    var current: BinarySearchTreeNode<T> = this
    var currentParent: BinarySearchTreeNode<T>? = parent
    while (true) {
        val child = when(dir) {
            DIRECTION.LEFT -> current.left
            DIRECTION.RIGHT -> current.right
        } ?: return BinarySearchResult(parent = currentParent, node = current)
        currentParent = current
        current = child
    }
}

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findLeftMostLeaf(parent: BinarySearchTreeNode<T>? = null) = findFarthestLeaf(parent = parent, dir = DIRECTION.LEFT)

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findRightMostLeaf(parent: BinarySearchTreeNode<T>? = null) = findFarthestLeaf(parent = parent, dir = DIRECTION.RIGHT)

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findNode(parent: BinarySearchTreeNode<T>? = null, value: T): BinarySearchResult<T>? {
    if (value == this.value) return BinarySearchResult(parent = parent, node = this)
    var current: BinarySearchTreeNode<T>? = this
    var currentParent: BinarySearchTreeNode<T>? = parent
    while(current != null) {
        if (value == current.value) return BinarySearchResult(parent = currentParent, node = current)
        currentParent = current
        current = if (value < current.value) current.left else current.right
    }
    return null
}

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findReplacementNodeForDelete(): BinarySearchResult<T>? {
    return if (Random.nextInt(0, 100) < 50)
        findInOrderSuccessor()
    else
        findInOrderPredecessor()
}
//endregion

//region Node Traversal
fun <T: Comparable<T>> BinarySearchTreeNode<T>.traverseInOrder(): List<T> {
    val stack: Stack<BinarySearchTreeNode<T>> = Stack()
    val accumulator: MutableList<T> = mutableListOf()
    var current: BinarySearchTreeNode<T>? = this
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

fun <T: Comparable<T>> BinarySearchTreeNode<T>.traversePreOrder(): List<T> {
    val queue: ArrayDeque<BinarySearchTreeNode<T>> = ArrayDeque()
    val accumulator: MutableList<T> = mutableListOf()
    var current: BinarySearchTreeNode<T>? = this
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

fun <T: Comparable<T>> BinarySearchTreeNode<T>.traversePostOrder(): List<T> {
    val stack1: Stack<BinarySearchTreeNode<T>> = Stack()
    val stack2: Stack<BinarySearchTreeNode<T>> = Stack()
    stack1.push(this)

    while(stack1.isNotEmpty()) {
        val current = stack1.pop()
        stack2.push(current)
        current.left?.let { stack1.push(it) }
        current.right?.let { stack1.push(it) }
    }

    return stack2.toList().map { it.value }.reversed()
}

fun <T: Comparable<T>> BinarySearchTreeNode<T>.traverseBreadthFirst(): List<T> {
    val accumulator: MutableList<T> = mutableListOf()
    val deque = ArrayDeque<BinarySearchTreeNode<T>>()
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

//region Misc
fun <T: Comparable<T>> Collection<T>.toBinarySearchTree(): BinarySearchTree<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val iterator = iterator()
    val tree = BinarySearchTree(iterator.next())
    while(iterator.hasNext()) tree.add(iterator.next())
    return tree
}
//endregion