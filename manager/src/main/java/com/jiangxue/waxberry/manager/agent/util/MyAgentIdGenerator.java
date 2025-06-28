package com.jiangxue.waxberry.manager.agent.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.query.Query;
import java.util.Random;

public class MyAgentIdGenerator implements IdentifierGenerator {

    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 8;

    @Override
    public String generate(SharedSessionContractImplementor session, Object object) {
        String generatedId;
        do {
            generatedId = generateRandomId();
        } while (idExists(session, generatedId)); // 检查是否已存在
        return generatedId;
    }

    private String generateRandomId() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(ALPHA_NUMERIC.length());
            sb.append(ALPHA_NUMERIC.charAt(index));
        }
        return sb.toString();
    }

    private boolean idExists(SharedSessionContractImplementor session, String id) {
        Query<Long> query = session.createQuery(
                "SELECT COUNT(1) FROM Agent WHERE id = :id", Long.class);
        query.setParameter("id", id);
        return query.getSingleResult() > 0;
    }
}
