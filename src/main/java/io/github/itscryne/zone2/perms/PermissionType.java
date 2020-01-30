package io.github.itscryne.zone2.perms;

import java.io.Serializable;

/**
 * @serial JSON
 */
public enum PermissionType implements Serializable {
    DESTROY, BUILD, REDSTONE, DOORS, INVENTORY, ENTITY, TRANSPORT, MANAGE
}
