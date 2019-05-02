package com.abchina.tt.service;

import com.abchina.tt.Environment;
import com.abchina.tt.generated.tables.daos.HouseDao;
import com.abchina.tt.generated.tables.pojos.House;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HouseService {

    private static HouseDao houseDao;
    private static final Logger logger = LoggerFactory.getLogger(HouseService.class);

    void loadHouseData(final String filename, final String delimiter) throws IOException {
        try (InputStream inputStream = new FileInputStream(new File(filename))) {
            LineIterator iterator = IOUtils.lineIterator(inputStream, "gbk");
            iterator.forEachRemaining(line -> save(line, delimiter));
        }
    }


    void save(String line, String delimiter) {
        save(new House(Arrays.stream(line.split(delimiter))
                .map(String::trim)
                .collect(Collectors.toList())));
    }

    void save(House house) {
        initHouseDao();
        houseDao.insert(house);
    }

    public HouseDao getHouseDao() {
        initHouseDao();
        return houseDao;
    }

    private void initHouseDao()  {
        if (houseDao == null) {
            synchronized (HouseService.class) {
                if (houseDao == null) {
                    try {
                        Configuration configuration = new DefaultConfiguration()
                                .set(new DataSourceConnectionProvider(Environment.getInstance().getDataSource()))
//                                .set(Environment.getInstance().getDataSource().getConnection())
                                .set(SQLDialect.MYSQL);
                        houseDao = new HouseDao(configuration);
                    } catch (Exception e) {
                        logger.error("creating configuration error", e);
                    }
                }
            }
        }
    }
}
