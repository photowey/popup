/*
 * Copyright © 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.photowey.popup.app.persistence.database;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * {@code PopupDatabaseAutoConfigure}
 * popup platform database auto-configuration
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
@AutoConfiguration(after = {DataSourceAutoConfiguration.class})
public class PopupDatabaseAutoConfigure {

    /**
     * {@link PlatformTransactionManager}
     *
     * @param dataSource {@link DataSource}
     * @return {@link PlatformTransactionManager}
     */
    @Bean
    @ConditionalOnMissingBean(PlatformTransactionManager.class)
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * {@link SqlSessionTemplate}
     *
     * @param sqlSessionFactory {@link SqlSessionFactory}
     * @return {@link SqlSessionTemplate}
     */
    @Bean
    @ConditionalOnMissingBean(SqlSessionTemplate.class)
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * {@link TransactionTemplate}
     *
     * @param transactionManager {@link PlatformTransactionManager}
     * @return {@link TransactionTemplate}
     */
    @Bean
    @ConditionalOnMissingBean(TransactionTemplate.class)
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}

