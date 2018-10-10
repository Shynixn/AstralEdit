package com.github.shynixn.astraledit.bukkit.logic.business;

import com.github.shynixn.astraledit.api.bukkit.business.entity.Operation;
import com.github.shynixn.astraledit.api.bukkit.business.entity.OperationType;

/**
 * Copyright 2017 Shynixn
 * <p>
 * Do not remove this header!
 * <p>
 * Version 1.0
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class OperationImpl implements Operation {
    private final OperationType type;
    private Object operationData;

    /**
     * Initializes a new operation
     *
     * @param type type
     */
    public OperationImpl(OperationType type) {
        super();
        this.type = type;
    }

    /**
     * Returns the operationType
     *
     * @return type
     */
    public OperationType getType() {
        return this.type;
    }

    /**
     * Returns the unknown operation Data
     *
     * @return data
     */
    public Object getOperationData() {
        return this.operationData;
    }

    /**
     * Sets the data of the operation
     *
     * @param operationData operationsData
     */
    public void setOperationData(Object operationData) {
        this.operationData = operationData;
    }
}
