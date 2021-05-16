//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idcta.proj.sensor.ble;

import com.idcta.proj.sensor.datatype.BluetoothState;

public interface BluetoothStateManagerDelegate {
    void bluetoothStateManager(BluetoothState didUpdateState);
}
