package com.ruchij.exceptions

object ExpiredAuthTokenException extends Exception
{
  override def getMessage: String = "Expired authentication token."
}
