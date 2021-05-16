//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idcta.proj.sensor;

import com.idcta.proj.sensor.datatype.Data;
import com.idcta.proj.sensor.datatype.PayloadData;
import com.idcta.proj.sensor.datatype.PayloadTimestamp;

import java.util.List;

/// Payload data supplier, e.g. BeaconCodes in C19X and BroadcastPayloadSupplier in Sonar.
public interface PayloadDataSupplier {
    /// Get payload for given timestamp. Use this for integration with any payload generator, e.g. BeaconCodes or SonarBroadcastPayloadService
    PayloadData payload(PayloadTimestamp timestamp);

    /// Parse raw data into payloads
    List<PayloadData> payload(Data data);
}
