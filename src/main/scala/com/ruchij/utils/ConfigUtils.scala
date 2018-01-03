package com.ruchij.utils

import com.ruchij.exceptions.EnvValueUndefinedException

import scala.util.{Failure, Success, Try}

object ConfigUtils
{
  def getEnvValue(name: String): Try[String] =
    sys.env.get(name)
      .fold[Try[String]](Failure(EnvValueUndefinedException(name)))(Success(_))
}
