package net.waymire.playground.kotlin.data.tree.bst

import org.hamcrest.CoreMatchers.anyOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat


class RecursiveBinarySearchTreeTest {
    @ParameterizedTest
    @MethodSource("net.waymire.playground.kotlin.data.tree.bst.RecursiveBinarySearchTreeTest#setOfIntegers")
    fun `given a BST, when in-order sort requested, then should sort in-order`(values: Set<Int>) {
        val tree = values.toRecursiveBinarySearchTree()
        val expected = values.sorted()
        val sorted = tree.traverseInOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BST, when pre-order sort requested, then should sort pre-order`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 55)
        val expected = values.toList()
        val tree = values.toRecursiveBinarySearchTree()
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BST, when post-order sort requested, then should sort post-order`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 55)
        val expected = listOf(15, 20, 18, 47, 55, 50, 43)
        val tree = values.toRecursiveBinarySearchTree()
        val sorted = tree.traversePostOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BST, when breadth-first-order sort requested, then should sort breadth-first-order`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 55)
        val expected = listOf(43,18,50,15,20,47,55)
        val tree = values.toRecursiveBinarySearchTree()
        val sorted = tree.traverseBreadthFirst()
        assertEquals(expected, sorted)
    }

    @ParameterizedTest
    @MethodSource("net.waymire.playground.kotlin.data.tree.bst.RecursiveBinarySearchTreeTest#setOfIntegers")
    fun `given value exists in tree, when contains called, then return true`(values: Set<Int>) {
        val tree = values.toRecursiveBinarySearchTree()
        val check = values.last()
        val result = tree.contains(check)
        assertTrue(result)
    }

    @ParameterizedTest
    @MethodSource("net.waymire.playground.kotlin.data.tree.bst.RecursiveBinarySearchTreeTest#setOfIntegers")
    fun `given value does not exist in tree, when contains called, then return false`(values: Set<Int>) {
        val tree = values.toRecursiveBinarySearchTree()
        val result = tree.contains(Int.MAX_VALUE)
        assertFalse(result)
    }

    @Test
    fun `given a BST, when a right leaf node is removed, then properly remove node`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 46, 55, 53)
        val expected = listOf(43, 18, 15, 20, 50, 47, 55, 53)
        val tree = values.toRecursiveBinarySearchTree()
        tree.remove(46)
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BST, when a right node with 1 child is removed, then properly remove node`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 46, 55, 53)
        val expected = listOf(43, 18, 15, 20, 50, 46, 55, 53)
        val tree = values.toRecursiveBinarySearchTree()
        tree.remove(47)
        val sorted = tree.traversePreOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BST, when a right node with 2 children is removed, then properly remove node and update root`() {
        val values =   listOf(43, 18, 15, 20, 50, 47, 46, 55, 53)
        val expected1 = listOf(43, 18, 15, 20, 53, 47, 46, 55)
        val expected2 = listOf(43, 18, 15, 20, 47, 46, 55, 53)
        val tree = values.toRecursiveBinarySearchTree()
        tree.remove(50)
        val sorted = tree.traversePreOrder()
        assertThat(sorted, anyOf(`is`(expected1), `is`(expected2)))
    }

    companion object {
        @JvmStatic
        fun setOfIntegers(): Stream<Set<Int>> = Stream.of(
            (1..100).map { Random.nextInt(1, 1000) }.toSet(),
            (1..1_000).map { Random.nextInt(1, 10_000) }.toSet(),
            (1..10_000).map { Random.nextInt(1, 100_000) }.toSet(),
            (1..100_000).map { Random.nextInt(1, 1_000_000) }.toSet(),
            (1..1_000_000).map { Random.nextInt(1, 10_000_000) }.toSet()
        )
    }
}