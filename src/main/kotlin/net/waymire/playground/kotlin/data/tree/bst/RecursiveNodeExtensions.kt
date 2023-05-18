package net.waymire.playground.kotlin.data.tree.bst

import kotlin.random.Random

//region Support Types
//endregion

//region Contains, Add, Remove
fun <T: Comparable<T>> BinarySearchTreeNode<T>.recursiveContains(value: T): Boolean {
    return this.findNode(value = value) != null
}

fun <T: Comparable<T>> BinarySearchTreeNode<T>.recursiveAdd(value: T): Boolean {
    if (this.value == value) return true
    return when {
        value < this.value -> addDirection(value, DIRECTION.LEFT)
        else -> addDirection(value, DIRECTION.RIGHT)
    }
}

fun <T: Comparable<T>> BinarySearchTreeNode<T>.recursiveRemove(value: T): Boolean {
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

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.addDirection(value: T, dir: DIRECTION): Boolean {
    val node = when(dir) {
        DIRECTION.LEFT -> left
        DIRECTION.RIGHT -> right
    }
    return when {
        node  == null -> {
            val newNode = BinarySearchTreeNode(value = value)
            when(dir) {
                DIRECTION.LEFT -> left = newNode
                DIRECTION.RIGHT -> right = newNode
            }
            true
        }
        node.value == value -> true
        else -> node.add(value)
    }
}
//endregion

//region Node Search
private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findInOrderSuccessor(): BinarySearchResult<T>? = right?.findLeftMostLeaf()

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findInOrderPredecessor(): BinarySearchResult<T>? = left?.findRightMostLeaf()

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findLeftMostLeaf(parent: BinarySearchTreeNode<T>? = null): BinarySearchResult<T> {
    return left?.findLeftMostLeaf(this) ?: BinarySearchResult(parent = parent, node = this)
}

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findRightMostLeaf(parent: BinarySearchTreeNode<T>? = null): BinarySearchResult<T> {
    return right?.findRightMostLeaf(this) ?: BinarySearchResult(parent = parent, node = this)
}

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findNode(parent: BinarySearchTreeNode<T>? = null, value: T): BinarySearchResult<T>? {
    if (value == this.value) return BinarySearchResult(parent = parent, node = this)
    if (value < this.value) return this.left?.findNode(this, value)
    return this.right?.findNode(this, value)
}

private fun <T: Comparable<T>> BinarySearchTreeNode<T>.findReplacementNodeForDelete(): BinarySearchResult<T>? {
    return if (Random.nextInt(0, 100) < 50)
        findInOrderSuccessor()
    else
        findInOrderPredecessor()
}
//endregion

//region Node Traversal
fun <T: Comparable<T>> BinarySearchTreeNode<T>.recursiveTraverseInOrder(): List<T> {
    fun recurse(node: BinarySearchTreeNode<T>, accumulator: MutableList<T>): List<T> {
        node.left?.let { recurse(it, accumulator) }
        accumulator.add(node.value)
        node.right?.let { recurse(it, accumulator) }
        return accumulator
    }
    return recurse(this, mutableListOf())
}

fun <T: Comparable<T>> BinarySearchTreeNode<T>.recursiveTraversePreOrder(): List<T> {
    fun recurse(node: BinarySearchTreeNode<T>, accumulator: MutableList<T>): List<T> {
        accumulator.add(node.value)
        node.left?.let { recurse(it, accumulator) }
        node.right?.let { recurse(it, accumulator) }
        return accumulator
    }
    return recurse(this, mutableListOf())
}

fun <T: Comparable<T>> BinarySearchTreeNode<T>.recursiveTraversePostOrder(): List<T> {
    fun recurse(node: BinarySearchTreeNode<T>, accumulator: MutableList<T>): List<T> {
        node.left?.let { recurse(it, accumulator) }
        node.right?.let { recurse(it, accumulator) }
        accumulator.add(node.value)
        return accumulator
    }
    return recurse(this,  mutableListOf())
}

fun <T: Comparable<T>> BinarySearchTreeNode<T>.recursiveTraverseBreadthFirst(): List<T> {
    fun recurse(deque: ArrayDeque<BinarySearchTreeNode<T>>, accumulator: MutableList<T>): List<T> {
        if (deque.isEmpty()) return accumulator
        val node = deque.removeFirst()
        accumulator.add(node.value)
        node.left?.let { deque.addLast(it) }
        node.right?.let { deque.addLast(it) }
        return recurse(deque, accumulator)
    }

    val deque = ArrayDeque<BinarySearchTreeNode<T>>()
    deque.addFirst(this)
    return recurse(deque, mutableListOf())
}
//endregion
