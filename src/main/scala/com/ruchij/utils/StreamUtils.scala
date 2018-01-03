package com.ruchij.utils

import java.io.{ByteArrayOutputStream, InputStream}

object StreamUtils
{
  def inputStreamToString(inputStream: InputStream): String =
  {
    val outputStream = new ByteArrayOutputStream()

    def copy(): String =
    {
      val value = inputStream.read()

      if (value != -1) {
        outputStream.write(value)
        copy()
      } else {
        outputStream.close()
        outputStream.toString
      }
    }

    copy()
  }

}
