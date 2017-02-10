package com.zestedesavoir.zdsnotificateur.internal.database;

import java.util.List;

/**
 * Generic interface to create a dao for a given model and
 * specified in the generic type <code>T</code>.
 *
 * @author Gerard Paligot
 */
public interface Dao<T> {
  /**
   * Save a list of model in the system.
   *
   * @param cascade If the model have a dependency to one or several models, this boolean is used to save these dependencies.
   * @param models  List of model to saved.
   * @return List of identifier.
   */
  long[] save(boolean cascade, T... models);

  /**
   * Update a list of model in the system.
   *
   * @param cascade If the model have a dependency to one or several models, this boolean is used to update these dependencies.
   * @param models  List of model to updated.
   * @return Number of line updated.
   */
  int update(boolean cascade, T... models);

  /**
   * Get all entries of dao model.
   *
   * @return List of {@link T}.
   */
  List<T> getAll();

  /**
   * Get the entry for the given key.
   *
   * @param key Probably an identifier.
   * @return The model which correspond to the result of the request.
   */
  T get(int key);

  /**
   * Delete a specific model for the given key.
   *
   * @param key Probably an identifier.
   * @return Number of line deleted.
   */
  int delete(int key);

  /**
   * Flush the database for the model.
   *
   * @return Number of line deleted.
   */
  int deleteAll();
}
