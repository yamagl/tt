package com.abchina.tt;

import com.abchina.tt.generated.tables.daos.HouseDao;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.junit.jupiter.api.Test;

class DSLTest {

    @Test
    public void create() {
        Environment environment = Environment.getInstance();
        Configuration configuration = new DefaultConfiguration()
                .set(SQLDialect.MYSQL)
                .set(environment.getDataSource());

        HouseDao houseDao = new HouseDao(configuration);




//        DSLContext create = new DefaultDSLContext(configuration);
//        Result<?> result = create.select()
//                .from(CMS_HOUSE)
//                .where(CMS_HOUSE.TYP.eq("1"))
//                .fetch();
    }

}