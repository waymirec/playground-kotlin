package net.waymire.playground.kotlin.data.tree.redblack

class RedBlackTree<T: Comparable<T>>(rootValue: T) {
    private var root = RedBlackTreeNode(value = rootValue, black = true)

    val height get() = root.height

    fun contains(value: T) = root.contains(value)

    fun add(value: T): Boolean {
        val newNode = root.add(value)
        if (newNode.parent.isBlack) return true

        newNode.checkTree()
        root.updateHeight()
        return true
    }

    fun remove(value: T) = root.remove(value)

    private fun RedBlackTreeNode<T>.checkTree() {
        var current: RedBlackTreeNode<T>? = this

        while(current != null) {
            if (current.isRed && current.parent.isRed) {
                current.correctTree()
            }
            current = current.parent
        }
    }

    private fun RedBlackTreeNode<T>.correctTree() {
        val parent = this.parent ?: return
        val grandParent = parent.parent ?: return

        if (aunt.isRed) {
            colorFlip()
            return
        }

        val updated = when (Pair(this.isLeftChild, parent.isLeftChild)) {
            Pair(true, false) ->  grandParent.rightLeftRotate()
            Pair(false, true) -> grandParent.leftRightRotate()
            Pair(false, false) -> grandParent.leftRotate()
            Pair(true, true) -> grandParent.rightRotate()
            else -> this
        }
        if (updated.parent == null) root = updated
        parent.colorFix()
    }

    private fun RedBlackTreeNode<T>.colorFlip() {
        this.grandParent?.red = true
        aunt?.black = true
        this.parent?.black = true
        root.black = true
    }

    private fun RedBlackTreeNode<T>.colorFix() {
        this.black = true
        this.left?.red = true
        this.right?.red = true
    }
}
