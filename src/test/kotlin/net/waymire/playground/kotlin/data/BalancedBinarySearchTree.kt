package net.waymire.playground.kotlin.data

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BalancedBinarySeareeTest {
    @ParameterizedTest
    @MethodSource("net.waymire.playground.kotlin.data.BalancedBinarySearchTreeTest#setOfIntegers")
    fun `given a BBST, when in-order sort requested, then should sort in-order`(values: Set<Int>) {
        val tree = values.toBalancedBinarySearchTree()
        val expected = values.sorted()
        val sorted = tree.traverseInOrder()
        assertEquals(values.size, sorted.size)
        assertEquals(expected, sorted, "sorted output does not match sorted input")
    }

    @Test
    fun `given a BBST, when pre-order sort requested, then should sort pre-order`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 55)
        val expected = values.toList()
        val tree = values.toBalancedBinarySearchTree()
        val sorted = tree.traversePreOrder()
        assertEquals(values.size, sorted.size)
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BBST, when post-order sort requested, then should sort post-order`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 55)
        val expected = listOf(15, 20, 18, 47, 55, 50, 43)
        val tree = values.toBalancedBinarySearchTree()
        val sorted = tree.traversePostOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BBST, when breadth-first-order sort requested, then should sort breadth-first-order`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 55)
        val expected = listOf(43,18,50,15,20,47,55)
        val tree = values.toBalancedBinarySearchTree()
        val sorted = tree.traverseBreadthFirst()
        assertEquals(expected, sorted)
    }

    @ParameterizedTest
    @MethodSource("net.waymire.playground.kotlin.data.BalancedBinarySearchTreeTest#setOfIntegers")
    fun `given value exists in BBST, when contains called, then return true`(values: Set<Int>) {
        val tree = values.toBalancedBinarySearchTree()
        val check = values.last()
        val result = tree.contains(check)
        assertTrue(result)
    }

    @ParameterizedTest
    @MethodSource("net.waymire.playground.kotlin.data.BalancedBinarySearchTreeTest#setOfIntegers")
    fun `given value does not exist in BBST, when contains called, then return false`(values: Set<Int>) {
        val tree = values.toBalancedBinarySearchTree()
        val result = tree.contains(Int.MAX_VALUE)
        assertFalse(result)
    }

    @Test
    fun `given a BBST, when a right leaf node is removed, then properly remove node`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 46, 55, 53)
        val expected = listOf(43, 18, 15, 20, 50, 47, 55, 53)
        val tree = values.toBalancedBinarySearchTree()
        tree.remove(46)
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BBST, when a right node with 1 child is removed, then properly remove node`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 46, 55, 53)
        val expected = listOf(43, 18, 15, 20, 50, 46, 55, 53)
        val tree = values.toBalancedBinarySearchTree()
        tree.remove(47)
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BBST, when a right node with 2 children is removed, then properly remove node and update root`() {
        val values =   listOf(43, 18, 15, 20, 50, 47, 46, 55, 53)
        val expected1 = listOf(43, 18, 15, 20, 53, 47, 46, 55)
        val expected2 = listOf(43, 18, 15, 20, 47, 46, 55, 53)
        val tree = values.toBalancedBinarySearchTree()
        tree.remove(50)
        val sorted = tree.traversePreOrder()
        assertThat(sorted, anyOf(`is`(expected1), `is`(expected2)))
    }


    @Test
    fun `given a BBST, when a node is added, then update height`() {
        val values =   listOf(43, 18, 15, 20, 50, 47, 46, 55, 53)
        val tree = values.toBalancedBinarySearchTree()
        assertThat(tree.height, `is`(3))
    }

    @Test
    fun `given a BBST, when a right leaf node is removed, then update height`() {
        val values =   listOf(43, 18, 15, 20, 50, 47, 46, 55)
        val tree = values.toBalancedBinarySearchTree()
        assertThat(tree.height, `is`(3))
        tree.remove(46)
        assertThat(tree.height, `is`(2))
    }

    @Test
    fun `given a BBST, when a right node with 1 child is removed, then update height`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 46, 55)
        val tree = values.toBalancedBinarySearchTree()
        assertThat(tree.height, `is`(3))
        tree.remove(47)
        assertThat(tree.height, `is`(2))
    }

    @Test
    fun `given a BBST, when a right node with 2 children is removed, then update height`() {
        val values =   listOf(43, 18, 15, 20, 50, 47, 46, 55)
        val expected = listOf(43, 18, 15, 20, 47, 46, 55)
        val tree = values.toBalancedBinarySearchTree()
        assertThat(tree.height, `is`(3))
        tree.remove(50)
        assertThat(tree.height, anyOf(`is`(2), `is`(3)))

        val sorted = tree.traversePreOrder()
        assertThat(sorted, `is`(expected))
    }

    @Test
    fun `given a balanced BBST, when balance is checked, then return true`() {
        val values = listOf(100, 50, 150, 40, 60)
        val tree = values.toBalancedBinarySearchTree()
        assertTrue(tree.isBalanced)
        assertTrue(tree.isLeftHeavy)
        assertFalse(tree.isRightHeavy)
    }


    @Test
    fun `given a balanced BBST, when the tree is left-heavy, then perform right rotation`() {
        val values = listOf(500, 400, 600, 300)
        val expected = listOf(500, 300, 200, 400, 600)
        val tree = values.toBalancedBinarySearchTree()
        tree.add(200)
        assertTrue(tree.isBalanced)
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given another balanced BBST, when the tree is left-heavy, then perform right rotation`() {
        val values = listOf(100, 50, 400, 300)
        val expected = listOf(100, 50, 300, 200, 400)
        val tree = values.toBalancedBinarySearchTree()
        tree.add(200)
        assertTrue(tree.isBalanced)
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a balanced BBST, when the tree is right-heavy, then perform left rotation`() {
        val values = listOf(500, 400, 600, 700)
        val expected = listOf(500, 400, 700, 600, 800)
        val tree = values.toBalancedBinarySearchTree()
        tree.add(800)
        assertTrue(tree.isBalanced)
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a balanced BBST, when the left subtree becomes right heavy, then perform a Right-Left rotation`() {
        val values = listOf(200, 100)
        val expected = listOf(150, 100, 200)
        val tree = values.toBalancedBinarySearchTree()
        tree.add(150)
        assertTrue(tree.isBalanced)
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a balanced BBST, when the right subtree becomes left heavy, then perform a Left-Right rotation`() {
        val values = listOf(200, 100)
        val expected = listOf(150, 100, 200)
        val tree = values.toBalancedBinarySearchTree()
        tree.add(150)
        assertTrue(tree.isBalanced)
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    companion object {
        @JvmStatic
        fun setOfIntegers() = Stream.of(
            (1..10).map { Random.nextInt(1, 1000) }.toSet(),
            (1..100).map { Random.nextInt(1, 1000) }.toSet(),
            (1..1_000).map { Random.nextInt(1, 10_000) }.toSet(),
            (1..10_000).map { Random.nextInt(1, 100_000) }.toSet(),
            (1..100_000).map { Random.nextInt(1, 1_000_000) }.toSet(),
            (1..1_000_000).map { Random.nextInt(1, 10_000_000) }.toSet(),
            (1..10_000_000).map { Random.nextInt(1, 100_000_000) }.toSet(),
            //(1..100_000_000).map { Random.nextInt(1, 900_000_000) }.toSet()
        )
    }
}