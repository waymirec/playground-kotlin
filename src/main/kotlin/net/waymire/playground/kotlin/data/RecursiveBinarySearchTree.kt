package net.waymire.playground.kotlin.data

import kotlin.random.Random

class RecursiveBinarySearchTree<T: Comparable<T>>(rootValue: T) {
    private var root = RecursiveBinarySearchTreeNode(rootValue)

    fun traverseInOrder() = root.traverseInOrder()
    fun traversePreOrder() = root.traversePreOrder()
    fun traversePostOrder() = root.traversePostOrder()
    fun traverseBreadthFirst() = root.traverseBreadthFirst()
    fun contains(value: T) = root.contains(value)
    fun add(value: T) = root.add(value)
    fun remove(value: T) = root.remove(value)
}

data class RecursiveBinarySearchResult<T: Comparable<T>>(
    val parent: RecursiveBinarySearchTreeNode<T>? = null,
    val node: RecursiveBinarySearchTreeNode<T>
)

class RecursiveBinarySearchTreeNode<T: Comparable<T>>(
    private var value: T,
    private var left: RecursiveBinarySearchTreeNode<T>? = null,
    private var right: RecursiveBinarySearchTreeNode<T>? = null
) {
    private enum class DIRECTION { LEFT, RIGHT }

    fun contains(value: T): Boolean {
        return this.findNode(value = value) != null
    }

    fun add(value: T): Boolean {
        if (this.value == value) return true
        return when {
            value < this.value -> addDirection(value, DIRECTION.LEFT)
            else -> addDirection(value, DIRECTION.RIGHT)
        }
    }

    fun remove(value: T): Boolean {
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

    fun traverseInOrder(): List<T> {
        fun recurse(node: RecursiveBinarySearchTreeNode<T>, accumulator: MutableList<T>): List<T> {
            node.left?.let { recurse(it, accumulator) }
            accumulator.add(node.value)
            node.right?.let { recurse(it, accumulator) }
            return accumulator
        }
        return recurse(this, mutableListOf())
    }

    fun traversePreOrder(): List<T> {
        fun recurse(node: RecursiveBinarySearchTreeNode<T>, accumulator: MutableList<T>): List<T> {
            accumulator.add(node.value)
            node.left?.let { recurse(it, accumulator) }
            node.right?.let { recurse(it, accumulator) }
            return accumulator
        }
        return recurse(this, mutableListOf())
    }

    fun traversePostOrder(): List<T> {
        fun recurse(node: RecursiveBinarySearchTreeNode<T>, accumulator: MutableList<T>): List<T> {
            node.left?.let { recurse(it, accumulator) }
            node.right?.let { recurse(it, accumulator) }
            accumulator.add(node.value)
            return accumulator
        }
        return recurse(this,  mutableListOf())
    }

    fun traverseBreadthFirst(): List<T> {
        fun recurse(deque: ArrayDeque<RecursiveBinarySearchTreeNode<T>>, accumulator: MutableList<T>): List<T> {
            if (deque.isEmpty()) return accumulator
            val node = deque.removeFirst()
            accumulator.add(node.value)
            node.left?.let { deque.addLast(it) }
            node.right?.let { deque.addLast(it) }
            return recurse(deque, accumulator)
        }

        val deque = ArrayDeque<RecursiveBinarySearchTreeNode<T>>()
        deque.addFirst(this)
        return recurse(deque, mutableListOf())
    }

    private fun findInOrderSuccessor(): RecursiveBinarySearchResult<T>? = right?.findLeftMostLeaf()

    private fun findInOrderPredecessor(): RecursiveBinarySearchResult<T>? = left?.findRightMostLeaf()

    private fun findLeftMostLeaf(parent: RecursiveBinarySearchTreeNode<T>? = null): RecursiveBinarySearchResult<T> = left?.let { it.findLeftMostLeaf(this) } ?: RecursiveBinarySearchResult(parent = parent, node = this)

    private fun findRightMostLeaf(parent: RecursiveBinarySearchTreeNode<T>? = null): RecursiveBinarySearchResult<T> = right?.let { it.findRightMostLeaf(this) } ?: RecursiveBinarySearchResult(parent = parent, node = this)

    private fun findNode(parent: RecursiveBinarySearchTreeNode<T>? = null, value: T): RecursiveBinarySearchResult<T>? {
        if (value == this.value) return RecursiveBinarySearchResult(parent = parent, node = this)
        if (value < this.value) return this.left?.findNode(this, value)
        return this.right?.findNode(this, value)
    }

    private fun findReplacementNodeForDelete(): RecursiveBinarySearchResult<T>? {
        return if (Random.nextInt(0, 100) < 50)
            findInOrderSuccessor()
        else
            findInOrderPredecessor()
    }

    private fun addDirection(value: T, dir: DIRECTION): Boolean {
        val node = when(dir) {
            DIRECTION.LEFT -> left
            DIRECTION.RIGHT -> right
        }
        return when {
            node  == null -> {
                val newNode = RecursiveBinarySearchTreeNode(value = value)
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
}

fun <T: Comparable<T>> Collection<T>.toRecursiveBinarySearchTree(): RecursiveBinarySearchTree<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val iterator = iterator()
    val tree = RecursiveBinarySearchTree(iterator.next())
    while(iterator.hasNext()) tree.add(iterator.next())
    return tree
}
