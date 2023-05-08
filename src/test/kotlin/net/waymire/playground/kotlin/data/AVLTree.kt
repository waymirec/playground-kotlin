package net.waymire.playground.kotlin.data

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class AVLTreeTest {
    @Test
    fun `given tree with depth of 3, when balance inspected, then each node should report accurately`() {
        //given
        val input = listOf(43, 18, 22)

        //when
        val tree = input.toAVLTree()
        val tst = listOf(18,22,43).toAVLTree()
        assertEquals(tst, tree)

        //Then
        assertFalse(tree.isBalanced)
        assertEquals(Pair(2,0), tree.balance)
        assertEquals(Pair(0,1), tree.left?.balance)
        assertEquals(Pair(0,0), tree.left?.right?.balance)
    }

    @Test
    fun `given balanced tree, when an insertion causes a left-right imbalance, then tree should be balanced with a left-right-rotation`() {
        //given
        val input = listOf(43,18,22)
        val expected = listOf(43,22,18).toAVLTree()

        //when
        val tree = input.toAVLTree()

        //then
        assertEquals(expected, tree)
    }
}

