package com.happy.friendogly.config.datasource;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Profile("prod-replication")
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            return DataSourceType.READER;
        }
        return DataSourceType.WRITER;
    }
}
