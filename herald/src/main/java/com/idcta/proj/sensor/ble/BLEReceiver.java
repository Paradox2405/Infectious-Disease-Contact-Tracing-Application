//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idcta.proj.sensor.ble;

import com.idcta.proj.sensor.Sensor;
import com.idcta.proj.sensor.SensorDelegate;
import com.idcta.proj.sensor.datatype.Data;
import com.idcta.proj.sensor.datatype.TargetIdentifier;

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
