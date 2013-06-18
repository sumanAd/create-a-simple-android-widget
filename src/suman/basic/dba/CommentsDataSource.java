package suman.basic.dba;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CommentsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private Msqlite dbHelper;
  private String[] allColumns = { Msqlite.COLUMN_ID,
      Msqlite.COLUMN_COMMENT };

  public CommentsDataSource(Context context) {
    dbHelper = new Msqlite(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Comment createComment(String comment) {
    ContentValues values = new ContentValues();
    //ContentValues class is used to store a set of values that the ContentResolver can process
    //here values.put just store the data && insertion to database is done by
    //.insert operation whith takes the contentvalues obj  ie "values"
    values.put(Msqlite.COLUMN_COMMENT, comment);
    long insertId = database.insert(Msqlite.TABLE_COMMENTS, null,
        values);
    Cursor cursor = database.query(Msqlite.TABLE_COMMENTS,
        allColumns, Msqlite.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Comment newComment = cursorToComment(cursor);
    cursor.close();
    return newComment;
  }

  public void deleteComment(Comment comment) {
    long id = comment.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(Msqlite.TABLE_COMMENTS, Msqlite.COLUMN_ID
        + " = " + id, null);
  }

  public List<Comment> getAllComments() {
    List<Comment> comments = new ArrayList<Comment>();

    //cursor is pointing to whole database table
    Cursor cursor = database.query(Msqlite.TABLE_COMMENTS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    //isAfterLast()-> Returns whether the cursor is pointing to the position after the last row.
    while (!cursor.isAfterLast()) {
      Comment comment = cursorToComment(cursor);
      comments.add(comment);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return comments;
  }

  private Comment cursorToComment(Cursor cursor) {
    Comment comment = new Comment();
    comment.setId(cursor.getLong(0));// cursor.getLong(0)->Returns the value of the requested column as a long.
    comment.setComment(cursor.getString(1));
    return comment;
  }
} 