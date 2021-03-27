//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idct.id.sensor.ble;

public interface BLEDeviceDelegate {
    void device(BLEDevice device, BLEDeviceAttribute didUpdate);
}

