package com.engineering.utils

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}

import com.engineering.EnvironmentVariables
import com.engineering.EnvironmentVariables.EnvironmentVariable
import enumeratum.values.{ShortEnum, ShortEnumEntry}

import scala.collection.immutable.IndexedSeq
import scala.util.Try

object FileTools {
  def save(completePath: String, content: String): Unit = {
    Files.write(Paths.get(completePath), content.getBytes(StandardCharsets.UTF_8))
  }

  def saveFileToFolder(folderPath: Path, fileName: String, content: String): Unit = {
    Files.write(Paths.get(folderPath + "/" + fileName), content.getBytes(StandardCharsets.UTF_8))
  }

  def getFolderPath(ev: EnvironmentVariable): Either[FileError, Path] = {
    val result = for {
      pathName <- EnvironmentVariables.envOrError(ev)
      path = Paths.get(pathName)
      _ <- Try {
        Files.createDirectories(path)
      }.toEither.left.map(new Error(_))
    } yield path

    result match {
      case Left(_)     => Left(FileError.FolderPathNotFound)
      case Right(path) => Right(path)
    }
  }

  sealed abstract class FileError(val value: Short) extends ShortEnumEntry

  object FileError extends ShortEnum[FileError] {

    override def values: IndexedSeq[FileError] = findValues

    case object FolderPathNotFound extends FileError(1)

    case object FolderIsEmpty extends FileError(2)

    case object FileNotFound extends FileError(3)

    case object InputFileError extends FileError(4)

  }
}
