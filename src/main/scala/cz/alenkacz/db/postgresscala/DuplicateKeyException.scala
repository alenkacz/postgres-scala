package cz.alenkacz.db.postgresscala

class DuplicateKeyException(message: String, innerException: Exception)
    extends Exception(message, innerException) {}
