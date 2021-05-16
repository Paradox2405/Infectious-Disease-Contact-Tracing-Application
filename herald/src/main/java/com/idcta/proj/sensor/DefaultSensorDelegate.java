//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idcta.proj.sensor;

import com.idcta.proj.sensor.datatype.ImmediateSendData;
import com.idcta.proj.sensor.datatype.Location;
import com.idcta.proj.sensor.datatype.PayloadData;
import com.idcta.proj.sensor.datatype.Proximity;
import com.idcta.proj.sensor.datatype.SensorState;
import com.idcta.proj.sensor.datatype.SensorType;
import com.idcta.proj.sensor.datatype.TargetIdentifier;

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
