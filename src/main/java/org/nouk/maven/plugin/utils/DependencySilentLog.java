package org.nouk.maven.plugin.utils;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.Logger;

/**
 * This logger implements both types of logs currently in use and turns off logs.
 * 
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 */
public class DependencySilentLog
    implements Log, Logger
{
    /**
     * @return <code>false</code>
     * @see Log#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled()
    {
        return false;
    }

    /**
     * By default, do nothing.
     *
     * @see Log#debug(CharSequence)
     */
    @Override
    public void debug( CharSequence content )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Log#debug(CharSequence, Throwable)
     */
    @Override
    public void debug( CharSequence content, Throwable error )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Log#debug(Throwable)
     */
    @Override
    public void debug( Throwable error )
    {
        // nop
    }

    /**
     * @return <code>false</code>
     * @see Log#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled()
    {
        return false;
    }

    /**
     * By default, do nothing.
     *
     * @see Log#info(CharSequence)
     */
    @Override
    public void info( CharSequence content )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Log#info(CharSequence, Throwable)
     */
    @Override
    public void info( CharSequence content, Throwable error )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Log#info(Throwable)
     */
    @Override
    public void info( Throwable error )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Log#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled()
    {
        // nop
        return false;
    }

    /**
     * By default, do nothing.
     *
     * @see Log#warn(CharSequence)
     */
    @Override
    public void warn( CharSequence content )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Log#warn(CharSequence, Throwable)
     */
    @Override
    public void warn( CharSequence content, Throwable error )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Log#warn(Throwable)
     */
    @Override
    public void warn( Throwable error )
    {
        // nop
    }

    /**
     * @return <code>false</code>
     * @see Log#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled()
    {
        return false;
    }

    /**
     * By default, do nothing.
     *
     * @see Log#error(CharSequence)
     */
    @Override
    public void error( CharSequence content )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Log#error(CharSequence, Throwable)
     */
    @Override
    public void error( CharSequence content, Throwable error )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Log#error(Throwable)
     */
    @Override
    public void error( Throwable error )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#debug(String)
     */
    @Override
    public void debug( String message )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#debug(String, Throwable)
     */
    @Override
    public void debug( String message, Throwable throwable )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#info(String)
     */
    @Override
    public void info( String message )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#info(String, Throwable)
     */
    @Override
    public void info( String message, Throwable throwable )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#warn(String)
     */
    @Override
    public void warn( String message )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#warn(String, Throwable)
     */
    @Override
    public void warn( String message, Throwable throwable )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#error(String)
     */
    @Override
    public void error( String message )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#error(String, Throwable)
     */
    @Override
    public void error( String message, Throwable throwable )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#fatalError(String)
     */
    @Override
    public void fatalError( String message )
    {
        // nop
    }

    /**
     * By default, do nothing.
     *
     * @see Logger#fatalError(String, Throwable)
     */
    @Override
    public void fatalError( String message, Throwable throwable )
    {
        // nop
    }

    /**
     * @return <code>false</code>
     * @see Logger#isFatalErrorEnabled()
     */
    @Override
    public boolean isFatalErrorEnabled()
    {
        return false;
    }

    /**
     * @return <code>null</code>
     * @see Logger#getChildLogger(String)
     */
    @Override
    public Logger getChildLogger( String name )
    {
        return null;
    }

    /**
     * @return <code>0</code>
     * @see Logger#getThreshold()
     */
    @Override
    public int getThreshold()
    {
        return 0;
    }

    /**
     * By default, do nothing
     */
    @Override
    public void setThreshold( int threshold )
    {
        // nop
    }

    /**
     * @return <code>null</code>
     * @see Logger#getName()
     */
    @Override
    public String getName()
    {
        return null;
    }
}
