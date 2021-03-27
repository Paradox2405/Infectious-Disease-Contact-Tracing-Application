//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idct.id.sensor.ble;

import com.idct.id.sensor.datatype.BluetoothState;

public interface BluetoothStateManagerDelegate {
    void bluetoothStateManager(BluetoothState didUpdateState);
}
