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

package com.agileapes.utils.spring.impl;

import com.agileapes.utils.spring.RepositoryDescriptor;
import com.agileapes.utils.spring.RepositoryEnhancer;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 18:10)
 */
public class DefaultRepositoryEnhancer implements RepositoryEnhancer {

    @Override
    public <E, K extends Serializable, R extends Repository<E, K>> R instantiate(RepositoryDescriptor<E, K, R> descriptor) {
        final RepositoryMethodInterceptor<E, K, R> callback = new RepositoryMethodInterceptor<E, K, R>(descriptor);
        return descriptor.getRepositoryType().cast(Enhancer.create(Object.class, new Class[]{descriptor.getRepositoryType()}, callback));
    }

}
