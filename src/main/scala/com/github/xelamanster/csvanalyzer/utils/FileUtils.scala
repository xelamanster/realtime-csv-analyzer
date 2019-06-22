package com.github.xelamanster.csvanalyzer.utils

import java.nio.file.Path

object FileUtils {

  def createFolderIfAbsent(outputFolder: Path): Path = {
    val folderPath = outputFolder.toFile

    if(!folderPath.exists()) {
      folderPath.mkdir()
    }

    outputFolder
  }

}
