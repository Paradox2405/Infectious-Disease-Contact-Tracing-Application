//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idct.id.sensor;

import com.idct.id.sensor.datatype.ImmediateSendData;
import com.idct.id.sensor.datatype.Location;
import com.idct.id.sensor.datatype.PayloadData;
import com.idct.id.sensor.datatype.Proximity;
import com.idct.id.sensor.datatype.SensorState;
import com.idct.id.sensor.datatype.SensorType;
import com.idct.id.sensor.datatype.TargetIdentifier;

import java.util.List;

/// Default implementation of SensorDelegate for making all interface methods optional.
public abstract class DefaultSensorDelegate implements SensorDelegate {

    @Override
    public void sensor(SensorType sensor, TargetIdentifier didDetect) {
    }

    @Override
    public void sensor(SensorType sensor, PayloadData didRead, TargetIdentifier fromTarget) {
    }

    @Override
    public void sensor(SensorType sensor, ImmediateSendData didReceive, TargetIdentifier fromTarget) {
    }

    @Override
    public void sensor(SensorType sensor, List<PayloadData> didShare, TargetIdentifier fromTarget) {
    }

    @Override
    public void sensor(SensorType sensor, Proximity didMeasure, TargetIdentifier fromTarget) {
    }

    @Override
    public void sensor(SensorType sensor, Location didVisit) {
    }

    @Override
    public void sensor(SensorType sensor, Proximity didMeasure, TargetIdentifier fromTarget, PayloadData withPayload) {
    }

    @Override
    public void sensor(SensorType sensor, SensorState didUpdateState) {
    }
}
