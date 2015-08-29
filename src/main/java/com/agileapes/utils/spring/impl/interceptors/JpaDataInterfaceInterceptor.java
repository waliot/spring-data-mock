/*
 * Copyright (c) 2014 Milad Naseri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agileapes.utils.spring.impl.interceptors;

import com.agileapes.utils.spring.InterfaceInterceptor;
import com.agileapes.utils.spring.RepositoryDescriptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.agileapes.utils.spring.tools.EntityTools.iterableToList;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/23 AD, 12:48)
 */
public class JpaDataInterfaceInterceptor<E, K extends Serializable> implements JpaRepository<E, K>, InterfaceInterceptor<E, K> {

    private final DefaultDataInterfaceInterceptor<E, K> interceptor;

    public JpaDataInterfaceInterceptor(Map<K, E> data, RepositoryDescriptor<E, K, ?> descriptor) {
        interceptor = new DefaultDataInterfaceInterceptor<E, K>(data, descriptor);
    }

    @Override
    public <S extends E> S save(S entity) {
        return interceptor.save(entity);
    }

    @Override
    public E findOne(K k) {
        return interceptor.findOne(k);
    }

    @Override
    public boolean exists(K k) {
        return interceptor.exists(k);
    }

    @Override
    public List<E> findAll() {
        return iterableToList(interceptor.findAll());
    }

    @Override
    public List<E> findAll(Sort sort) {
        return iterableToList(interceptor.findAll());
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return interceptor.findAll(pageable);
    }

    @Override
    public List<E> findAll(Iterable<K> ks) {
        return iterableToList(interceptor.findAll(ks));
    }

    @Override
    public long count() {
        return interceptor.count();
    }

    @Override
    public void delete(K k) {
        interceptor.delete(k);
    }

    @Override
    public void delete(E entity) {
        interceptor.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends E> entities) {
        interceptor.delete(entities);
    }

    @Override
    public void deleteAll() {
        interceptor.deleteAll();
    }

    @Override
    public void flush() {

    }

    @Override
    public void deleteInBatch(Iterable<E> entities) {
        delete(entities);
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Override
    public E getOne(K k) {
        return findOne(k);
    }

    @Override
    public <S extends E> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public <S extends E> List<S> save(Iterable<S> entities) {
        return iterableToList(interceptor.save(entities));
    }

}
