package net.waymire.playground.kotlin

private fun time(func: () -> Any): Long {
    val start = System.currentTimeMillis()
    func()
    return System.currentTimeMillis() - start
}