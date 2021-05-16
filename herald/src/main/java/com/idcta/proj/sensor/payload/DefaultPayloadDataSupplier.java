//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idcta.proj.sensor.payload;

import com.idcta.proj.sensor.PayloadDataSupplier;
import com.idcta.proj.sensor.datatype.Data;
import com.idcta.proj.sensor.datatype.PayloadData;
import com.idcta.proj.sensor.datatype.PayloadTimestamp;

import java.util.ArrayList;
import java.util.List;

/// Default payload data supplier implementing fixed length payload splitting method.
public abstract class DefaultPayloadDataSupplier implements PayloadDataSupplier {

    @Override
    public List<PayloadData> payload(Data data) {
        // Get fixed length payload data
        final PayloadData fixedLengthPayloadData = payload(new PayloadTimestamp());
        final int payloadDataLength = fixedLengthPayloadData.value.length;
        // Split raw data comprising of concatenated payloads into individual payloads
        final List<PayloadData> payloads = new ArrayList<>();
        final byte[] bytes = data.value;
        for (int index = 0; (index + payloadDataLength) <= bytes.length; index += payloadDataLength) {
            final byte[] payloadBytes = new byte[payloadDataLength];
            System.arraycopy(bytes, index, payloadBytes, 0, payloadDataLength);
            payloads.add(new PayloadData(payloadBytes));
        }
        return payloads;
    }

}
