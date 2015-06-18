package com.unisoft.algotrader.provider.csv;

import com.unisoft.algotrader.core.id.InstId;
import com.unisoft.algotrader.event.data.Bar;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 6/16/15.
 */
public class CSVDataStoreTest {

    String EXPECTED = "Date,Open,High,Low,Close,Volume,OpenInt\n"+
            "19999,500.0,9999.0,100.0,600.0,0,0\n"+
            "20000,600.0,20000.0,120.0,700.0,0,0\n";

    @Test
    public void testCsvImport(){
        StringWriter sw = new StringWriter();
        CSVDataStore csvImport = new CSVDataStore(sw);

        Bar bar1 = new Bar(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 19999, 60, 9999, 100, 500, 600);
        Bar bar2 = new Bar(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(), 20000, 60, 20000, 120, 600, 700);

        csvImport.onBar(bar1);
        csvImport.onBar(bar2);
        csvImport.disconnect();

        assertEquals(EXPECTED, sw.toString());

    }
}