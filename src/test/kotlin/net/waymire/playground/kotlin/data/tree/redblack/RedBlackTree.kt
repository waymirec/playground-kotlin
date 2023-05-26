package net.waymire.playground.kotlin.data.tree.redblack

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class RedBlackTreeTest {
    @Test
    fun `given a balanced tree with red leaves, when a new node is added, then recolor`() {
        val values = listOf(3, 1, 5, 6, 7, 8, 9, 10, 15, 20)
        val tree = values.toRedBlackTree()
        println("foo")
    }
}