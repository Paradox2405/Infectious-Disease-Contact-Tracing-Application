//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idcta.proj.sensor.datatype;

/// Sensor state
public enum SensorState {
    /// Sensor is powered on, active and operational
    on,
    /// Sensor is powered off, inactive and not operational
    off,
    /// Sensor is not available
    unavailable
}
