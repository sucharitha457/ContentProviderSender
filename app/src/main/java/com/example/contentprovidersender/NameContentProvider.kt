package com.example.contentprovidersender

import android.app.DownloadManager.COLUMN_ID
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class NameContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.contentprovidersender.provider"
        const val USERS_TABLE = "users"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$USERS_TABLE")

        private const val USERS = 1
        private const val USERS_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, USERS_TABLE, USERS)
            addURI(AUTHORITY, "$USERS_TABLE/#", USERS_ID)
        }
    }

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(): Boolean {
        context?.let {
            databaseHelper = DatabaseHelper(it)
        }
        return ::databaseHelper.isInitialized
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val db = databaseHelper.readableDatabase
        val cursor = when (uriMatcher.match(uri)) {
            USERS -> db.query(
                USERS_TABLE, projection, selection, selectionArgs,
                null, null, sortOrder
            )
            USERS_ID -> {
                val id = uri.lastPathSegment?.toLong()
                db.query(
                    USERS_TABLE, projection, "$COLUMN_ID = ?", arrayOf(id.toString()),
                    null, null, sortOrder
                )
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = databaseHelper.writableDatabase
        val id = when (uriMatcher.match(uri)) {
            USERS -> db.insert(USERS_TABLE, null, values)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
}
