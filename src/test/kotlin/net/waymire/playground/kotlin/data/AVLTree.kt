package net.waymire.playground.kotlin.data

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class AVLTreeTest {
    @Test
    fun `given imbalanced tree, when balance inspected, then return correct balance`() {
        //given
        val values = listOf(43, 18, 22)

        //when
        val tree = values.toAVLTree()
        val tst = listOf(18,22,43).toAVLTree()
        assertEquals(tst, tree)

        //Then
        assertFalse(tree.isBalanced)
        assertEquals(Pair(2,0), tree.balance)
    }

    @Test
    fun `given a balanced tree, when an insertion causes right-heavy imbalance, then tree should self-balance with a lleft rotation`() {
        //given
        val values = listOf(10, 20, 30)
        val expected = listOf(20, 10, 30).toAVLTree()

        //when
        val tree = values.toAVLTree()

        //then
        assertTrue(tree.isBalanced)
        assertEquals(expected, tree)
    }

    @Test
    fun `given balanced tree, when an insertion causes a left-right imbalance, then tree should be balanced with a left-right-rotation`() {
        //given
        val values = listOf(43, 18, 22)
        val expected = listOf(43, 18, 22).toAVLTree()

        //when
        val tree = values.toAVLTree()

        //then
        assertTrue(tree.isBalanced)
        assertEquals(expected, tree)
    }
}

