//  Copyright 2020 VMware, Inc.
//  SPDX-License-Identifier: Apache-2.0
//

package com.idct.id.sensor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.idct.id.sensor.ble.BLESensorConfiguration;
import com.idct.id.sensor.ble.ConcreteBLESensor;
import com.idct.id.sensor.data.BatteryLog;
import com.idct.id.sensor.data.ConcreteSensorLogger;
import com.idct.id.sensor.data.ContactLog;
import com.idct.id.sensor.data.DetectionLog;
import com.idct.id.sensor.data.EventTimeIntervalLog;
import com.idct.id.sensor.data.SensorLogger;
import com.idct.id.sensor.data.StatisticsLog;
import com.idct.id.sensor.datatype.Data;
import com.idct.id.sensor.datatype.PayloadData;
import com.idct.id.sensor.datatype.PayloadTimestamp;
import com.idct.id.sensor.datatype.TargetIdentifier;
import com.idct.id.sensor.datatype.TimeInterval;
import com.idct.id.sensor.service.ForegroundService;

import java.util.ArrayList;
import java.util.List;

/// Sensor array for combining multiple detection and tracking methods.
public class SensorArray implements Sensor {
    private final Context context;
    private final SensorLogger logger = new ConcreteSensorLogger("Sensor", "SensorArray");
    private final List<Sensor> sensorArray = new ArrayList<>();

    private final PayloadData payloadData;
    public final static String deviceDescription = android.os.Build.MODEL + " (Android " + android.os.Build.VERSION.SDK_INT + ")";

    private final ConcreteBLESensor concreteBleSensor;

    public SensorArray(final Context context, PayloadDataSupplier payloadDataSupplier) {
        this.context = context;
        // Ensure logger has been initialised (should have happened in AppDelegate already)
        ConcreteSensorLogger.context(context);
        logger.debug("init");

        // Start foreground service to enable background scan
        final Intent intent = new Intent(context, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

        // Define sensor array
        concreteBleSensor = new ConcreteBLESensor(context, payloadDataSupplier);
        sensorArray.add(concreteBleSensor);

        // Loggers
        payloadData = payloadDataSupplier.payload(new PayloadTimestamp());
		if (com.idct.id.BuildConfig.DEBUG) {
	        add(new ContactLog(context, "contacts.csv"));
	        add(new StatisticsLog(context, "statistics.csv", payloadData));
	        add(new DetectionLog(context,"detection.csv", payloadData));
	        new BatteryLog(context, "battery.csv");
            if (BLESensorConfiguration.payloadDataUpdateTimeInterval != TimeInterval.never) {
                add(new EventTimeIntervalLog(context, "statistics_didRead.csv", payloadData, EventTimeIntervalLog.EventType.read));
            }
		}
        logger.info("DEVICE (payload={},description={})", payloadData.shortName(), deviceDescription);
    }

    /// Immediate send data.
    public boolean immediateSend(Data data, TargetIdentifier targetIdentifier) {
        return concreteBleSensor.immediateSend(data,targetIdentifier);
    }

    /// Immediate send to all (connected / recent / nearby)
    public boolean immediateSendAll(Data data) {
        return concreteBleSensor.immediateSendAll(data);
    }

    public final PayloadData payloadData() {
        return payloadData;
    }

    @Override
    public void add(final SensorDelegate delegate) {
        for (Sensor sensor : sensorArray) {
            sensor.add(delegate);
        }
    }

    @Override
    public void start() {
        logger.debug("start");
        for (Sensor sensor : sensorArray) {
            sensor.start();
        }
    }

    @Override
    public void stop() {
        logger.debug("stop");
        for (Sensor sensor : sensorArray) {
            sensor.stop();
        }
    }
}
