package com.engineering.utils

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}

import scala.util.Try

object FileTools {
  def save(completePath: String, content: String): Unit = {
    Files.write(Paths.get(completePath), content.getBytes(StandardCharsets.UTF_8))
  }

  def getFilesList(from: Path): Either[Throwable, List[File]] = {
    Try(from.toFile.listFiles.toList).map { (files: List[File]) =>
      files.filter(file => !file.getName.startsWith("."))
    }.toEither
  }
}
