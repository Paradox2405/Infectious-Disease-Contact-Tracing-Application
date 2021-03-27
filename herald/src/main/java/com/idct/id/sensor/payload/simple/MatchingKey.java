//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idct.id.sensor.payload.simple;

import com.idct.id.sensor.datatype.Data;

/// Matching key
public class MatchingKey extends Data {

    public MatchingKey(Data value) {
        super(value);
    }

    public MatchingKey(byte repeating, int count) {
        super(repeating, count);
    }
}
