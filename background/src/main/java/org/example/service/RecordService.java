package org.example.service;

import org.example.pojo.Record;
import org.json.JSONException;

import java.io.IOException;


/**
 * @author TZH
 */
public interface RecordService {

    String physicalMeasurement(Record record) throws JSONException, IOException;

}
