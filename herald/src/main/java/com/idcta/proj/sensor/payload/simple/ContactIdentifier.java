//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idcta.proj.sensor.payload.simple;

import com.idcta.proj.sensor.datatype.Data;

/// Contact identifier
public class ContactIdentifier extends Data {

    public ContactIdentifier(Data value) {
        super(value);
    }

    public ContactIdentifier(byte repeating, int count) {
        super(repeating, count);
    }
}
