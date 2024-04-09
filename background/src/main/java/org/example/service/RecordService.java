package org.example.service;

import org.example.pojo.*;

import org.json.JSONException;

import java.io.IOException;


/**
 * @author TZH
 */
public interface RecordService {


    String recordPhysical(Physical physical) throws JSONException, IOException;

    String recordBlood(Blood blood) throws JSONException, IOException;

    String recordPressure(Pressure pressure) throws JSONException, IOException;

    String recordTemperature(Temperature temperature) throws JSONException, IOException;

    String recordUrine(Urine urine) throws JSONException, IOException;
}
