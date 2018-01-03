package com.ruchij.exceptions

object InvalidCredentials extends Exception
{
  override def getMessage: String = "Invalid credentials."
}
