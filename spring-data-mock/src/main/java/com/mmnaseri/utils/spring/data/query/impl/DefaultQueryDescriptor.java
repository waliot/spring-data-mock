package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.InvocationMatcher;
import com.mmnaseri.utils.spring.data.domain.Modifier;
import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.Page;
import com.mmnaseri.utils.spring.data.query.PageParameterExtractor;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.query.Sort;
import com.mmnaseri.utils.spring.data.query.SortParameterExtractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.List;

/**
 * This is a mutable query descriptor that you can use to describe what a query does.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/20/15)
 */
public class DefaultQueryDescriptor implements QueryDescriptor {

    private static final Log log = LogFactory.getLog(InvocationMatcher.class);

    private final boolean distinct;
    private final String function;
    private final int limit;
    private final PageParameterExtractor pageExtractor;
    private final SortParameterExtractor sortExtractor;
    private final List<List<Parameter>> branches;
    private final RepositoryFactoryConfiguration configuration;
    private final RepositoryMetadata repositoryMetadata;

    public DefaultQueryDescriptor(boolean distinct, String function, int limit, PageParameterExtractor pageExtractor, SortParameterExtractor sortExtractor, List<List<Parameter>> branches, RepositoryFactoryConfiguration configuration, RepositoryMetadata repositoryMetadata) {
        this.distinct = distinct;
        this.function = function;
        this.limit = limit;
        this.pageExtractor = pageExtractor;
        this.sortExtractor = sortExtractor;
        this.branches = branches;
        this.configuration = configuration;
        this.repositoryMetadata = repositoryMetadata;
    }

    @Override
    public RepositoryFactoryConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public RepositoryMetadata getRepositoryMetadata() {
        return repositoryMetadata;
    }

    @Override
    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public String getFunction() {
        return function;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public Page getPage(Invocation invocation) {
        return pageExtractor == null ? null : pageExtractor.extract(invocation);
    }

    @Override
    public Sort getSort(Invocation invocation) {
        return sortExtractor == null ? null : sortExtractor.extract(invocation);
    }

    @Override
    public List<List<Parameter>> getBranches() {
        return branches;
    }

    @Override
    public boolean matches(Object entity, Invocation invocation) {
        log.info("Matching " + entity + " against " + invocation);
        final List<List<Parameter>> branches = getBranches();
        if (branches.isEmpty()) {
            return entity != null;
        }
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        for (List<Parameter> branch : branches) {
            boolean matches = true;
            for (Parameter parameter : branch) {
                final boolean ignoreCase;
                ignoreCase = parameter.getModifiers().contains(Modifier.IGNORE_CASE);
                Object value = wrapper.getPropertyValue(parameter.getPath());
                if (ignoreCase && value != null && value instanceof String) {
                    value = ((String) value).toLowerCase();
                }
                final Operator operator = parameter.getOperator();
                final Object[] properties = new Object[operator.getOperands()];
                for (int i = 0; i < operator.getOperands(); i++) {
                    properties[i] = invocation.getArguments()[parameter.getIndices()[i]];
                    if (ignoreCase && properties[i] != null && properties[i] instanceof String) {
                        properties[i] = ((String) properties[i]).toLowerCase();
                    }
                }
                if (!operator.getMatcher().matches(parameter, value, properties)) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return (getFunction() != null ? getFunction() + " " : "") + (isDistinct() ? "distinct " : "") + getBranches();
    }

}
