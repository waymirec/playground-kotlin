package net.waymire.playground.kotlin.data.tree.redblack

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class RedBlackTreeTest {
    @Test
    fun `given a balanced Red-Black Tree, when balance is checked, then return true`() {
        val values =    listOf(5, 8, 1, 10, 9, 15, 20)
        val tree = values.toRedBlackTree()
        assertTrue(tree.isBalanced)
        assertFalse(tree.isLeftHeavy)
        assertFalse(tree.isRightHeavy)
    }
}