package net.waymire.playground.kotlin.data

import java.util.Stack
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class BalancedBinarySearchTree<T: Comparable<T>>(rootValue: T) {
    private var root = BalancedBinarySearchTreeNode(value = rootValue)

    val isBalanced get() = root.isBalanced
    val isLeftHeavy get() = root.isLeftHeavy
    val isRightHeavy get() = root.isRightHeavy
    val height get() = root.height

    fun traverseInOrder() = root.traverseInOrder()
    fun traversePreOrder() = root.traversePreOrder()
    fun traversePostOrder() = root.traversePostOrder()
    fun traverseBreadthFirst() = root.traverseBreadthFirst()
    fun contains(value: T) = root.contains(value)

    fun add(values: Iterable<T>): Int {
        val iterator = values.iterator()
        var count = 0
        while (iterator.hasNext()) {
            root.add(iterator.next())
            count++
        }
        root.updateTreeHeightTopDown()
        return count
    }

    fun add(value: T): Boolean {
        val result = root.add(value)
        result.updateTreeHeightBottomUp()

        if (root.isNotBalanced) {
            var node = result
            while(true) {
                val parent = node.parent
                if (node.isNotBalanced) break
                if (parent == null) break
                node = parent
            }

            if (node.isNotBalanced) {
                val updated = node.rightRotate()
                if (root.value == node.value) root = updated
                root.updateTreeHeightTopDown()
            }
        }
        return true
    }

    fun remove(value: T): Boolean {
        root.remove(value) ?: return false
        root.updateTreeHeightTopDown()
        return true
    }
}

class BalancedBinarySearchTreeNode<T: Comparable<T>>(
    var value: T,
    var parent: BalancedBinarySearchTreeNode<T>? = null,
    var left: BalancedBinarySearchTreeNode<T>? = null,
    var right: BalancedBinarySearchTreeNode<T>? = null,
    var leftHeight: Int = 0,
    var rightHeight: Int = 0
) {
    private enum class DIRECTION { LEFT, RIGHT }

    val balanceFactor get() = leftHeight - rightHeight
    val isBalanced get() = abs(balanceFactor) < 2
    val isNotBalanced get() = !isBalanced
    val isLeftHeavy get() = balanceFactor > 1
    val isRightHeavy get() = balanceFactor < -1
    val height get() = max(leftHeight, rightHeight)



    val isLeafNode get() = left == null && right == null
    val isNotLeafNode get() = !isLeafNode
    val isLeftChild get() = parent?.let { value < it.value } ?: false
    val isRightChild get() = parent?.let { value >= it.value } ?: false

    fun rightRotate(): BalancedBinarySearchTreeNode<T> {
        val leftChild = this.left ?: throw IllegalStateException()
        this.left = leftChild.right
        leftChild.right = this
        this.parent?.let { it.left = leftChild }
        leftChild.parent = this.parent
        this.parent = leftChild
        return leftChild
    }

    fun contains(value: T): Boolean {
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

    fun add(value: T): BalancedBinarySearchTreeNode<T> {
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

    fun remove(value: T): BalancedBinarySearchTreeNode<T>? {
        val nodeToBeDeleted = findNode(value = value) ?: return null
        val parent = nodeToBeDeleted.parent


        // target has no children
        if (nodeToBeDeleted.left == null && nodeToBeDeleted.right == null) {
            if (parent != null) {
                if (nodeToBeDeleted.value < parent.value) parent.left = null else parent.right = null
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
            if (nodeToBeDeleted.value < parent.value) parent.left = survivor else parent.right = survivor
        }
        return nodeToBeDeleted

    }

    fun traverseInOrder(): List<T> {
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
            continue
        }
        return accumulator
    }

    fun traversePreOrder(): List<T> {
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
            continue
        }
        return accumulator
    }

    fun traversePostOrder(): List<T> {
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

    fun traverseBreadthFirst(): List<T> {
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

    fun updateTreeHeightTopDown() {
        val queue: ArrayDeque<BalancedBinarySearchTreeNode<T>> = ArrayDeque()
        this.clearHeight()
        queue.addFirst(this)
        while(queue.isNotEmpty()) {
            val n = queue.removeFirst()
            if (n.isLeafNode) n.updateTreeHeightBottomUp()
            n.left?.let { it.clearHeight(); queue.addFirst(it) }
            n.right?.let { it.clearHeight(); queue.addFirst(it) }
        }
    }

    fun updateTreeHeightBottomUp() {
        var height = 0
        var node: BalancedBinarySearchTreeNode<T> = this
        while(true) {
            val parent = node.parent ?: break
            height++
            if (node.isLeftChild)
                parent.leftHeight = max(parent.leftHeight, height)
            else
                parent.rightHeight = max(parent.rightHeight, height)
            node = parent
        }
    }

    private fun findInOrderSuccessor(): BalancedBinarySearchTreeNode<T>? = right?.findLeftMostLeaf()

    private fun findInOrderPredecessor(): BalancedBinarySearchTreeNode<T>? = left?.findRightMostLeaf()

    private fun findFarthestLeaf(dir: DIRECTION): BalancedBinarySearchTreeNode<T> {
        var current: BalancedBinarySearchTreeNode<T> = this
        while (true) {
            val child = when(dir) {
                DIRECTION.LEFT -> current.left
                DIRECTION.RIGHT -> current.right
            } ?: return current
            current = child
        }
    }

    private fun findLeftMostLeaf() = findFarthestLeaf(DIRECTION.LEFT)

    private fun findRightMostLeaf() = findFarthestLeaf(DIRECTION.RIGHT)

    private fun findNode(value: T): BalancedBinarySearchTreeNode<T>? {
        if (value == this.value) return this
        var current: BalancedBinarySearchTreeNode<T>? = this
        while(current != null) {
            if (value == current.value) return current
            current = if (value < current.value) current.left else current.right
        }
        return null
    }

    private fun findReplacementNodeForDelete(): BalancedBinarySearchTreeNode<T>? {
        return if (Random.nextInt(0, 100) < 50)
            findInOrderSuccessor()
        else
            findInOrderPredecessor()
    }

    private fun clearHeight() {
        this.leftHeight = 0
        this.rightHeight = 0
    }
}

fun <T: Comparable<T>> Collection<T>.toBalancedBinarySearchTree(): BalancedBinarySearchTree<T> {
    if (isEmpty()) throw IllegalArgumentException("list cannot be empty")
    val iterator = iterator()
    val tree = BalancedBinarySearchTree(iterator.next())
    while(iterator.hasNext()) tree.add(iterator.next())
    return tree
}
