package net.waymire.playground.kotlin.data.tree.bptree

import net.waymire.playground.kotlin.SortedList
import net.waymire.playground.kotlin.sortedListOf

//region Support Types
//endregion

//region Properties
val <K : Comparable<K>, V> TreeNode<K, V>.isRoot get() = parent == null
val <K : Comparable<K>, V> TreeNode<K, V>.isNotRoot get() = !isRoot
val <K : Comparable<K>, V> TreeNode<K, V>.isLeaf get() = this.children.isEmpty()
val <K : Comparable<K>, V> TreeNode<K, V>.isNotLeaf get() = !isLeaf
val <K : Comparable<K>, V> TreeNode<K, V>.isFull get() = keys.size >= maxKeySize
val <K : Comparable<K>, V> TreeNode<K, V>.isNotFull get() = !isFull
val <K : Comparable<K>, V> TreeNode<K, V>.isOverflow get() = keys.size > maxKeySize
val <K : Comparable<K>, V> TreeNode<K, V>.isUnderflow get() = keys.size < minKeySize
val <K : Comparable<K>, V> TreeNode<K, V>.hasExtraKeyCapacity get() = keys.size > minKeySize
//endregion

//region Helpers
fun <K : Comparable<K>, V> TreeNode<K, V>.adopt(other: TreeNode<K, V>) {
    other.children.forEach { it.parent = other }
    other.records.removeIf { !other.keys.contains(it.key) }
    this.children.add(other)
}
//endregion

//region Contains, Add, Remove
fun <K : Comparable<K>, V> TreeNode<K, V>.containsKey(key: K): Boolean {
    return findKey(key) != null
}

fun <K : Comparable<K>, V> TreeNode<K, V>.put(key: K, value: V): TreeNode<K, V> {
    var current: TreeNode<K, V> = this

    outer@ while (true) {
        if (current.isLeaf) {
            current.keys.add(key)
            current.records.add(Record(key, value))
            while (current.isOverflow) current = current.split()
            return current
        }

        for ((i, v) in current.keys.withIndex()) {
            if (key < v) {
                current = current.children[i]
                continue@outer
            }
        }

        current = current.children.last()
    }
}

fun <K : Comparable<K>, V> TreeNode<K, V>.remove(key: K): TreeNode<K, V>? {
    val node = findKey(key) ?: return null
    if (node.hasExtraKeyCapacity) {
        node.keys.remove(key)
        return node
    }

    val promoted = node.promote(key)
    if (promoted != null) return promoted

    return node.promote(key)
}

fun <K : Comparable<K>, V> TreeNode<K, V>.promote(targetKey: K): TreeNode<K, V>? {
    val index = keys.indexOf(targetKey)
    if (index < 0) return null

    val leftChild = children.getOrNull(index)
    if (leftChild != null && leftChild.hasExtraKeyCapacity) {
        return leftChild.promotePredecessor(targetKey)
    }

    val rightChild = children.getOrNull(index+1)
    if (rightChild != null && rightChild.hasExtraKeyCapacity) {
        return rightChild.promoteSuccessor(targetKey)
    }

    return null
}

fun <K : Comparable<K>, V> TreeNode<K, V>.promotePredecessor(targetKey: K): TreeNode<K, V>? {
    val index = keys.indexOf(targetKey)
    val child = children.getOrNull(index) ?: return null
    val promoted = child.keys.removeLast()
    this.keys[index] = promoted
    return child
}

fun <K : Comparable<K>, V> TreeNode<K, V>.promoteSuccessor(targetKey: K): TreeNode<K, V>? {
    val index = keys.indexOf(targetKey)
    if (index < 0) return null
    val child = children.getOrNull(index+1) ?: return null
    val promoted = child.keys.removeFirst()
    this.keys[index] = promoted
    return child
}
//endregion

//region Search
fun <K : Comparable<K>, V> TreeNode<K, V>.findKey(key: K): TreeNode<K, V>? {
    var current: TreeNode<K, V> = this
    outer@ while (true) {
        if (current.keys.contains(key)) return current
        if (current.isLeaf) return null

        for ((i, v) in current.keys.withIndex()) {
            if (key < v) {
                current = current.children[i]
                continue@outer
            }
        }
        current = current.children.last()
    }
}
//endregion

//region Rotations
fun <K : Comparable<K>, V> TreeNode<K, V>.split(): TreeNode<K, V> {
    val medianIndex = keys.lastIndex / 2
    val medianValue = keys[medianIndex]

    val parent = this.parent ?: TreeNode(order)
    parent.children.remove(this)

    val left = TreeNode(
        order = order,
        parent = parent,
        keys = SortedList(keys.subList(0, medianIndex).toMutableList()),
        children = if (isNotLeaf) SortedList(children.subList(0, medianIndex + 1).toMutableList()) else sortedListOf(),
        records = if (isLeaf) SortedList(records.subList(0, medianIndex + 1).toMutableList()) else sortedListOf()
    )
    parent.adopt(left)

    val right = TreeNode(
        order = order,
        parent = parent,
        keys = SortedList(keys.subList(medianIndex, keys.size).toMutableList()),
        children = if (isNotLeaf) SortedList(children.subList(medianIndex + 1, children.size).toMutableList()) else sortedListOf(),
        records = if (isLeaf) SortedList(records.subList(medianIndex, records.size).toMutableList()) else sortedListOf()
    )
    parent.adopt(right)

    previous?.next = left
    left.next = right
    right.previous = left

    parent.keys.add(right.keys.first())
    if (isNotLeaf) right.keys.remove(medianValue)

    return parent
}

//endregion
