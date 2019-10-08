package io.github.itscryne.zone2;

import java.io.Serializable;

/**
 * @serial JSON
 */
public enum PermissionType implements Serializable {
    BREAK, PLACE, REDSTONE, DOORS, CONTAINER, ADMINISTRATE;
}
