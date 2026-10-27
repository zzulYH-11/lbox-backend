package com.lbox.lbox_backend.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    //QueryDsl 쓰는 이유 : 선택 조건이 여러개이므로 !
    /*
     * JPAQueryFactory:
     * QueryDSL 쿼리를 생성하는 객체.
     *
     * Bean으로 등록하면 Repository에서 생성자 주입받아 사용할 수 있다.
     * EntityManager는 Spring이 관리하는 것을 전달받는다.
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
