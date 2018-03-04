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

  /**
    * Get a folder path from an environment variable.
    * @param folderPath path of the folder where to store the output file
    * @param fileName name of the output file
    * @param content string body to save to a file
    * @return either the path, or an error
    */
  def saveFileToFolder(folderPath: Path, fileName: String, content: String): Unit = {
    Files.write(Paths.get(folderPath + "/" + fileName), content.getBytes(StandardCharsets.UTF_8))
  }

  /**
    * Get a folder path from an environment variable.
    * @param ev environment variable
    * @return either the path, or an error
    */
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

  /**
    * Get a list of files, ignoring files beginning with a period.
    * @param folder where to find files
    * @return a list of files that may be empty
    */
  private def getFilesList(folder: Path): List[File] = {
    Option(folder.toFile.listFiles)
      .map(x =>
        x.toList.filterNot(file =>
          file.getName.startsWith(".")
        )
      )
      .getOrElse(List.empty[File])
  }

  /**
    * Get a list of files from an environment variable.
    * @param ev environment variable
    * @return either a list of files, or an error
    */
  def listFiles(ev: EnvironmentVariable): Either[FileError, List[File]] = {
    getFolderPath(ev)
      .fold(_ => Left(FileError.FolderPathNotFound), path => Right(getFilesList(path)))
  }

  /**
    * User defined errors regarding files and folders.
    */
  sealed abstract class FileError(val value: Short) extends ShortEnumEntry

  object FileError extends ShortEnum[FileError] {

    override def values: IndexedSeq[FileError] = findValues

    case object FolderPathNotFound extends FileError(1)

    case object FolderIsEmpty extends FileError(2)

    case object FileNotFound extends FileError(3)

    case object InputFileError extends FileError(4)

  }
}
