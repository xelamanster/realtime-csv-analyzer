package com.github.xelamanster.csvanalyzer.utils

class TestTimer(var time: Long = 0) extends Timer {

  override def currentTimeMillis(): Long = time

}
