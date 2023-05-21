package net.waymire.playground.kotlin.data.tree.btree

import net.waymire.playground.kotlin.SortedList
import net.waymire.playground.kotlin.sortedListOf
import java.util.Stack

//region Support Types
//endregion

//region Properties
val <T : Comparable<T>> BTreeNode<T>.isRoot get() = parent == null
val <T : Comparable<T>> BTreeNode<T>.isNotRoot get() = !isRoot
val <T : Comparable<T>> BTreeNode<T>.isLeaf get() = children.isEmpty()
val <T : Comparable<T>> BTreeNode<T>.isNotLeaf get() = !isLeaf
val <T : Comparable<T>> BTreeNode<T>.isFull get() = keys.size >= maxKeySize || children.size >= maxChildrenSize
val <T : Comparable<T>> BTreeNode<T>.isNotFull get() = !isFull
val <T : Comparable<T>> BTreeNode<T>.isOverSized get() = keys.size > maxKeySize || children.size > maxChildrenSize
val <T : Comparable<T>> BTreeNode<T>.isUnderSized get() = keys.size < minKeySize || children.size < minChildrenSize
//endregion

//region Contains, Add, Remove
fun <T : Comparable<T>> BTreeNode<T>.contains(value: T): Boolean {
    return findNode(value) != null
}

fun <T : Comparable<T>> BTreeNode<T>.add(value: T): BTreeNode<T> {
    var current: BTreeNode<T> = this

    outer@ while (true) {
        if (current.isLeaf) {
            current.keys.add(value)
            while (current.isOverSized) current = current.split()
            return current
        }

        for ((i, v) in current.keys.withIndex()) {
            if (value < v) {
                current = children[i]
                continue@outer
            }
        }

        current = children.last()
    }
}

fun <T : Comparable<T>> BTreeNode<T>.remove(value: T): BTreeNode<T>? {
    val node = findNode(value) ?: return null
    node.keys.remove(value)
    if (node.isUnderSized) {

    }
    return node
}

private fun <T : Comparable<T>> BTreeNode<T>.clear() {
    this.keys.clear()
    this.children.clear()
    this.parent = null
}
//endregion

//region Search
fun <T : Comparable<T>> BTreeNode<T>.findNode(value: T): BTreeNode<T>? {
    var current: BTreeNode<T> = this
    outer@ while (true) {
        if (current.isLeaf) return if (current.keys.contains(value)) current else null

        for ((i, v) in current.keys.withIndex()) {
            if (value < v) {
                current = children[i]
                continue@outer
            }
        }
        current = children.last()
    }
}
//endregion

//region Rotations
fun <T : Comparable<T>> BTreeNode<T>.split(): BTreeNode<T> {
    val medianIndex = (keys.size / 2) - 1
    val medianValue = keys[medianIndex]

    val p = this.parent ?: BTreeNode(order)
    p.keys.add(medianValue)
    p.children.remove(this)

    val left = BTreeNode(
        order = order,
        parent = p,
        keys = SortedList(keys.subList(0, medianIndex).toMutableList()),
        children = if (isNotLeaf) SortedList(children.subList(0, medianIndex + 1).toMutableList()) else sortedListOf()
    )
    p.children.add(left)

    val right = BTreeNode(
        order = order,
        parent = p,
        keys = SortedList(keys.subList(medianIndex + 1, keys.size).toMutableList()),
        children = if (isNotLeaf) SortedList(
            children.subList(medianIndex + 1, children.size).toMutableList()
        ) else sortedListOf()
    )
    p.children.add(right)

    return p
}

//endregion

//region Traversal
fun <T : Comparable<T>> BTreeNode<T>.traverseInOrder(): List<T> {
    val stack: Stack<BTreeNode<T>> = Stack()
    val accumulator: MutableList<T> = mutableListOf()
    var current: BTreeNode<T>? = this

    while(current != null) {
        stack.push(current)
        current = current.children.firstOrNull()
    }
    if (stack.empty()) return accumulator

    while(stack.isNotEmpty()) {
        current = stack.pop()
        if (current.isLeaf) {
            accumulator.addAll(current.keys)
            continue
        }
        var index = 0
        while(index < current.keys.size) {
            accumulator.add(current.keys[index])
            accumulator.addAll(current.children[index+1].keys)
            index++
        }
    }
    return accumulator
}
//endregion