package com.github.shynixn.astraledit.api.bukkit.business.entity;

public interface Operation {
    /**
     * Returns the operationType
     *
     * @return type
     */
    OperationType getType();

    /**
     * Returns the unknown operation Data
     *
     * @return data
     */
    Object getOperationData();

    /**
     * Sets the data of the operation
     *
     * @param operationData operationsData
     */
    void setOperationData(Object operationData);
}
