//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idct.id.sensor.ble;

import com.idct.id.sensor.Sensor;
import com.idct.id.sensor.SensorDelegate;
import com.idct.id.sensor.datatype.Data;
import com.idct.id.sensor.datatype.TargetIdentifier;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Beacon receiver scans for peripherals with fixed service UUID.
 */
public interface BLEReceiver extends Sensor {
    Queue<SensorDelegate> delegates = new ConcurrentLinkedQueue<>();

    /// Immediate send data.
    boolean immediateSend(Data data, TargetIdentifier targetIdentifier);

    // Immediate send to all (connected / recent / nearby)
    boolean immediateSendAll(Data data);
}
