package com.ruchij.exceptions

object InvalidAuthTokenException extends Exception
{
  override def getMessage: String = "Invalid authentication token."
}
