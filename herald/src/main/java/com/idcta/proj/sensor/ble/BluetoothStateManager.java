//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idcta.proj.sensor.ble;

import com.idcta.proj.sensor.datatype.BluetoothState;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface BluetoothStateManager {
    Queue<BluetoothStateManagerDelegate> delegates = new ConcurrentLinkedQueue<>();

    BluetoothState state();
}
