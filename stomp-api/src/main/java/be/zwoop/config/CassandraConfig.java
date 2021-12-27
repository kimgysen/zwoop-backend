package be.zwoop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Profile({"dev","test"})
@Configuration
@EnableCassandraRepositories
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Value("${cassandra.host}")
    private String host;

    @Value("${cassandra.port}")
    private int port;


    @Override
    @Nonnull
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Override
    @Nonnull
    public String getContactPoints() {
        return host;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    @Nonnull
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(
                CreateKeyspaceSpecification.createKeyspace("zwoop_chat")
                        .ifNotExists()
                        .withSimpleReplication(1));
    }

}
