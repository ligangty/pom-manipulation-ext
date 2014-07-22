/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.commonjava.maven.ext.manip.state;

import java.util.Properties;

import org.commonjava.maven.ext.manip.impl.DependencyManipulator;

/**
 * Captures configuration relating to dependency alignment from the POMs. Used by {@link DependencyManipulator}.
 */
public class DependencyState
    implements State
{
    /**
     * The character used to separate groupId:arifactId:version
     */
    public static final String GAV_SEPERATOR = ":";

    /**
     * The String that needs to be prepended a system property to make it a dependencyExclusion.
     * For example to exclude junit alignment for the GAV (org.groupId:artifactId)<br/>
     * <code>-DdependencyExclusion.junit:junit@org.groupId:artifactId</code>
     */
    public static final String DEPENDENCY_EXCLUSION_PREFIX = "dependencyExclusion.";

    /**
     * Two possible formats currently supported for version property output:</br>
     * <code>version.group</code></br>
     * <code>version.group.artifact</code></br>
     * <code>none</code> (equates to off)</br>
     * Configured by the property <code>-DversionPropertyFormat=[VG|VGA|NONE]</code>
     */
    public static enum VersionPropertyFormat
    {
        VG,
        VGA,
        NONE;
    }

    /**
     * The name of the property which contains the GAV of the remote pom from which to retrieve dependency management
     * information. <br />
     *<code>-DdependencyManagement:org.foo:bar-dep-mgmt:1.0</code>
     */
    public static final String DEPENDENCY_MANAGEMENT_POM_PROPERTY = "dependencyManagement";

    private final String depMgmt;

    private final boolean overrideTransitive;

    private final boolean overrideDependencies;

    public DependencyState( final Properties userProps )
    {
        depMgmt = userProps.getProperty( DEPENDENCY_MANAGEMENT_POM_PROPERTY );
        overrideTransitive = Boolean.valueOf( userProps.getProperty( "overrideTransitive", "true" ) );
        overrideDependencies = Boolean.valueOf( userProps.getProperty( "overrideDependencies", "true" ) );
    }

    /**
     * Enabled ONLY if repo-reporting-removal is provided in the user properties / CLI -D options.
     *
     * @see #ENFORCE_SYSPROP
     * @see org.commonjava.maven.ext.manip.state.State#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        return ( depMgmt != null && depMgmt.length() > 0 );
    }

    /**
     * Whether to override unmanaged transitive dependencies in the build. Has the effect of adding (or not) new entries
     * to dependency management when no matching dependency is found in the pom. Defaults to true.
     */
    public boolean getOverrideTransitive()
    {
        return overrideTransitive;
    }

    /**
     * Whether to override managed dependencies in the build. Defaults to true.
     */
    public boolean getOverrideDependencies()
    {
        return overrideDependencies;
    }

    public String getRemoteDepMgmt()
    {
        return depMgmt;
    }

}