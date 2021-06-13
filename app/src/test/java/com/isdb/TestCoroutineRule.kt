package com.isdb

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestCoroutineRule : TestRule {

  private val testCoroutineDispatcher = TestCoroutineDispatcher()

  private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

  @ExperimentalCoroutinesApi
  override fun apply(base: Statement, description: Description?) = object : Statement() {
    override fun evaluate() {
      Dispatchers.setMain(testCoroutineDispatcher)

      base.evaluate()

      Dispatchers.resetMain()
      testCoroutineScope.cleanupTestCoroutines()
    }
  }

  @ExperimentalCoroutinesApi
  fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
    testCoroutineScope.runBlockingTest { block() }

}