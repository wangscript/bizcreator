package com.bizcreator.core.data;

/**
 * Default <code>ModelComparer</code>
 *
 * @param <M> the model type
 */
public class DefaultModelComparer<M extends ModelData> implements ModelComparer<M> {

  /**
   * Global instance.
   */
  @SuppressWarnings("unchecked")
  public static final DefaultModelComparer DFFAULT = new DefaultModelComparer();

  public boolean equals(M a, M b) {
    return (a == b || (a != null && a.equals(b)));
  }

}
