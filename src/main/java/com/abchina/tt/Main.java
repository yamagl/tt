package com.abchina.tt;


import com.abchina.tt.generated.tables.House;
import com.abchina.tt.generated.tables.records.HouseRecord;
import com.abchina.tt.service.HouseService;
import com.abchina.tt.service.StaticMapService;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        HouseService houseService = new HouseService();
        BigDecimal threshold = new BigDecimal("3000000");
        DSLContext create = new DefaultDSLContext(houseService.getHouseDao().configuration());
        try (Cursor<HouseRecord> cursor = create.selectFrom(House.HOUSE_)
                .where(House.HOUSE_.ADDRESS.isNotNull()
                        .and(House.HOUSE_.PRICE.lt(threshold)))
                .fetchLazy()) {
            for (int photoCount = 0; photoCount < 100 && cursor.hasNext(); photoCount++) {
                Result<HouseRecord> records = cursor.fetchNext(10);
                try {
                    StaticMapService.markStaticMap(new ArrayList<>(records.map(HouseRecord::getAddress)));
                } catch (IOException | URISyntaxException e) {
                   logger.error("Error occur", e);
                }

                logger.info(String.format("already product %d photos", photoCount + 1));
            }
        }
    }
}
