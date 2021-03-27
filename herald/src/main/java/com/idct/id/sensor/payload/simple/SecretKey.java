//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idct.id.sensor.payload.simple;

import com.idct.id.sensor.datatype.Data;

/// Secret key
public class SecretKey extends Data {

    public SecretKey(byte[] value) {
        super(value);
    }

    public SecretKey(byte repeating, int count) {
        super(repeating, count);
    }
}
