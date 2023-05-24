package net.waymire.playground.kotlin.data.tree.btree

import net.waymire.playground.kotlin.SortedList
import net.waymire.playground.kotlin.sortedListOf

//region Support Types
//endregion

//region Properties
val <T : Comparable<T>> TreeNode<T>.isRoot get() = parent == null
val <T : Comparable<T>> TreeNode<T>.isNotRoot get() = !isRoot
val <T : Comparable<T>> TreeNode<T>.isLeaf get() = children.isEmpty()
val <T : Comparable<T>> TreeNode<T>.isNotLeaf get() = !isLeaf
val <T : Comparable<T>> TreeNode<T>.isFull get() = keys.size >= maxKeySize || children.size >= maxChildrenSize
val <T : Comparable<T>> TreeNode<T>.isNotFull get() = !isFull
val <T : Comparable<T>> TreeNode<T>.isOverSized get() = keys.size > maxKeySize || children.size > maxChildrenSize
val <T : Comparable<T>> TreeNode<T>.isUnderSized get() = keys.size < minKeySize || children.size < minChildrenSize
val <T : Comparable<T>> TreeNode<T>.hasExtraKeyCapacity get() = keys.size > minKeySize
//endregion

//region Contains, Add, Remove
fun <T : Comparable<T>> TreeNode<T>.contains(value: T): Boolean {
    return findNode(value) != null
}

fun <T : Comparable<T>> TreeNode<T>.add(value: T): TreeNode<T> {
    var current: TreeNode<T> = this

    outer@ while (true) {
        if (current.isLeaf) {
            current.keys.add(value)
            while (current.isOverSized) current = current.split()
            return current
        }

        for ((i, v) in current.keys.withIndex()) {
            if (value < v) {
                current = current.children[i]
                continue@outer
            }
        }

        current = current.children.last()
    }
}

fun <T : Comparable<T>> TreeNode<T>.remove(value: T): TreeNode<T>? {
    val node = findNode(value) ?: return null
    if (node.hasExtraKeyCapacity) {
        node.keys.remove(value)
        return node
    }

    val promoted = node.promote(value)
    if (promoted != null) return promoted

    return node.promote(value)
}

fun <T : Comparable<T>> TreeNode<T>.promote(targetValue: T): TreeNode<T>? {
    val index = keys.indexOf(targetValue)
    if (index < 0) return null

    val leftChild = children.getOrNull(index)
    if (leftChild != null && leftChild.hasExtraKeyCapacity) {
        return leftChild.promotePredecessor(targetValue)
    }

    val rightChild = children.getOrNull(index+1)
    if (rightChild != null && rightChild.hasExtraKeyCapacity) {
        return rightChild.promoteSuccessor(targetValue)
    }

    return null
}

fun <T : Comparable<T>> TreeNode<T>.promotePredecessor(targetValue: T): TreeNode<T>? {
    val index = keys.indexOf(targetValue)
    val child = children.getOrNull(index) ?: return null
    val promoted = child.keys.removeLast()
    this.keys[index] = promoted
    return child
}

fun <T : Comparable<T>> TreeNode<T>.promoteSuccessor(targetValue: T): TreeNode<T>? {
    val index = keys.indexOf(targetValue)
    if (index < 0) return null
    val child = children.getOrNull(index+1) ?: return null
    val promoted = child.keys.removeFirst()
    this.keys[index] = promoted
    return child
}

private fun <T : Comparable<T>> TreeNode<T>.clear() {
    this.keys.clear()
    this.children.clear()
    this.parent = null
}
//endregion

//region Search
fun <T : Comparable<T>> TreeNode<T>.findNode(value: T): TreeNode<T>? {
    var current: TreeNode<T> = this
    outer@ while (true) {
        if (current.keys.contains(value)) return current
        if (current.isLeaf) return null

        for ((i, v) in current.keys.withIndex()) {
            if (value < v) {
                current = current.children[i]
                continue@outer
            }
        }
        current = current.children.last()
    }
}
//endregion

//region Rotations
fun <T : Comparable<T>> TreeNode<T>.split(): TreeNode<T> {
    val medianIndex = keys.lastIndex / 2
    val medianValue = keys[medianIndex]

    val p = this.parent ?: TreeNode(order)
    p.keys.add(medianValue)
    p.children.remove(this)

    val left = TreeNode(
        order = order,
        parent = p,
        keys = SortedList(keys.subList(0, medianIndex).toMutableList()),
        children = if (isNotLeaf) SortedList(children.subList(0, medianIndex + 1).toMutableList()) else sortedListOf()
    )
    p.children.add(left)

    val right = TreeNode(
        order = order,
        parent = p,
        keys = SortedList(keys.subList(medianIndex + 1, keys.size).toMutableList()),
        children = if (isNotLeaf) SortedList(
            children.subList(medianIndex + 1, children.size).toMutableList()
        ) else sortedListOf()
    )
    p.children.add(right)

    this.clear()
    return p
}

//endregion
